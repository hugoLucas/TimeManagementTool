import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Hugo on 11/10/2016.
 */
public class AddTask implements ActionListener {

    private JComboBox projectSelector; /* User input field for specifying new task project assignment */
    private JTextField estHoursInput; /* User input field to enter new task estimated man hours needed for completion */
    private JTextField taskNameInput; /* User input field to enter new task name */

    /**
     * Saves references to user input components and populates the project dropdown menu.
     *
     * @param projectSelector   component user will select project assignment with
     * @param taskNameInput     component user will specify new task name with
     * @param hoursInput        component user will specify estimated man hours needed to complete task with
     * @param projs             container housing all projects in system
     */
    public AddTask(JComboBox projectSelector, JTextField taskNameInput, JTextField hoursInput, ArrayList<EmployeeProject> projs){
        this.projectSelector = projectSelector;
        this.estHoursInput = hoursInput;
        this.taskNameInput = taskNameInput;

        for(EmployeeProject p: projs)
            projectSelector.addItem(p.getProjectName());
    }

    /**
     * Triggerd when user presses create new task button. Extracts data from all GUI components
     * and passes information to database if the estimated time needed to complete the task is a
     * valid number. Does not validate any other field.
     *
     * @param e     Action which triggers method call
     */
    public void actionPerformed(ActionEvent e) {
        if(taskNameInput.getText().equals(""))
            JOptionPane.showMessageDialog(null, "Please enter a valid task name");
        else{
            try {
                String taskSelected = (String) taskNameInput.getText();
                int hoursSelected = Integer.parseInt(estHoursInput.getText());
                String projectSelected = (String) projectSelector.getSelectedItem();

                DB_Writer writer = new DB_Writer();
                writer.addTask(taskSelected, projectSelected, hoursSelected);
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number");
            }
        }
    }
}
