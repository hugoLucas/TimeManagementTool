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
    private int employeeID;

    public EmployeeLog(Time ci, Time co, Date ld, int id){
        this.clockIn = ci;
        this.clockOut = co;
        this.logDate = ld;
        this.taskID = id;

        this.employeeID = -1;
    }

    public EmployeeLog(Time ci, Time co, Date ld, int id, int eid){
        this.clockIn = ci;
        this.clockOut = co;
        this.logDate = ld;
        this.taskID = id;

        this.employeeID = eid;
    }

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
}
