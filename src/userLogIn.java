
import java.util.ArrayList;

/**
 * Created by Hugo Lucas on 11/3/2016.
 */
public class userLogIn {

    private int workStatus;
    private int employeeNumber;
    private int managerStatus;

    public userLogIn(){
        this.workStatus = -1;
        this.employeeNumber = -1;
        this.managerStatus = 0;
    }

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

    public int[] results(){
        int retArray[] = {this.employeeNumber, this.workStatus, this.managerStatus};
        return retArray;
    }

    public EmployeeProjectTaskMap populateDropDown(int num){
        DB_Reader reader = new DB_Reader();
        EmployeeProjectTaskMap map = reader.projectTaskMap(num);

        return map;
    }

    public ArrayList<String> projectList(EmployeeProjectTaskMap map){
        ArrayList<EmployeeProject> p = map.getProjects();
        ArrayList<String> returnList = new ArrayList<>(p.size());

        for (EmployeeProject proj: p)
            returnList.add(proj.getProjectName());

        return returnList;
    }

    public ArrayList<String> tasksInProject(String projName, EmployeeProjectTaskMap map){
        ArrayList<String> taskList = new ArrayList<>();

        for(EmployeeTask t: map.getProjectTasks(projName))
            taskList.add(t.getTaskName());

        return taskList;
    }

    public int [] taskAndProjectIDs(){
        DB_Reader reader = new DB_Reader();
        int IDs[] = reader.getCurrentEmployeeTaskAndProject(employeeNumber);

        return IDs;
    }

}
