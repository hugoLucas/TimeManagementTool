import net.sf.dynamicreports.examples.Templates;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 * Created by Hugo on 11/14/2016.
 */
public class ProgressReport implements ActionListener {
    /**
     * Generates a summary of project progress
     *
     * @param e     Action which triggered call
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        DB_Reader reader = new DB_Reader();
        ArrayList<ProjectLine> proj = reader.genProgressReport();

        Date intervalStartTime = new Date(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        Date intervalEndTime = new Date(cal.getTimeInMillis());

        ArrayList<EmployeeLog> log = reader.genManagerTimeSheet(0,0,0,intervalStartTime,intervalEndTime);

        this.build(proj,log);
    }

    public void build(ArrayList<ProjectLine> proj, ArrayList<EmployeeLog> log){
        /* For first portion of report */
        TextColumnBuilder<String> projectColumn = col.column("Project Name", "project", type.stringType());
        TextColumnBuilder<Integer> workedColumn = col.column("Hours Completed", "completed", type.integerType());
        TextColumnBuilder<Integer> totalColumn = col.column("Total Hours Needed", "needed", type.integerType());

        TextColumnBuilder<String> clockInColumn = col.column("Clock-In Time", "clockin", type.stringType());
        TextColumnBuilder<String> clockOutColumn = col.column("Clock-Out Time", "clockout", type.stringType());
        TextColumnBuilder<String> dateColumn = col.column("Date", "date", type.stringType());
        TextColumnBuilder<Integer> taskColumn = col.column("Task ID", "task", type.integerType());
        TextColumnBuilder<Integer> empColumn = col.column("Employee ID", "employee", type.integerType());

        DRDataSource dataSourceOne = new DRDataSource("project", "completed", "needed");
        DRDataSource dataSourceTwo = new DRDataSource("task", "employee", "date", "clockin", "clockout");

        for(EmployeeLog l: log)
            if(l.getClockOut()!=null)
                dataSourceTwo.add(l.getTaskID(), l.getEmployeeID(), l.getLogDate().toString(), l.getClockIn().toString(), l.getClockOut().toString());
            else
                dataSourceTwo.add(l.getTaskID(), l.getEmployeeID(), l.getLogDate().toString(), l.getClockIn().toString(), "IN PROGRESS");

        for(ProjectLine p: proj)
            dataSourceOne.add(p.getProjectName(), p.getHoursWorkedOnProject(), p.getEstimatedHoursToCompletion() - p.getHoursWorkedOnProject());

        JasperReportBuilder yearToDate = report().setTemplate(Templates.reportTemplate)
                .columns(projectColumn, workedColumn, totalColumn)
                .title(
                        cmp.text("Weekly Summary Report"),
                        cht.stackedBar3DChart()
                                .setTitle("Total Progress Summary")
                                .setCategory(projectColumn)
                                .series(
                                        cht.serie(totalColumn), cht.serie(workedColumn))
                                .setCategoryAxisFormat(
                                        cht.axisFormat().setLabel("Projects")))
                .pageFooter(Templates.footerComponent)
                .setDataSource(dataSourceOne);

        JasperReportBuilder weeklySummary = report().setTemplate(Templates.reportTemplate)
                .columns(taskColumn, empColumn, dateColumn, clockInColumn, clockOutColumn)
                .setDataSource(dataSourceTwo);

        try {
            report()
                    .title(cmp.verticalList(cmp.subreport(yearToDate), cmp.subreport(weeklySummary)))
                    .show(false);
        } catch (DRException e) {
            e.printStackTrace();
        }
    }
}

