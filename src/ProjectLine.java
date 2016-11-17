/**
 * Created by Hugo on 11/14/2016.
 */
public class ProjectLine {

    private int projectID;                      /* Project identification number */
    private String projectName;                 /* Project name */
    private int hoursWorkedOnProject;           /* Man hours spent on project */
    private int estimatedHoursToCompletion;     /* Estimated man hours needed to complete project */
    private int percentDone;                    /* Percent project is done based on man hours worked */


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
        this.percentDone = this.hoursWorkedOnProject/this.estimatedHoursToCompletion;
    }

    /**
     * Formats and returns private fields in String form
     *
     * @return      String summary of private fields
     */
    public String printReport(){
        StringBuilder ret = new StringBuilder();

        ret.append("Project Name: " + this.projectName);
        ret.append(" (" + this.projectID + ") => ");
        ret.append("Hours Worked: " + this.hoursWorkedOnProject);
        ret.append(", Estimated Completion Work-Hours: " + this.estimatedHoursToCompletion);
        if(this.percentDone <= 1)
            ret.append(", Percent Done: " + this.percentDone + "%");
        else
            ret.append(", Behind Schedule By: " + (this.percentDone-1) + "%");

        return ret.toString();
    }

}
