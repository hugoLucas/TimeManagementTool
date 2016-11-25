import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Hugo Lucas on 11/24/2016.
 */
public class ProjectLineTest {

    private ProjectLine testObj;

    private int projectID;
    private int hoursWorked;
    private int estimatedCompletion;

    private String projectName;

    @Before
    public void setUp() throws Exception {
        this.projectID = 1;
        this.hoursWorked = 100;
        this.estimatedCompletion = 1000;

        this.projectName = "TMT";
    }

    @Test
    public void constructorAndSetterGeneralTest() throws Exception {
        this.testObj = new ProjectLine(this.projectID, this.projectName,
                this.hoursWorked, this.estimatedCompletion);

        assertEquals(this.projectName, this.testObj.getProjectName());
        assertEquals(this.hoursWorked, this.testObj.getHoursWorkedOnProject());
        assertEquals(this.estimatedCompletion, this.testObj.getEstimatedHoursToCompletion());
    }
}