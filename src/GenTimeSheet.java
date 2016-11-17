import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Hugo Lucas on 11/5/2016.
 */
public class GenTimeSheet {

    private String timeIntervalSelected;    /* String representation of interval selected (month, week, year) */
    private int projectIDSeleted;           /* Integer identification number of the project selected */
    private int taskIDSelected;             /* Integer identification number of the task selected */
    private int employeeStatus;             /* 0 if not manager, 1 if manager */
    private int employeeID;                 /* Integer identification number of the employee selected */

    private Date start;                     /* Date object used to mark start of interval */
    private Date end;                       /* Date object used to mark end of interval */

    /**
     * Binds parameters to appropriate private field. Sets manager status to 0 if employee is developer and
     * 1 if employee is a manager. Initialises data objects to null.
     *
     * @param status                integer value signifying the employee's rank
     * @param employeeNumber        employee identification number
     * @param projID                project identification number
     * @param taskID                task identification number
     * @param timeInt               string representation of interval selected
     */
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

    /**
     * Calls different internal method depending on rank of calling object
     *
     * @return      StringBuilder object that contains time sheet
     */
    public void createReport(){
        this.intervalToDates();

        if(employeeStatus > 0)
            this.createManagerReport();
        else
            this.createDevReport();
    }

    /**
     * Creates a developer timesheet report. Passes internal fields to database reader which then generates a list
     * of EmployeeTimeLogs. Internal call to listToBuilder transforms list to StringBuilder object.
     *
     * @return      StringBuilder containing report
     */
    private void createDevReport(){
        DB_Reader reader = new DB_Reader();
        ArrayList<EmployeeLog> recs = reader.genEmployeeTimeSheet(employeeID, projectIDSeleted, taskIDSelected, start, end);

        TimeSheetReport rep = new TimeSheetReport(recs, false);
        rep.buildReport();
    }

    /**
     * Creates a manager timesheet report. Passes internal fields to database reader which then generates a list
     * of EmployeeTimeLogs. Internal call to listToBuilder transforms list to StringBuilder object.
     *
     * @return      StringBuilder containing report
     */
    private void createManagerReport(){
        DB_Reader reader = new DB_Reader();
        ArrayList<EmployeeLog> recs = reader.genManagerTimeSheet(employeeID, projectIDSeleted, taskIDSelected, start, end);

        TimeSheetReport rep = new TimeSheetReport(recs, true);
        rep.buildReport();
    }

    /**
     * Using a Calendar object, method converts a String representation of a time interval into
     * two Date objects which mark the begining and end of that interval
     */
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
}
