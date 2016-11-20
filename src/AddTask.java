import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Hugo on 11/10/2016.
 */
public class AddTask {

    private String projectSelector; /* User input field for specifying new task project assignment */
    private int estHoursInput; /* User input field to enter new task estimated man hours needed for completion */
    private String taskNameInput; /* User input field to enter new task name */

    /**
     * Saves references to user input components and populates the project dropdown menu.
     *
     * @param projectSelector   component user will select project assignment with
     * @param taskNameInput     component user will specify new task name with
     * @param hoursInput        component user will specify estimated man hours needed to complete task with
     */
    public AddTask(String projectSelector, String taskNameInput, int hoursInput){
        this.projectSelector = projectSelector;
        this.estHoursInput = hoursInput;
        this.taskNameInput = taskNameInput;
    }

    /**
     * Triggerd when user presses create new task button. Extracts data from all GUI components
     * and passes information to database if the estimated time needed to complete the task is a
     * valid number. Does not validate any other field.
     *
     */
    public boolean addTaskToDatabase() {
        DB_Writer writer = new DB_Writer();
        return writer.addTask(taskNameInput, projectSelector, estHoursInput);
    }
}
