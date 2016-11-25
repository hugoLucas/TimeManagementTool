import java.util.ArrayList;

/**
 * Class handles the login process for a new user as well as few misc. functions
 * needed to populate the GUI during the initial login process.
 *
 * Created by Hugo Lucas on 11/18/2016.
 */
public class LoginUserData {

    private int employeeNumber;
    private ArrayList<Employee> list;

    /**
     * Sets default value of logged in user's information
     */
    public LoginUserData(){
        this.employeeNumber = -1;
        this.list = null;
    }

    /**
     * Logs the user in by making a call to the DB_Reader's login_user method
     * which checks if information provided by the user is valid.
     * employeeNumber = -1 if login information is invalid. employeeNumber = -2
     * if any other error has occurred. employeeNumber is set to the employee's
     * ID number if the login information is valid.
     *
     * @param       username the username inputted by the user
     * @param       password the password inputted by the user
     * @return      array containing employee number, work status, and manager status
     */
    public int[] authenticate(String username, String password){
        DB_Reader reader = new DB_Reader();
        int[] result = reader.login_user(password, username);

        if(result[0] == -2) {
            /* Exception thrown */
            this.employeeNumber = -2;
        }
        else if (result[0] == -1) {
            /* General query failure */
            this.employeeNumber = -1;
        }
        else{
            this.employeeNumber = result[0];
        }

        return result;
    }

    /**
     * Populates the drop down list with projects that the employee is
     * allowed to work on.
     *
     * @param       employeeNumber the employee's ID number
     * @return      an EmployeeProjectTaskMap object which holds different
     *              projects and tasks.
     */
    public EmployeeProjectTaskMap gatherMappingInformation(int employeeNumber){
        DB_Reader reader = new DB_Reader();
        return reader.projectTaskMap(employeeNumber);
    }

    public ArrayList<Employee> getAllEmployees(){
        DB_Reader reader = new DB_Reader();

        this.list = reader.allEmployees();
        return list;
    }

    /**
     * Gets the ID of the project and task the current user is working on.
     * @return an array of int holding the ID of the project and task of the
     * current user
     */
    public int [] taskAndProjectIDs(){
        DB_Reader reader = new DB_Reader();
        int IDs[] = reader.getCurrentEmployeeTaskAndProject(employeeNumber);

        return IDs;
    }

    /**
     * Used by a manager to get the ID number of an employee.
     * @param name the name of the employee
     * @return the ID number of an employee
     */
    public int getEmployeeNumberByName(String name) {
        if(list == null)
            this.list = getAllEmployees();

        for (Employee e : list)
            if (e.getName().equals(name))
                return e.getEmployeeNumber();
        return -1;
    }
}
