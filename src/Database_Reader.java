import java.util.ArrayList;

/**
 * Created by Hugo Lucas on 10/29/2016.
 */
public interface Database_Reader {

    /**
     * Given a user's login information, method will return the employee_number of the
     * user if the login credentials are valid. Invalid credentials will result in negative
     * value.
     * @param input_password    password retrieved by GUI page
     * @param input_username    username retrieved by GUI page
     * @return  Employee number of user if login is valid, -1 if invalid, -2 if other errors
     */
    int[] login_user(String input_password, String input_username);

    /**
     * Given a valid employee identification number, method will return a list of
     * projects an employee can work on.
     *
     * @param employee_number   EmployeeID
     * @return  list of all projects an employee can work, can be used by GUI to populate
     * input selection menus
     */
    ArrayList<EmployeeProject> projectsAvailable(int employee_number);

    /**
     * Given a valid employee identification number, method will return a list of
     * tasks an employee can work on.
     *
     * @param employee_number   EmployeeID
     * @return  list of all tasks an employee can work, can be used by GUI to populate
     * input selection menus
     */
    ArrayList<EmployeeTask> tasksAvailable(int employee_number);

    /**
     * Helper method for employee times heet generation.
     *
     * @param list list of all employee whose work hours are needed
     * @return list of all employee's work hours
     */
    ArrayList<EmployeeLog> employeeWorkHours(ArrayList<Employee> list);

    /**
     * Generates a list of all employees currently working at the company. Used
     * by GUI in manager report generation.
     *
     * @return list of employees
     */
    ArrayList<Employee> allEmployees();

    /**
     * Determines if an employee should clock-in or out of work
     * @param employee_number employee identifier
     * @return 1 if employee is already clocked in , 0 if otherwise
     *//*
    int employeeWorkStatus(int employee_number);*/
}
