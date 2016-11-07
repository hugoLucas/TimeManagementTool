import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Hugo Lucas on 11/5/2016.
 */
public class GenTimeSheet {

    private String timeIntervalSelected;
    private int projectIDSeleted;
    private int taskIDSelected;
    private int employeeStatus;     /* 0 if not manager, 1 if manager */
    private int employeeID;

    private Date start;
    private Date end;

    public GenTimeSheet(String status, int employeeNumber, int projID, int taskID, String timeInt){
        if(status.equals("Manager"))
            this.employeeStatus = 1;
        else
            this.employeeStatus = 0;

        this.timeIntervalSelected = timeInt;
        this.employeeID = employeeNumber;
        this.projectIDSeleted = projID;
        this.taskIDSelected = taskID;

        this.start = null;
        this.end = null;
    }

    public StringBuilder createReport(){
        this.intervalToDates();

        if(employeeStatus > 0)
            return this.createManagerReport();
        else
            return this.createDevReport();
    }

    private StringBuilder createDevReport(){
        DB_Reader reader = new DB_Reader();
        ArrayList<EmployeeLog> recs = reader.genEmployeeTimeSheet(employeeID, projectIDSeleted, taskIDSelected, start, end);

        if(recs.size() > 0) {
            StringBuilder out = new StringBuilder();
            for (EmployeeLog el : recs) {
                out.append(el.printLog());
                out.append(System.getProperty("line.separator"));
            }

            return out;
        }else{
            return null;
        }
    }

    private void intervalToDates(){
        long intervalStartTime = System.currentTimeMillis();
        long intervalEndTime = 0;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        if(timeIntervalSelected.equals("Current Week")){
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            intervalEndTime = cal.getTimeInMillis();
        }else if(timeIntervalSelected.equals("Current Month")){
            cal.set(Calendar.DAY_OF_MONTH, 1);
            intervalEndTime = cal.getTimeInMillis();
        }else{
            cal.set(Calendar.DAY_OF_YEAR, 1);
            intervalEndTime = cal.getTimeInMillis();
        }

        this.start = new Date(intervalStartTime);
        this.end = new Date(intervalEndTime);
    }

    private StringBuilder createManagerReport(){
        return null;
    }
}
