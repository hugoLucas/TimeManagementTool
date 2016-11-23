import java.sql.Date;
import java.sql.Time;

/**
 * This class is an object representation of a time log in the database. It contains
 * all the current information for a specific time log. ClockOut variable is null when
 * the employee is still currently working on the task.
 *
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
     * @return      Task ID of task employee worked on
     */
    public int getTaskID(){
        return this.taskID;
    }

    /**
     * Simple getter method
     *
     * @return     Employee ID of employee
     */
    public int getEmployeeID(){
        return this.employeeID;
    }
}
