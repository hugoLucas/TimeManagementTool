import java.sql.Date;
import java.sql.Time;

/**
 * Created by Hugo Lucas on 10/29/2016.
 */
public class EmployeeLog {

    private Time clockIn;
    private Time clockOut;
    private Date logDate;
    private int taskID;

    public EmployeeLog(Time ci, Time co, Date ld, int id){
        this.clockIn = ci;
        this.clockOut = co;
        this.logDate = ld;
        this.taskID = id;
    }

    public String printLog(){
        String out = String.format("IN: %s OUT: %s DATE: %s TASK: %d",clockIn.toString(), clockOut.toString(), logDate.toString(), taskID);

        return out;
    }
}
