import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Hugo Lucas on 11/10/2016.
 */
public class BindTask implements ActionListener {

    private JButton generateTasks;          /* Button used to update dropdowns */
    private JComboBox taskSelector;         /* Dropdown menu used to select task to bind */
    private JComboBox projectSelector;      /* Dropdown menu used to select project to bind task to */
    private EmployeeProjectTaskMap map;     /* Map which contains project to task mapping */

    private ArrayList<EmployeeTask> taskList;   /* List of all unassigned tasks gathered from database */

    /**
     * Default constructor used to gather references to GUI components needed to bind a task to a project.
     * Attaches listener to task selector that generates a new list given the project selected. Ensures only
     * tasks unassigned to a project are generated. Populates project selector.
     *
     * @param generateTasks         JButton which triggers bind tasks
     * @param taskSelector          ComboBox used to select task to bind
     * @param projectSelector       ComboBox used to select project to bind task to
     * @param map                   EmployeeTaskMap to find which tasks are unassigned to a given project
     */
    public BindTask(JButton generateTasks, JComboBox taskSelector, JComboBox projectSelector, EmployeeProjectTaskMap map){
        this.generateTasks = generateTasks;
        this.taskSelector = taskSelector;
        this.projectSelector = projectSelector;
        this.map = map;

        this.generateTasks.addActionListener(new ActionListener() {
            /**
             * Given a selected project, listener repopulates task selector with tasks not assigned to
             * project.
             *
             * @param e     Action which triggered method call
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                taskList = gatherUnassignedTasks();

                if (taskList != null) {
                    taskSelector.removeAllItems();
                    for(EmployeeTask t: taskList)
                        taskSelector.addItem(t.getTaskName());
                }else{
                    JOptionPane.showMessageDialog(null, "No Tasks Available to Bind!");
                }
            }
        });

        for(EmployeeProject p: map.getProjects())
            projectSelector.addItem(p.getProjectName());
    }

    /**
     *  Creates a database reader to extract all tasks in database not assigned to a project.
     *
     * @return  ArrayList containing all unassigned tasks in EmployeeTask format
     */
    public ArrayList<EmployeeTask> gatherUnassignedTasks(){
        DB_Reader reader = new DB_Reader();
        return reader.orphanTasks();
    }

    /**
     * Called when user selects the bind task button. Extracts data from dropdown menus and passes that
     * information to a database writer in order to edit the project task assignments. If no tasks are
     * unassigned, returns error message.
     *
     * @param e     Action which triggered method call
     */
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

    /**
     * Searches task list to find a task in the list whose name matches the input paramter.
     * Extracts identification number form that task.
     *
     * @param taskName      String representation of a task name
     * @return              identification number of matching task
     */
    public int getTaskIDByName(String taskName){
        for(EmployeeTask t: this.taskList)
            if(t.getTaskName().equals(taskName))
                return t.getTaskID();
        return -1;
    }
}
