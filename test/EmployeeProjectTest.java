import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Hugo Lucas on 11/23/2016.
 */
public class EmployeeProjectTest {

    @Test
    public void generalTestOfConstructorAndGetter() throws Exception {
        String projectName = "TMT";
        int projectID = 100;

        EmployeeProject testObj = new EmployeeProject(projectName, projectID);
        assertEquals(projectID, testObj.getProjectID());
        assertEquals(projectName, testObj.getProjectName());
    }
}