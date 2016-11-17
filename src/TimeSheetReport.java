import net.sf.dynamicreports.adhoc.AdhocManager;
import net.sf.dynamicreports.adhoc.configuration.AdhocColumn;
import net.sf.dynamicreports.adhoc.configuration.AdhocConfiguration;
import net.sf.dynamicreports.adhoc.configuration.AdhocReport;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.sql.Time;
import java.util.ArrayList;


/**
 * Created by Hugo Lucas on 11/16/2016.
 */
public class TimeSheetReport {

    private ArrayList<EmployeeLog> dataSource;     /* Source of all time sheet report information */
    private boolean includeEmpID;                  /* Used to determine if the employee id should go into report */

    public TimeSheetReport(ArrayList<EmployeeLog> list, boolean managerReport){
        this.dataSource = list;
        this.includeEmpID = managerReport;
    }

    public void buildReport(){
        AdhocConfiguration configuration = new AdhocConfiguration();
        AdhocReport report = new AdhocReport();
        configuration.setReport(report);

        String [] columnNames = {"EmployeeID", "Task ID", "Log Date", "Clock-In Time", "Clock-Out Time"};
        int index = 0;
        if(!this.includeEmpID)
            index ++;

        for(; index < columnNames.length; index ++){
            AdhocColumn column = new AdhocColumn();
            column.setName(columnNames[index]);
            report.addColumn(column);
        }

        try{
            JasperReportBuilder repBuildr = AdhocManager.createReport(configuration.getReport());
            repBuildr.setDataSource(this.setData());
            repBuildr.show(false);
        }catch(DRException e){
            e.printStackTrace();
        }
    }

    private JRDataSource setData(){
        DRDataSource dataSource = null;
        if(!this.includeEmpID)
            dataSource = new DRDataSource("Task ID", "Log Date", "Clock-In Time", "Clock-Out Time");
        else
            dataSource = new DRDataSource("EmployeeID", "Task ID", "Log Date", "Clock-In Time", "Clock-Out Time");

        for(EmployeeLog log: this.dataSource){
            String clockOut = "In-Progress";
            Time cOut = log.getClockOut();
            if(cOut!=null)
                clockOut = cOut.toString();

            if(!this.includeEmpID)
                dataSource.add(log.getTaskID(), log.getLogDate().toString(), log.getClockIn().toString(), clockOut);
            else
                dataSource.add(log.getEmployeeID(), log.getTaskID(), log.getLogDate().toString(), log.getClockIn().toString(), clockOut);
        }

        return dataSource;
    }
}
