import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Hugo on 11/10/2016.
 */
public class AddTask implements ActionListener {

    private JComboBox projectSelector;
    private JTextField estHoursInput;
    private JTextField taskNameInput;

    public AddTask(JComboBox projectSelector, JTextField taskNameInput, JTextField hoursInput, ArrayList<EmployeeProject> projs){
        this.projectSelector = projectSelector;
        this.estHoursInput = hoursInput;
        this.taskNameInput = taskNameInput;

        for(EmployeeProject p: projs)
            projectSelector.addItem(p.getProjectName());
    }

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
