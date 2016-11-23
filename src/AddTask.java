/**
 * This class handles the transport of information from the GUI layer to the
 * database. Saves references to the data values supplied to the user and passes
 * them to the database writing object when all values are valid and the user
 * selects the "Add Task" button.
 *
 * Created by Hugo on 11/10/2016.
 */
public class AddTask {

    private String projectSelector; /* User input specifying new task project assignment */
    private int estHoursInput; /* User input for new task's estimated man hours needed for completion */
    private String taskNameInput; /* User input value for new task's name */

    /**
     * Saves references to the text/selection in the user input components.
     *
     * @param projectSelector   project assignment
     * @param taskNameInput     new task name
     * @param hoursInput        estimated man hours needed to complete task
     */
    public AddTask(String projectSelector, String taskNameInput, int hoursInput){
        this.projectSelector = projectSelector;
        this.estHoursInput = hoursInput;
        this.taskNameInput = taskNameInput;
    }

    /**
     * Invoked when user presses create new task button. Passes information to database
     * if the estimated time needed to complete the task is a valid number and
     * the other fields are non-empty.
     *
     * @return  TRUE if task was added to database
     *          FALSE if otherwise
     */
    public boolean addTaskToDatabase() {
        if(this.estHoursInput > 0 && !this.projectSelector.equals("")
                && !this.taskNameInput.equals("")) {
            DB_Writer writer = new DB_Writer();
            return writer.addTask(taskNameInput, projectSelector, estHoursInput);
        }

        return false;
    }
}
