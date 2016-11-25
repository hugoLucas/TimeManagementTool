
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Hugo on 11/23/2016.
 */


public class EmployeeTaskTest {

    private EmployeeTask testObj;
    private String taskName;
    private int taskID;

    @Before
    public void setUp() throws Exception {
        this.taskName = "NAME";
        this.taskID = 1000;
    }

    @After
    public void tearDown() throws Exception {
        this.taskName = null;
        this.taskID = -1;
    }

    @Test
    public void constructorAndGetterTest() throws Exception {
        this.testObj = new EmployeeTask(this.taskName, this.taskID);
        assertEquals(this.taskID, this.testObj.getTaskID());
        assertEquals(this.taskName, this.testObj.getTaskName());
    }

    @Test(expected = NullPointerException.class)
    public void constructorAndGetterTestWithNullValues() throws Exception {
        this.taskName = null;
        assertEquals(this.taskID, this.testObj.getTaskID());
        assertEquals(this.taskName, this.testObj.getTaskName());
    }
}