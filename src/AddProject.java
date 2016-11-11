import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Hugo on 11/10/2016.
 */
public class AddProject implements ActionListener {

    private JTextField projectNameField;

    public AddProject(JTextField projNameField){
        this.projectNameField = projNameField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(projectNameField.getText().equals(""))
            JOptionPane.showMessageDialog(null, "Please enter a valid project name");
        else{
            String newProjectName = projectNameField.getText();

            DB_Writer writer = new DB_Writer();
            writer.addProject(newProjectName);
        }
    }
}
