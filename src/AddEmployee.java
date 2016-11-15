import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Hugo Lucas on 11/9/2016.
 */

public class AddEmployee implements ActionListener {

    private JTextField firstName; /* User input field for new Employee first name */
    private JTextField lastName; /* User input field for new Employee last name */
    private JTextField hireDate; /* User input field for new Employee hire date */
    private JComboBox groupSelector; /* User input field for new Employee rank (Developer or Manager) */


    /**
     * Default constructor which takes in the components housing the properties
     * of the new employee to be added.
     *
     * @param fn    JTextField in which user inputs the first name of the new employee
     * @param ln    JTextField in which user inputs the last name of the new employee
     * @param hd    JTextField in which user inputs the hire date of the new employee
     * @param gs    JComboBox which allows the user to select the rank of the new employee
     *
     */
    public AddEmployee(JTextField fn, JTextField ln, JTextField hd, JComboBox gs/*, JList ps*/){
        this.firstName = fn;
        this.lastName = ln;
        this.hireDate = hd;
        this.groupSelector = gs;
    }

    /**
     * Triggered when user presses the add new employee button. Extracts user selections
     * from fields and passes information to database if the date entered is valid. No checks
     * are done on the other fields.
     *
     * @param e     Action which triggered method call
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String firstNameEntered = this.getEntry(this.firstName);
        String lastNameEntered = this.getEntry(this.lastName);
        Date startDateEntered = this.extractDate();
        int groupSelected = this.groupSelector.getSelectedIndex();

        DB_Writer writer = new DB_Writer();
        if(startDateEntered != null)
            writer.addEmployee(firstNameEntered, lastNameEntered, startDateEntered, groupSelected);
        else
            JOptionPane.showMessageDialog(null, "Invalid date entry, please try again");
    }

    /**
     * If the given JTextField has valid, non-blank data, this method extracts
     * that information.
     *
     * @param name  JTextField parameter
     * @return data captured from the text field
     *
     */
    private String getEntry (JTextField name){
        String toRet = null;
        if(!name.getText().equals(""))
            toRet = name.getText();

        return toRet;
    }

    /**
     * If the hireDate isn't blank method parses the String date representation
     * into a valid java.sql.Date object.
     *
     * @return Date objected to be passed to database writer
     *
     */
    public Date extractDate(){
        Date toRet = null;

        if(!this.hireDate.getText().equals("")){
            String dateEntered = this.hireDate.getText();
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
