
import java.util.ArrayList;

/**
 * Created by Hugo Lucas on 11/3/2016.
 */
public class userLogIn {

    private int workStatus;
    private int employeeNumber;
    private int managerStatus;
    private ArrayList<Employee> list;

    /**
     * Sets default value of logged in user's information
     */
    public userLogIn(){
        this.workStatus = -1;
        this.employeeNumber = -1;
        this.managerStatus = 0;
        this.list = null;
    }

    /**
     * Logs the user in by making a call to the DB_Reader's login_user method
     * which checks if information provided by the user is valid.
     * employeeNumber = -1 if login information is invalid. employeeNumber = -2
     * if any other error has occurred. employeeNumber is set to the employee's
     * ID number if the login information is valid.
     * @param username the username inputted by the user
     * @param password the password inputted by the user
     */
    public void login(String username, String password){
        DB_Reader reader = new DB_Reader();

        int[] loginResult = reader.login_user(password, username);

        if(loginResult[0] == -2)
            employeeNumber = -2;
        else if (loginResult[0] == -1)
            employeeNumber = -1;
        else{
            employeeNumber = loginResult[0];
            workStatus = loginResult[1];
            managerStatus = loginResult[2];
        }
    }

    /**
     * Returns the information of the current user that is logged in.
     * @return an int array which holds an employee's ID number, work status,
     * and manager status
     */
    public int[] results(){
        int retArray[] = {this.employeeNumber, this.workStatus, this.managerStatus};
        return retArray;
    }

    /**
     * Populates the drop down list with projects that the employee is
     * allowed to work on.
     * @param num the employee's ID number
     * @return an EmployeeProjectTaskMap object which holds different
     * projects and tasks.
     */
    public EmployeeProjectTaskMap populateDropDown(int num){
        DB_Reader reader = new DB_Reader();
        EmployeeProjectTaskMap map = reader.projectTaskMap(num);

        return map;
    }

    /**
     * Gets the name of the projects mapped into the EmployeeProjectTaskMap
     * object and stores them into an ArrayList of Strings.
     * @param map holds projects that the employee is allowed to work on
     * @return a list of the names of all projects that the employee can
     * work on
     */
    public ArrayList<String> projectList(EmployeeProjectTaskMap map){
        ArrayList<EmployeeProject> p = map.getProjects();
        ArrayList<String> returnList = new ArrayList<>(p.size());

        for (EmployeeProject proj: p)
            returnList.add(proj.getProjectName());

        return returnList;
    }

    /**
     * Gets the name of the tasks that are associated with a project and stores
     * them into an ArrayList.
     * @param projName the name of the project
     * @param map holds tasks that the employee is allowed to work on
     * @return a list of the names of all tasks that is associated with a project
     */
    public ArrayList<String> tasksInProject(String projName, EmployeeProjectTaskMap map){
        ArrayList<String> taskList = new ArrayList<>();

        for(EmployeeTask t: map.getProjectTasks(projName))
            taskList.add(t.getTaskName());

        return taskList;
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
     * Used by a manager to get a list of all employees.
     * @return an ArrayList of all employees currently working in the company
     */
    public ArrayList<Employee> getAllEmployees(){
        DB_Reader reader = new DB_Reader();
        list = reader.allEmployees();
        return list;
    }

    /**
     * Used by a manager to get the ID number of an employee.
     * @param name the name of the employee
     * @return the ID number of an employee
     */
    public int getEmployeeNumberByName(String name){
        if (list != null)
            for(Employee e: list)
                if(e.getName().equals(name))
                    return e.getEmployeeNumber();
        return -1;
    }
}
