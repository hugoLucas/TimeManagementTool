import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Hugo on 11/14/2016.
 */
public class AssignTask implements ActionListener {

    private JComboBox employeeSelector;
    private JComboBox taskSelector;
    private JButton assignTaskButton;
    private ArrayList<Employee> empList;
    private ArrayList<EmployeeTask> tskList;

    public AssignTask(JComboBox empSel, JComboBox tskSel, JButton assignBut){
        this.employeeSelector = empSel;
        this.taskSelector = tskSel;
        this.assignTaskButton = assignBut;

        this.assignTaskButton.addActionListener(new ActionListener() {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        DB_Reader reader = new DB_Reader();
        this.empList = reader.allEmployees();

        this.employeeSelector.removeAllItems();
        for(Employee emp: empList)
            this.employeeSelector.addItem(emp.getName());

        buildTaskList(empList.get(0).getEmployeeNumber(), reader);
    }

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

    private int getEmployeeID(String name){
        for(Employee e: this.empList)
            if(e.getName().equals(name))
                return e.getEmployeeNumber();
        return -1;
    }

    private int getTaskID(String name){
        for(EmployeeTask t: this.tskList)
            if(t.getTaskName().equals(name))
                return t.getTaskID();
        return -1;
    }
}
