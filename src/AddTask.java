import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Hugo on 11/10/2016.
 */
public class AddTask implements ActionListener {

    private JComboBox projectSelector;
    private JTextField taskNameInput;

    public AddTask(JComboBox projectSelector, JTextField taskNameInput, ArrayList<EmployeeProject> projs){
        this.projectSelector = projectSelector;
        this.taskNameInput = taskNameInput;

        for(EmployeeProject p: projs)
            projectSelector.addItem(p.getProjectName());
    }

    public void actionPerformed(ActionEvent e) {
        if(taskNameInput.getText().equals(""))
            JOptionPane.showMessageDialog(null, "Please enter a valid task name");
        else{
            String taskSelected = (String) taskNameInput.getText();
            String projectSelected = (String) projectSelector.getSelectedItem();

            DB_Writer writer = new DB_Writer();
            writer.addTask(taskSelected, projectSelected);
        }
    }
}
