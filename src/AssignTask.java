import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Hugo on 11/14/2016.
 */
public class AssignTask implements ActionListener {

    private JComboBox employeeSelector; /* User input field for specifying employee to assign task to */
    private JComboBox taskSelector; /* User input field for selecting task to assign */
    private JButton assignTaskButton; /* User input field to trigger updating of all fields */
    private ArrayList<Employee> empList; /* Container to track available employees */
    private ArrayList<EmployeeTask> tskList; /* Container to track tasks avialable to given employee */

    /**
     * Default constructor. Takes in references to GUI components user will input task assignment
     * data into. Attaches listener to button passed that extracts data from the components and passes
     * the information to the database. Attaches listener to employee selector in order to generate unique
     * task list for the employee.
     *
     * @param empSel        dropdown menu used to select employee to assign task to
     * @param tskSel        dropdown menu used to select task to assign
     * @param assignBut     button that trigger assignment operations
     */
    public AssignTask(JComboBox empSel, JComboBox tskSel, JButton assignBut){
        this.employeeSelector = empSel;
        this.taskSelector = tskSel;
        this.assignTaskButton = assignBut;

        this.assignTaskButton.addActionListener(new ActionListener() {
            /**
             * Triggered by pressing assign task button. Extracts information from components and
             * validates the taskID and employeeID. Once that's done it passes that information to
             * the database writer to alter the assignment tables.
             *
             * @param e     Action which triggered method call
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeSelected = (String) employeeSelector.getSelectedItem();
                String taskSelected = (String) taskSelector.getSelectedItem();

                DB_Writer writer = new DB_Writer();
                int eID = getEmployeeID(employeeSelected);
                int tID = getTaskID(taskSelected);

                if(eID > 0 && tID > 0)
                    writer.assignTaskToEmployee(tID, eID);
                else
                    JOptionPane.showMessageDialog(null, "Error! Invalid selection!");
            }
        });

        this.employeeSelector.addActionListener(new ActionListener() {
            /**
             * Extracts the employee selected and determines their ID. Using that information
             * the listener then repopulates the task dropdown in order for it to only show
             * tasks that the employee is not assigned to.
             *
             * @param e     Action which triggered method call
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeName = (String) employeeSelector.getSelectedItem();
                if(empList != null) {
                    int employeeID = getEmployeeID(employeeName);
                    buildTaskList(employeeID, new DB_Reader());
                }
            }
        });
    }

    /**
     * Class listener that reacts to the pressing of the generate button. Creates and stores a list of employees
     * and possible tasks for the first employee. Populates the employee dropdown with the list of employees
     * and also populates the task dropdown with tasks the first employee in the list is not assigned to.
     *
     * @param e     Action which triggered call
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        DB_Reader reader = new DB_Reader();
        this.empList = reader.allEmployees();

        this.employeeSelector.removeAllItems();
        for(Employee emp: empList)
            this.employeeSelector.addItem(emp.getName());

        buildTaskList(empList.get(0).getEmployeeNumber(), reader);
    }

    /**
     * Removes all current task from task selector and then adds tasks generated
     * by database reader. If no tasks are available for the given employee, displays
     * an error message to the user.
     *
     * @param employeeID    employee identification number of employee selected using selector
     * @param reader        a database reader object used to extract information from database
     */
    private void buildTaskList(int employeeID, DB_Reader reader){
        taskSelector.removeAllItems();
        this.tskList = reader.unassignedTask(employeeID);
        if(tskList.size() == 0)
            JOptionPane.showMessageDialog(null, "No tasks to assign, select another employee");
        else{
            for(EmployeeTask t: tskList)
                this.taskSelector.addItem(t.getTaskName());
        }
    }

    /**
     * Looks through employee list to match the given name to an identification number.
     *
     * @param name      employee name extracted from selector
     * @return          the employee identification number for the given employee
     */
    private int getEmployeeID(String name){
        for(Employee e: this.empList)
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
    private int getTaskID(String name){
        for(EmployeeTask t: this.tskList)
            if(t.getTaskName().equals(name))
                return t.getTaskID();
        return -1;
    }
}
