import java.util.ArrayList;

/**
 * Created by Hugo Lucas on 11/3/2016.
 */
public class EmployeeProjectTaskMap {

    private ArrayList<EmployeeProject> projects;
    private ArrayList<EmployeeTask> tasks;
    private ArrayList<Integer> taskIndexToProject;

    public EmployeeProjectTaskMap(){
        this.projects = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.taskIndexToProject = new ArrayList<>();
    }

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

    public ArrayList<EmployeeProject> getProjects(){
        return this.projects;
    }

    public ArrayList<EmployeeTask> getTasks(){
        return this.tasks;
    }

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

    public int getTaskID(String taskName){
        for(EmployeeTask tk: this.tasks)
            if(tk.getTaskName().equals(taskName))
                return tk.getTaskID();
        return -1;
    }

    public int getProjectID(String projectName){
        for(EmployeeProject p: this.projects)
            if(p.getProjectName().equals(projectName))
                return p.getProjectID();
        return -1;
    }

    public String getTaskName(int tkID){
        for(EmployeeTask tk: this.tasks)
            if(tk.getTaskID() == tkID)
                return tk.getTaskName();
        return null;
    }

    public String getProjectName(int pjID){
        for(EmployeeProject p: this.projects)
            if(p.getProjectID() == pjID)
                return p.getProjectName();
        return null;
    }
}
