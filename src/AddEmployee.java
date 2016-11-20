import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This object is called whenever a Manager-level user accesses the Add Employee
 * panel of the System page and opts to enter the information for a new employee
 * and presses the Add New Employee Button. Handles the transfer to information
 * from GUI components to the appropriate database writer method which will add
 * the employee to the database.
 *
 * Created by Hugo Lucas on 11/9/2016.
 */

public class AddEmployee {

    private String firstName; /* User input field for new Employee first name */
    private String lastName; /* User input field for new Employee last name */
    private Date hireDate; /* User input field for new Employee hire date */
    private int groupSelector; /* User input field for new Employee rank (Developer or Manager) */


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
    public AddEmployee(String fn, String ln, String hd, int gs){
        this.firstName = fn;
        this.lastName = ln;
        this.hireDate = this.extractDate(hd);
        this.groupSelector = gs;
    }

    /**
     * Triggered when user presses the add new employee button. Extracts user selections
     * from fields and passes information to database if the date entered is valid. No checks
     * are done on the other fields.
     */
    public int addEmployeeToDatabase() {
        DB_Writer writer = new DB_Writer();

        if(hireDate != null) {
            boolean result = writer.addEmployee(firstName, lastName,
                    hireDate, groupSelector);
            if(result)
                return 1;
            else
                return 0;
        }
        else
            return -1;
    }

    /**
     * If the hireDate isn't blank method parses the String date representation
     * into a valid java.sql.Date object.
     *
     * @return Date objected to be passed to database writer
     *
     */
    private Date extractDate(String dateString){
        Date toRet = null;

        if(!dateString.equals("")){
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            try {
                long selectedTimeInMillis = df.parse(dateString).getTime();
                toRet = new Date(selectedTimeInMillis);
            } catch (ParseException e) {
                return null;
            }
        }

        return toRet;
    }
}
