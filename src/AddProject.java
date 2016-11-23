/**
 * This class is responsible for the addition of a new project to the central
 * database. This class takes in a reference to the GUI component used to enter
 * the new project's name and extracts that information when the user presses
 * the Create Project button. This information is then passed to a database
 * writing object which makes the addition to the database.
 *
 * Created by Hugo Lucas on 11/10/2016.
 */
public class AddProject {

    private String projectName; /* User input for new project name */

    /**
     * Saves reference to new project name.
     */
    public AddProject(String projName){
        this.projectName = projName;
    }

    /**
     * Triggered when user presses the add new project button. Passes information
     * over to database to handle the addition of the new project.
     *
     * @return TRUE when project was added successfully, FALSE otherwise.
     */
    public boolean addProjectToDatabase ( ) {
        if(this.projectName != null) {
            DB_Writer writer = new DB_Writer();
            return writer.addProject(this.projectName);
        }
        return false;
    }
}
