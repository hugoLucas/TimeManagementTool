/**
 * Created by Hugo Lucas on 11/4/2016.
 */
/**
 * The userClockOut object allows users to store data representing when a
 * developer has officially clocked out to pause or finish working on a project.
 */
public class ClockOutUser {
    //Variable of type Int respresenting the ID of the employee working on the task.
    private int employeeID;
    //Variable of type Int representing the ID of the task.
    private int taskID;

    /**
     * userClockOut contrsuctor reads in two integer variables (one representing the
     * employee's ID and the other the task's ID) and stores both of these for
     * later use.
     *
     * @param eID
     * @param tID
     */
    public ClockOutUser(int eID, int tID){
        this.employeeID = eID;
        this.taskID = tID;
    }

    /**
     * The clockOut method initializes an instance of the object DB_Writer to
     * access the database. It then stores employeeID and taskID and clocks out
     * the user. It also changes the the status of the employee.
     */
    public void clockOut(){
        DB_Writer writer = new DB_Writer();
        writer.clockOutUser(employeeID, taskID);
        writer.setWorkStatus(employeeID, 0);
    }
}
