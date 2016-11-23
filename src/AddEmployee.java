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

    private String firstName; /* User input for new Employee first name */
    private String lastName; /* User input for new Employee last name */
    private Date hireDate; /* User input for new Employee hire date */
    private int groupSelector; /* User input for new Employee rank (Developer or Manager) */


    /**
     * Default constructor which takes in references to the values of the new
     * employee's fields.
     *
     * @param fn    first name of the new employee
     * @param ln    last name of the new employee
     * @param hd    hire date of the new employee, format (MM/DD/YYYY)
     * @param gs    rank of the new employee (0 or 1)
     *
     */
    public AddEmployee(String fn, String ln, String hd, int gs){
        this.firstName = fn;
        this.lastName = ln;
        this.hireDate = this.extractDate(hd);
        this.groupSelector = gs;
    }

    /**
     * Called when user presses the add new employee button. Passes information to
     * database if the date entered is valid. Al checks if the other fields are
     * non-empty.
     *
     * @Return  1 if employee was added to database successfully
     *          0 if employee was not added due to some error
     *          -1 if employee had a non-valid field
     */
    public int addEmployeeToDatabase() {
        if(hireDate != null && !firstName.equals("") && !lastName.equals("") &&
                (groupSelector == 0 || groupSelector == 1)) {
            DB_Writer writer = new DB_Writer();

            boolean result = writer.addEmployee(firstName, lastName,
                    hireDate, groupSelector);

            if(result)
                return 1;
            return 0;
        }
        else
            return -1;
    }

    /**
     * If the hireDate isn't blank method parses the String date representation
     * into a valid java.sql.Date object. Verifies this object is a valid date
     * by comparing the new object's String representation to the old String
     * value. This is done to avoid errors when user supplies an invalid date
     * that is still parseable.
     *
     * @return  Date objected to be passed to database writer if valid, null
     *          otherwise.
     */
    private Date extractDate(String dateString){
        Date toRet = null;

        if (dateString != null) {
            if(!dateString.equals("")){
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    long selectedTimeInMillis = df.parse(dateString).getTime();
                    toRet = new Date(selectedTimeInMillis);
                } catch (ParseException e) {
                    return null;
                }

                String check = dateString.substring(6,10) + "-" + dateString.substring(0,2) +
                        "-" + dateString.substring(3,5);

                if(check.equals(toRet.toString()))
                    return toRet;
                return null;
            }
        }

        return toRet;
    }
}
