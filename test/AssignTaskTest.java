import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Hugo on 11/22/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AssignTask.class)
public class AssignTaskTest {

    private AssignTask mockedObject;

    private DB_Writer testWriter;

    private ArrayList<EmployeeTask> testTaskList;
    private ArrayList<Employee> testEmpList;

    private String testEmployeeName;
    private String testTaskName;

    @Before
    public void setUp() throws Exception {
        this.mockedObject = mock(AssignTask.class);

        this.testWriter = mock(DB_Writer.class);

        this.testEmployeeName = "EMP_NAME";
        this.testTaskName = "TSK_NAME";

        this.testEmpList = new ArrayList<>();
        this.testEmpList.add(new Employee("EMP_NAME", "LAST_NAME", 1));

        this.testTaskList = new ArrayList<>();
        this.testTaskList.add(new EmployeeTask("TSK_NAME", 1));
    }

    @After
    public void tearDown() throws Exception {
        this.testWriter = null;

        this.testEmployeeName = null;
        this.testTaskName = null;
    }

    @Test
    public void validInputsReturnTrue() throws Exception {
        when(this.testWriter.assignTaskToEmployee(anyInt(), anyInt())).
                thenReturn(Boolean.TRUE);
        whenNew(DB_Writer.class).withAnyArguments().thenReturn(this.testWriter);

        when(this.mockedObject.getEmployeeID(anyString(), anyObject())).thenReturn(1);
        when(this.mockedObject.getTaskID(anyString(), anyObject())).thenReturn(1);

        when(this.mockedObject.employeeList()).thenReturn(this.testEmpList);
        when(this.mockedObject.buildTaskList(anyInt())).thenReturn(this.testTaskList);
        when(this.mockedObject.addAssignmentToDatabase()).thenCallRealMethod();

        boolean result = this.mockedObject.addAssignmentToDatabase();

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void invalidTaskIDReturnFalse() throws Exception {
        when(this.mockedObject.getEmployeeID(anyString(), anyObject())).thenReturn(1);
        when(this.mockedObject.getTaskID(anyString(), anyObject())).thenReturn(-1);

        when(this.mockedObject.employeeList()).thenReturn(this.testEmpList);
        when(this.mockedObject.buildTaskList(anyInt())).thenReturn(this.testTaskList);
        when(this.mockedObject.addAssignmentToDatabase()).thenCallRealMethod();

        boolean result = this.mockedObject.addAssignmentToDatabase();

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void invalidEmployeeIDReturnFalse() throws Exception {
        when(this.mockedObject.getEmployeeID(anyString(), anyObject())).thenReturn(-1);
        when(this.mockedObject.getTaskID(anyString(), anyObject())).thenReturn(1);

        when(this.mockedObject.employeeList()).thenReturn(this.testEmpList);
        when(this.mockedObject.buildTaskList(anyInt())).thenReturn(this.testTaskList);
        when(this.mockedObject.addAssignmentToDatabase()).thenCallRealMethod();

        boolean result = this.mockedObject.addAssignmentToDatabase();
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void unableToBuildEmployeeListReturnFalse() throws Exception {
        whenNew(DB_Writer.class).withAnyArguments().thenReturn(this.testWriter);
        when(this.mockedObject.employeeList()).thenReturn(null);

        boolean result = this.mockedObject.addAssignmentToDatabase();
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void unableToBuildTaskListReturnFalse() throws Exception {
        whenNew(DB_Writer.class).withAnyArguments().thenReturn(this.testWriter);
        when(this.mockedObject.employeeList()).thenReturn(this.testEmpList);
        when(this.mockedObject.getEmployeeID(anyString(), anyObject())).thenReturn(1);
        when(this.mockedObject.buildTaskList(anyInt())).thenReturn(null);

        boolean result = this.mockedObject.addAssignmentToDatabase();
        assertEquals(Boolean.FALSE, result);
    }

}