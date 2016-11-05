/**
 * Created by Hugo Lucas on 11/4/2016.
 */
public class userClockOut {

    private int employeeID;
    private int taskID;

    public userClockOut(int eID, int tID){
        this.employeeID = eID;
        this.taskID = tID;
    }

    public void clockOut(){
        DB_Writer writer = new DB_Writer();
        writer.clockOutUser(employeeID, taskID);
        writer.setWorkStatus(employeeID, 0);
    }
}
