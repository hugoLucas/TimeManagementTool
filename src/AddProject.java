import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Hugo on 11/10/2016.
 */
public class AddProject implements ActionListener {

    private JTextField projectNameField; /* User input field for new project name */

    /**
     * Saves reference to component user will enter the new project name.
     *
     * @param projNameField     JTextField needed to enter new project name
     */
    public AddProject(JTextField projNameField){
        this.projectNameField = projNameField;
    }

    /**
     * Triggered when user presses the add new project button. Extarcts data from
     * saved JTextField and passes information to the database.
     *
     * @param e     Action which triggered method call
     */
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
