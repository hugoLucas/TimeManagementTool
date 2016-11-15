/**
 * Created by Hugo on 11/14/2016.
 */
public class ProjectLine {

    private int projectID;
    private String projectName;
    private int hoursWorkedOnProject;
    private int estimatedHoursToCompletion;
    private int percentDone;

    public ProjectLine(int projectID, String projectName, int hoursWorked, int hourEstimate){
        this.projectID = projectID;
        this.projectName = projectName;
        this.hoursWorkedOnProject = hoursWorked;
        this.estimatedHoursToCompletion = hourEstimate;
        this.percentDone = this.hoursWorkedOnProject/this.estimatedHoursToCompletion;
    }

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
