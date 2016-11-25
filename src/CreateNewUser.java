import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This class handles the creation of a new user login. Once an employee is added to
 * the system, they cannot login as they do not have a valid username and password.
 * This class is invoked whenever such an employee tries to log in to the system for
 * the first time. Using their identifying information, they are able to create
 * a username and password of their choosing. If a non-new employee tries to use this
 * class they will be rejected as they already have a username and password.
 *
 * Created by Hugo on 11/10/2016.
 */
public class CreateNewUser {

    private String firstName;          /* New user's first name */
    private String lastName;           /* New user's last name */
    private String dateHired;          /* New user's hire date */
    private String userName;           /* Login username */
    private String passWord;           /* Login password */

    /**
     * Stores references to text from GUI components used by user to specify their identifying information and their
     * desired login credentials
     *
     * @param firstName     first name
     * @param lastName      last name
     * @param dateHired     date of hire
     * @param username      desired username
     * @param password      desired password
     */
    public CreateNewUser(String firstName, String lastName, String dateHired, String username, String password ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateHired = dateHired;
        this.userName = username;
        this.passWord = password;
    }

    /**
     * Passes information to a database writer object if non of the user input fields are empty.
     * If the database writer determines the user already exists or if the new user was created,
     * method generates an appropriate pop-up message to alert the user of the result.
     */
    public boolean createNewLogin () {
        Date dateOfHire = this.extractDate(dateHired);
        if (dateOfHire != null) {
            DB_Writer writer = new DB_Writer();
            boolean result = writer.createLogin(firstName, lastName, dateOfHire, userName,
                    passWord);

            return result;
        }

        return false;
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
