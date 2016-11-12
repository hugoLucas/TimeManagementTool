import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Hugo Lucas on 11/10/2016.
 */
public class BindTask implements ActionListener {

    private JButton generateTasks;
    private JComboBox taskSelector;
    private JComboBox projectSelector;
    private EmployeeProjectTaskMap map;

    private ArrayList<EmployeeTask> taskList;

    public BindTask(JButton generateTasks, JComboBox taskSelector, JComboBox projectSelector, EmployeeProjectTaskMap map){
        this.generateTasks = generateTasks;
        this.taskSelector = taskSelector;
        this.projectSelector = projectSelector;
        this.map = map;

        this.generateTasks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskList = gatherUnassignedTasks();

                if (taskList != null) {
                    taskSelector.removeAllItems();
                    for(EmployeeTask t: taskList)
                        taskSelector.addItem(t.getTaskName());
                }
            }
        });

        for(EmployeeProject p: map.getProjects())
            projectSelector.addItem(p.getProjectName());
    }

    public ArrayList<EmployeeTask> gatherUnassignedTasks(){
        DB_Reader reader = new DB_Reader();
        return reader.orphanTasks();
    }

    public void actionPerformed(ActionEvent e) {
        if (this.taskList != null) {
            int projectID = map.getProjectID((String) projectSelector.getSelectedItem());
            int taskID = getTaskIDByName((String) taskSelector.getSelectedItem());
            DB_Writer writer = new DB_Writer();
            writer.assignTask(taskID, projectID);

            generateTasks.doClick();
        }else{
            JOptionPane.showMessageDialog(null, "Invalid Task Selection");
        }
    }

    public int getTaskIDByName(String taskName){
        for(EmployeeTask t: this.taskList)
            if(t.getTaskName().equals(taskName))
                return t.getTaskID();
        return -1;
    }
}
