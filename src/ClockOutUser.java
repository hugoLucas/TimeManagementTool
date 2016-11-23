/**
 * This class handles the clock-out function of the time management system. This class
 * provides the information needed for the system to mark a user as clocked-out and no
 * longer working on a task. This class is invoked when the user presses the "Clock-Out"
 * button from the Manager or Developer Clock-Out screen.
 *
 * Created by Hugo Lucas on 11/4/2016.
 */

public class ClockOutUser {

    private int employeeID;     /* Employee ID of the user clocking out */
    private int taskID;         /* The task ID of the task the user was working on */

    /**
     * Constructor reads in the identification numbers needed by system to clock
     * a user out.
     *
     * @param eID   employee ID of the user that needs to clock-out
     * @param tID   task ID of the task the user was working on
     */
    public ClockOutUser(int eID, int tID){
        this.employeeID = eID;
        this.taskID = tID;
    }

    /**
     * Method creates a database writing object to alter database to reflect
     * user is no longer working on project. Verifies the IDs supplied by the
     * GUI are valid (non-negative) and then passes the information to the
     * database writing object. If any part of the clock-out process fails the
     * method returns false.
     *
     * @return      TRUE if clock-out is successful
     *              FALSE otherwise
     */
    public boolean clockOut(){
        if(this.employeeID > 0 && this.taskID > 0) {
            DB_Writer writer = new DB_Writer();
            if (writer.clockOutUser(employeeID, taskID))
                return writer.setWorkStatus(employeeID, 1);
        }
        return false;
    }
}
