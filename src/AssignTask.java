import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Hugo on 11/14/2016.
 */
public class AssignTask {

    private String employeeSelected; /* User input field for specifying employee to assign task to */
    private String taskSelected; /* User input field for selecting task to assign */

    /**
     * Default constructor. Takes in references to GUI components user will input task assignment
     * data into. Attaches listener to button passed that extracts data from the components and passes
     * the information to the database. Attaches listener to employee selector in order to generate unique
     * task list for the employee.
     *
     * @param empSel        dropdown menu used to select employee to assign task to
     * @param tskSel        dropdown menu used to select task to assign
     */
    public AssignTask(String empSel, String tskSel){
        this.employeeSelected = empSel;
        this.taskSelected = tskSel;
    }

    public AssignTask(){};

    /**
     * Class listener that reacts to the pressing of the generate button. Creates and stores a list of employees
     * and possible tasks for the first employee. Populates the employee dropdown with the list of employees
     * and also populates the task dropdown with tasks the first employee in the list is not assigned to.
     *
     */
    public ArrayList<Employee> employeeList() {
        DB_Reader reader = new DB_Reader();
        return reader.allEmployees();
    }

    public ArrayList<EmployeeTask> buildTaskList(int employeeID){
        DB_Reader reader = new DB_Reader();
        return reader.unassignedTask(employeeID);
    }

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
    private int getEmployeeID(String name, ArrayList<Employee> empList){
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
    private int getTaskID(String name, ArrayList<EmployeeTask> tskList){
        for(EmployeeTask t: tskList)
            if(t.getTaskName().equals(name))
                return t.getTaskID();
        return -1;
    }

    /**
     * Triggered by pressing assign task button. Extracts information from components and
     * validates the taskID and employeeID. Once that's done it passes that information to
     * the database writer to alter the assignment tables.
     */
    public boolean addAssignmentToDatabase() {
        DB_Writer writer = new DB_Writer();
        ArrayList<Employee> empList = this.employeeList();
        int eID = getEmployeeID(employeeSelected, empList);

        ArrayList<EmployeeTask> tskList = this.buildTaskList(eID);
        int tID = getTaskID(taskSelected, tskList);

        return writer.assignTaskToEmployee(tID, eID);
    }
}
