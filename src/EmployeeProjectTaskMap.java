
import java.util.ArrayList;

/**
 * This object is a representation of one of the tables located in the database.
 * The goal of this class is provide a simple way of determining what tasks belong
 * to what project and what identification number is associated with a given project
 * name or task name.
 *
 * Created by Hugo Lucas on 11/3/2016.
 */
public class EmployeeProjectTaskMap {

    private ArrayList<EmployeeProject> projects;        /* list of all employee projects in database */
    private ArrayList<EmployeeTask> tasks;              /* list of all employee tasks in database */
    private ArrayList<Integer> taskIndexToProject;      /* list mapping task list to project list */

    /**
     * Constructor initializes all private fields to empty lists
     */
    public EmployeeProjectTaskMap(){
        this.projects = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.taskIndexToProject = new ArrayList<>();
    }

    /**
     * Given an employee task and project, method adds task to task list also adds project to project list
     * only if the project is not currently in the project list already. Takes list index of project and adds
     * it to map to create relationship between newly added task and project.
     *
     * @param p     the employee project to be added or bound to task
     * @param t     the employee task to add and be bound to project
     */
    public void addMapping(EmployeeProject p, EmployeeTask t){
        tasks.add(t);

        boolean addToProjects = true;
        int projectIndex = 0;
        for (EmployeeProject proj : projects) {
            projectIndex++;
            if (proj.getProjectName().equals(p.getProjectName())) {
                addToProjects = false;
                projectIndex--;
                break;
            }
        }
        if (addToProjects) {
            projects.add(p);
            taskIndexToProject.add(projects.size() - 1);
        }else
            taskIndexToProject.add(projectIndex);
    }

    /**
     * Returns project list
     *
     * @return      ArrayList of EmployeeProject objects
     */
    public ArrayList<EmployeeProject> getProjects(){
        return this.projects;
    }

    /**
     * Given a project name, method compiles all tasks associated with that
     * project
     *
     * @param projectName       String name of project
     * @return                  ArrayList of EmployeeTask objects bound to project
     */
    public ArrayList<EmployeeTask> getProjectTasks(String projectName){
        ArrayList<EmployeeTask> taskList = new ArrayList<>();

        int index = 0;
        for(EmployeeProject p: projects)
            if(p.getProjectName().equals(projectName))
                break;
            else
                index++;

        for(int i = 0; i < taskIndexToProject.size(); i ++)
            if(taskIndexToProject.get(i) == index)
                taskList.add(tasks.get(i));

        return taskList;
    }

    /**
     * Retrieves task identification number using task name by searching task list
     *
     * @param taskName      name of task whose identification number is needed
     * @return              task identification number
     */
    public int getTaskID(String taskName){
        for(EmployeeTask tk: this.tasks)
            if(tk.getTaskName().equals(taskName))
                return tk.getTaskID();
        return -1;
    }

    /**
     * Retrieves project identification number using project name by searching project list
     *
     * @param projectName      name of project whose identification number is needed
     * @return                 project identification number
     */
    public int getProjectID(String projectName){
        for(EmployeeProject p: this.projects)
            if(p.getProjectName().equals(projectName))
                return p.getProjectID();
        return -1;
    }

    /**
     * Retrieves task name using task identification number by searching task list
     *
     * @param tkID          identification number of task whose name is needed
     * @return              task name String
     */
    public String getTaskName(int tkID){
        for(EmployeeTask tk: this.tasks)
            if(tk.getTaskID() == tkID)
                return tk.getTaskName();
        return null;
    }

    /**
     * Retrieves project name using task identification number by searching project list
     *
     * @param pjID          identification number of project whose name is needed
     * @return              project name String
     */
    public String getProjectName(int pjID){
        for(EmployeeProject p: this.projects)
            if(p.getProjectID() == pjID)
                return p.getProjectName();
        return null;
    }

    public ArrayList<Integer> getTaskIndexToProject(){
        return this.taskIndexToProject;
    }
}
