/**
 * Created by Hugo Lucas on 11/4/2016.
 */
public class userClockIn {

    private int employeeID;
    private int taskID;


    public userClockIn(int eID, int tID) {
        this.employeeID = eID;
        this.taskID = tID;
    }

    public void clockIn(){
        DB_Writer writer = new DB_Writer();
        writer.clockInUser(employeeID, taskID);
        writer.setWorkStatus(employeeID, 1);
    }
}
