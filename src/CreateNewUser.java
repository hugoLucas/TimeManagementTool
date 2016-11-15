import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Hugo on 11/10/2016.
 */
public class CreateNewUser implements ActionListener {

    private JTextField firstNameField;          /* JTextField used by user to enter the new user's first name */
    private JTextField lastNameField;           /* JTextField used by user to enter the new user's last name */
    private JTextField dateHiredField;          /* JTextField used by user to enter the new user's hire date */
    private JTextField userNameField;           /* JTextField used by user to specify their login username */
    private JPasswordField passWordField;       /* JPasswordField used by user to specifiy their login password */

    /**
     * Stores references to GUI components used by user to specify their identifying information and their
     * desired login credentials
     *
     * @param firstName     Reference to GUI component used to specify the user's first name
     * @param lastName      Reference to GUI component used to specify the user's last name
     * @param dateHired     Reference to GUI component used to specify the user's date of hire
     * @param username      Reference to GUI component used to specify the user's desired username
     * @param password      Reference to GUI component used to specify the user's desired password
     */
    public CreateNewUser(JTextField firstName, JTextField lastName, JTextField dateHired, JTextField username, JPasswordField password ){
        this.firstNameField = firstName;
        this.lastNameField = lastName;
        this.dateHiredField = dateHired;
        this.userNameField = username;
        this.passWordField = password;
    }

    /**
     * Action listener triggered by the create login button. Extracts information from user input fields and passes
     * that information to a database writer object if non of the user input fields are empty. If the database writer
     * determines the user already exists or if the new user was created, method generates an appropriate pop-up
     * message to alert the user of the result.
     *
     * @param e     Action which triggered the method call
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String firstName = this.firstNameField.getText();
        String lastName = this.lastNameField.getText();
        Date dateHired = extractDate();
        String userName = this.userNameField.getText();
        String password = String.valueOf(this.passWordField.getPassword());

        if(firstName.equals("") || lastName.equals("") || dateHired == null || userName.equals("") || password.equals(""))
            JOptionPane.showMessageDialog(null, "Please enter all the required information");
        else{
            DB_Writer writer = new DB_Writer();
            int res = writer.createLogin(firstName, lastName, dateHired, userName, password);

            if(res > 0)
                JOptionPane.showMessageDialog(null, "Login created! Login using the main screen to continue");
            else
                JOptionPane.showMessageDialog(null, "User already exists! Please login using the main screen");
        }
    }

    /**
     * Given a date String of the format Month-Day-Year, this method turns that String into a java.sql.Date object
     * in order to pass that information to the database writer. If the transformation fails the method returns null
     *
     * @return java.sql.Date object if String is valid, null if not.
     */
    public Date extractDate(){
        Date toRet = null;

        if(!this.dateHiredField.getText().equals("")){
            String dateEntered = this.dateHiredField.getText();
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            try {
                long selectedTimeInMillis = df.parse(dateEntered).getTime();
                toRet = new Date(selectedTimeInMillis);
            } catch (ParseException e) {
                return null;
            }
        }

        return toRet;
    }
}
