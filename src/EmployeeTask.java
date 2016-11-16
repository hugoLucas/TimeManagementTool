/**
 * Created by Hugo Lucas on 10/29/2016.
 */
public class EmployeeTask {

    private String taskName;    /* Name of task */
    private int taskID;         /* Task identification number */

    /**
     * Binds parameter values to appropriate private field
     *
     * @param tName     new task name
     * @param tID       new task identification number
     */
    public EmployeeTask(String tName, int tID){
        this.taskName = tName;
        this.taskID = tID;
    }

    /**
     * Returns name of task object
     *
     * @return      String of task name
     */
    public String getTaskName(){
        return this.taskName;
    }

    /**
     * Returns identification number of task object
     *
     * @return      int of task identification number
     */
    public int getTaskID() {
        return this.taskID;
    }
}

