import org.apache.commons.lang.ObjectUtils;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Hugo Lucas on 11/23/2016.
 */

@PrepareForTest(AddEmployee.class)
public class EmployeeProjectTaskMapTest {


    @Test
    public void oneTaskPerProjectMapTest() throws Exception {
        EmployeeProject projObj1 = new EmployeeProject("TMT", 1000);
        EmployeeProject projObj2 = new EmployeeProject("AS", 2000);

        EmployeeTask tskObj1 = new EmployeeTask("DEBUG", 1001);
        EmployeeTask tskObj2 = new EmployeeTask("TEST", 2001);

        EmployeeProjectTaskMap testObj = new EmployeeProjectTaskMap();
        testObj.addMapping(projObj1, tskObj1);
        testObj.addMapping(projObj2, tskObj2);

        ArrayList<Integer> result = testObj.getTaskIndexToProject();
        assertEquals(new Integer(0), result.get(0));
        assertEquals(new Integer(1), result.get(1));
    }

    @Test
    public void multipleTasksPerProjectMapTest() throws Exception {
        EmployeeProject projObj1 = new EmployeeProject("TMT", 1000);

        EmployeeTask tskObj1 = new EmployeeTask("DEBUG", 1001);
        EmployeeTask tskObj2 = new EmployeeTask("TEST", 1002);
        EmployeeTask tskObj3 = new EmployeeTask("CODE", 1003);

        EmployeeProjectTaskMap testObj = new EmployeeProjectTaskMap();
        testObj.addMapping(projObj1, tskObj1);
        testObj.addMapping(projObj1, tskObj2);
        testObj.addMapping(projObj1, tskObj3);

        ArrayList<Integer> result = testObj.getTaskIndexToProject();
        assertEquals(new Integer(0), result.get(0));
        assertEquals(new Integer(0), result.get(1));
        assertEquals(new Integer(0), result.get(2));
    }

    @Test
    public void getTasksForProjectWithMultipleTasks() throws Exception {
        EmployeeProject projObj1 = new EmployeeProject("TMT", 1000);

        EmployeeTask tskObj1 = new EmployeeTask("DEBUG", 1001);
        EmployeeTask tskObj2 = new EmployeeTask("TEST", 1002);
        EmployeeTask tskObj3 = new EmployeeTask("CODE", 1003);
        EmployeeTask tskObj4 = new EmployeeTask("EAT", 1004);

        EmployeeProjectTaskMap testObj = new EmployeeProjectTaskMap();
        testObj.addMapping(projObj1, tskObj1);
        testObj.addMapping(projObj1, tskObj2);
        testObj.addMapping(projObj1, tskObj3);
        testObj.addMapping(projObj1, tskObj4);

        assertEquals(4, testObj.getProjectTasks("TMT").size());
    }

    @Test
    public void getTaskIDForExistingTask() throws Exception {
        EmployeeProject projObj1 = new EmployeeProject("TMT", 1000);
        EmployeeTask tskObj1 = new EmployeeTask("DEBUG", 1001);

        EmployeeProjectTaskMap testObj = new EmployeeProjectTaskMap();
        testObj.addMapping(projObj1, tskObj1);

        assertEquals(1001, testObj.getTaskID("DEBUG"));
    }

    @Test
    public void getTaskIDForNonExistingTask() throws Exception {
        EmployeeProjectTaskMap testObj = new EmployeeProjectTaskMap();
        assertEquals(-1, testObj.getTaskID("TASK"));
    }

    @Test
    public void getProjectIDForExistingProject() throws Exception {
        EmployeeProject projObj1 = new EmployeeProject("TMT", 1000);
        EmployeeTask tskObj1 = new EmployeeTask("DEBUG", 1001);

        EmployeeProjectTaskMap testObj = new EmployeeProjectTaskMap();
        testObj.addMapping(projObj1, tskObj1);

        assertEquals(1000, testObj.getProjectID("TMT"));
    }

    @Test
    public void getProjectIDForNonExistingProject() throws Exception {
        EmployeeProjectTaskMap testObj = new EmployeeProjectTaskMap();
        assertEquals(-1, testObj.getProjectID("TMT"));
    }

    @Test
    public void getTaskNameForExistingTask() throws Exception {
        EmployeeProject projObj1 = new EmployeeProject("TMT", 1000);
        EmployeeTask tskObj1 = new EmployeeTask("DEBUG", 1001);

        EmployeeProjectTaskMap testObj = new EmployeeProjectTaskMap();
        testObj.addMapping(projObj1, tskObj1);

        assertEquals("DEBUG", testObj.getTaskName(1001));
    }

    @Test
    public void getTaskNameForNonExistingTask() throws Exception {
        EmployeeProjectTaskMap testObj = new EmployeeProjectTaskMap();
        assertEquals(null, testObj.getTaskName(1001));
    }

    @Test
    public void getProjectNameForExistingProject() throws Exception {
        EmployeeProject projObj1 = new EmployeeProject("TMT", 1000);
        EmployeeTask tskObj1 = new EmployeeTask("DEBUG", 1001);

        EmployeeProjectTaskMap testObj = new EmployeeProjectTaskMap();
        testObj.addMapping(projObj1, tskObj1);

        assertEquals("TMT", testObj.getProjectName(1000));
    }

    @Test
    public void getProjectNameForNonExistingProject() throws Exception {
        EmployeeProjectTaskMap testObj = new EmployeeProjectTaskMap();
        assertEquals(null, testObj.getProjectName(1000));
    }
}