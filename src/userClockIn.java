/**
 * Created by Hugo Lucas on 11/4/2016.
 */

/**
 * The userClockIn object allows users to store data representing when a
 * developer has officially clocked in to start working on a project.
 *
 */
public class userClockIn
{
    //Variable of type Int respresenting the ID of the employee working on the task.
    private int employeeID;
    //Variable of type Int representing the ID of the task.
    private int taskID;

    /**
     * userClockIn constructor reads in two integer variables (one representing the
     * employee's ID and the other the task's ID) and stores both of these for
     * later use.
     *
     * @param eID
     * @param tID
     */
    public userClockIn(int eID, int tID) {
        this.employeeID = eID;
        this.taskID = tID;
    }

    /**
     * The clockIn method initializes an instance of the object DB_Writer to
     * access the database. It then stores employeeID and taskID and clocks in
     * the user. It also changes the the status of the employee.
     */
    public void clockIn(){
        DB_Writer writer = new DB_Writer();
        writer.clockInUser(employeeID, taskID);
        writer.setWorkStatus(employeeID, 1);
    }
}

