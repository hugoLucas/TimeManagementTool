/**
 * Created by Hugo Lucas on 10/29/2016.
 */
/**
 * This objects holds data (name and ID) to represent a project that a developer is working on.
 */
public class EmployeeProject
{
    //String varaible representing the name of the project.
    private String projectName;
    //String variable representing the ID of the project.
    private int projectID;

    /**
     * This is the constructor that initializes the new object being made by setting the project name and its ID.
     *
     * @param projName
     * @param projID
     */
    public EmployeeProject(String projName, int projID){
        this.projectName = projName;
        this.projectID = projID;
    }

    /**
     * This function returns the name of the project as a String.
     *
     * @return String
     */
    public String getProjectName(){
        return this.projectName;
    }

    /**
     * This function returns the ID of the project as a String.
     *
     * @return String
     */
    public int getProjectID() {
        return this.projectID;
    }

}
