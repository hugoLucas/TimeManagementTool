import java.sql.Date;
import java.sql.Time;

/**
 * Created by Hugo Lucas on 10/29/2016.
 */
public class EmployeeLog {

    private Time clockIn;       /* Time log was created */
    private Time clockOut;      /* Time log ended */
    private Date logDate;       /* Date log was created */
    private int taskID;         /* Task log is associated with */
    private int employeeID;     /* Employee identification number of employee who created the log*/

    /**
     * Creates an EmployeeLog with the given parameters.
     * The employee ID is set to -1.
     * @param ci time employee has clocked in
     * @param co time the user has clocked out
     * @param ld the date the task was worked on my employee
     * @param id the ID of the task employee worked on
     */
    public EmployeeLog(Time ci, Time co, Date ld, int id){
        this.clockIn = ci;
        this.clockOut = co;
        this.logDate = ld;
        this.taskID = id;

        this.employeeID = -1;
    }

    /**
     * Creates an EmployeeLog with the given parameters.
     * The employee ID number is given.
     * @param ci time employee has clocked in
     * @param co time the user has clocked out
     * @param ld the date the task was worked on my employee
     * @param id the ID of the task employee worked on
     * @param eid the ID number of the employee
     */
    public EmployeeLog(Time ci, Time co, Date ld, int id, int eid){
        this.clockIn = ci;
        this.clockOut = co;
        this.logDate = ld;
        this.taskID = id;

        this.employeeID = eid;
    }

    /**
     * Displays the time of clock in and clock out, the date, the task that was
     * worked on, and, if available, the ID number of the employee that worked
     * on the task.
     * @return the string to be printed out
     */
    public String printLog(){
        String out = null;
        String clockProxy = null;

        if(clockOut != null)
            clockProxy = clockOut.toString();
        else
            clockProxy = "PENDING";

        if(employeeID == -1)
            out = String.format("IN: %s OUT: %s DATE: %s TASK: %d",clockIn.toString(), clockProxy, logDate.toString(), taskID);
        else
            out = String.format("IN: %s OUT: %s DATE: %s TASK: %d EmployeeID: %d",clockIn.toString(), clockProxy, logDate.toString(), taskID, employeeID);
        return out;
    }

    /**
     * Simple getter method
     *
     * @return      Time object representing the start of the employee log
     */
    public Time getClockIn() {
        return this.clockIn;
    }

    /**
     * Simple getter method
     *
     * @return      Time object representing the end of the employee log
     */
    public Time getClockOut(){
        return this.clockOut;
    }

    /**
     * Simple getter method
     *
     * @return      Time object representing the date of the employee log
     */
    public Date getLogDate(){
        return this.logDate;
    }

    /**
     * Simple getter method
     *
     * @return      Time object representing the task associated with the employee log
     */
    public int getTaskID(){
        return this.taskID;
    }

    /**
     * Simple getter method
     *
     * @return      Time object representing the employee associated with the employee log
     */
    public int getEmployeeID(){
        return this.employeeID;
    }
}
