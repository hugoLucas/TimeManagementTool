/**
 * Created by Hugo Lucas on 10/29/2016.
 */
public class EmployeeProject {

    private String projectName;
    private int projectID;

    public EmployeeProject(String projName, int projID){
        this.projectName = projName;
        this.projectID = projID;
    }

    public String getProjectName(){
        return this.projectName;
    }

    public int getProjectID() {
        return this.projectID;
    }
}
