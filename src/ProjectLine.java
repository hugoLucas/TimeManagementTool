/**
 * Object representation of one line in a time sheet report. Contains information regarding the amount
 * of time spent working on a project and all its tasks by every employee assigned to it.
 *
 * Created by Hugo on 11/14/2016.
 */
public class ProjectLine {

    private int projectID;                      /* Project identification number */
    private String projectName;                 /* Project name */
    private int hoursWorkedOnProject;           /* Man hours spent on project */
    private int estimatedHoursToCompletion;     /* Estimated man hours needed to complete project */


    /**
     * Binds parameters to appropriate private fields
     *
     * @param projectID         identification number of new project
     * @param projectName       name of new project
     * @param hoursWorked       hours spent on new project
     * @param hourEstimate      estimate of total hours needed to complete new project
     */
    public ProjectLine(int projectID, String projectName, int hoursWorked, int hourEstimate){
        this.projectID = projectID;
        this.projectName = projectName;
        this.hoursWorkedOnProject = hoursWorked;
        this.estimatedHoursToCompletion = hourEstimate;
    }

    public String getProjectName(){
        return this.projectName;
    }

    public int getHoursWorkedOnProject(){
        return this.hoursWorkedOnProject;
    }

    public int getEstimatedHoursToCompletion(){
        return this.estimatedHoursToCompletion;
    }
}
