/**
 * Class handles the transfer of data from the GUI input-fields to the database.
 * Invoked by the user when the "Clock-In" button is pressed in the Developer or
 * Manager Clock-In screens.
 *
 * Created by Hugo Lucas on 11/4/2016.
 */

public class ClockInUser {

    private int employeeID; /* Employee ID of user clocking in*/
    private int taskID;     /* Task ID of task user is going to work on */

    /**
     * Constructor takes in references to the data extracted from the input fields
     * and stores the information.
     *
     * @param eID       Employee ID of current user
     * @param tID       Task ID of task selected to work on
     */
    public ClockInUser(int eID, int tID) {
        this.employeeID = eID;
        this.taskID = tID;
    }

    /**
     *  Creates a DB writing object and uses it to edit the database to reflect
     *  the current employee is now working on the given task. Also sets the work
     *  status of the employee to 1 indicating the employee is currently busy
     *  working on a task and will eventually need to clock-out.
     *
     *  @return     TRUE if clock-in was successful
     *              FALSE otherwise
     */
    public boolean clockIn(){
        if(this.employeeID > 0 && this.taskID > 0) {
            DB_Writer writer = new DB_Writer();
            if (writer.clockInUser(employeeID, taskID))
                return writer.setWorkStatus(employeeID, 1);
        }
        return false;
    }
}

