/**
 * Created by Hugo Lucas on 10/29/2016.
 */
public class EmployeeTask {

    private String taskName;
    private int taskID;

    public EmployeeTask(String tName, int tID){
        this.taskName = tName;
        this.taskID = tID;
    }

    public String getTaskName(){
        return this.taskName;
    }

    public int getTaskID() {
        return this.taskID;
    }
}

