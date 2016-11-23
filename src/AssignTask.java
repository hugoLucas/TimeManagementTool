import java.util.ArrayList;

/**
 * This class handles the process of assigning an employee a task to work on by
 * adding the assignment information to the database. This class is invoked when
 * the user selects the "Assign Task Button".
 *
 * Created by Hugo on 11/14/2016.
 */
public class AssignTask {

    private String employeeSelected; /* Employee to assign task to */
    private String taskSelected; /* Task to assign */

    /**
     * Takes in references to text in GUI components and
     * saves them for future use.
     *
     * @param empSel        employee to assign task to
     * @param tskSel        task to assign
     */
    public AssignTask(String empSel, String tskSel){
        this.employeeSelected = empSel;
        this.taskSelected = tskSel;
    }

    /**
     * Constructor used by GUI to access methods needed to populate screen which
     * invokes this class.
     */
    public AssignTask(){};

    /**
     * Provides a list of all employees in the database.
     *
     * @return      an ArrayList of Employee objects containing information
     *              about every employee in database
     */
    public ArrayList<Employee> employeeList() {
        DB_Reader reader = new DB_Reader();
        return reader.allEmployees();
    }

    /**
     * Provides a list of all tasks an employee is currently not assigned to.
     *
     * @param employeeID    the employee ID used to select tasks with
     * @return              an ArrayList of Task objects containing information
     *                      about every task in database employee is not assigned to
     */
    public ArrayList<EmployeeTask> buildTaskList(int employeeID){
        DB_Reader reader = new DB_Reader();
        return reader.unassignedTask(employeeID);
    }

    /**
     * Provides the information needed to populate the task menu in the Assign Task
     * tab of the System menu.
     *
     * @param name      name of the employee who is currently selected
     * @return          a list of tasks not assigned to that employee
     */
    public ArrayList<EmployeeTask> dropDownTaskList(String name){
        DB_Reader reader = new DB_Reader();
        ArrayList<Employee> empList = this.employeeList();
        int employeeID = this.getEmployeeID(name, empList);

        return reader.unassignedTask(employeeID);
    }

    /**
     * Looks through employee list to match the given name to an identification number.
     *
     * @param name      employee name extracted from selector
     * @return          the employee identification number for the given employee
     */
    public int getEmployeeID(String name, ArrayList<Employee> empList){
        for(Employee e: empList)
            if(e.getName().equals(name))
                return e.getEmployeeNumber();
        return -1;
    }

    /**
     * Looks through task list to match given task name to an identification number.
     *
     * @param name      task name extracted from selector
     * @return          the task identification number for the given task
     */
    public int getTaskID(String name, ArrayList<EmployeeTask> tskList){
        for(EmployeeTask t: tskList)
            if(t.getTaskName().equals(name))
                return t.getTaskID();
        return -1;
    }

    /**
     * Triggered by pressing assign task button, validates the taskID and employeeID.
     * Then passes info to the database writer to alter the assignment tables.
     *
     * @return      TRUE if assignment was added
     *              FALSE otherwise
     */
    public boolean addAssignmentToDatabase() {
        DB_Writer writer = new DB_Writer();
        ArrayList<Employee> empList = this.employeeList();
        if (empList != null) {
            int eID = getEmployeeID(employeeSelected, empList);

            ArrayList<EmployeeTask> tskList = this.buildTaskList(eID);
            if (tskList != null) {
                int tID = getTaskID(taskSelected, tskList);

                if(eID < 0 || tID < 0)
                    return false;
                return writer.assignTaskToEmployee(tID, eID);
            }
        }

        return false;
    }
}
