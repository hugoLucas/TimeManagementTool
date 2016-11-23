import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.Time;

import static org.junit.Assert.*;

/**
 * Created by Hugo Lucas on 11/23/2016.
 */
public class EmployeeLogTest {

    private Time clockIn;
    private Time clockOut;
    private Date logDate;
    private int taskID;
    private int employeeID;

    @Before
    public void setUp() throws Exception {
        clockIn = new Time(System.currentTimeMillis()-3600000);
        clockOut = new Time(System.currentTimeMillis());
        logDate = new Date(System.currentTimeMillis());
        taskID = 1;
        employeeID = 1;
    }

    @After
    public void tearDown() throws Exception {
        clockIn = null;
        clockOut = null;
        logDate = null;
        taskID = -1;
        employeeID = -1;
    }

    @Test
    public void testFirstConstructorAndGetterMethods(){
        EmployeeLog testObj = new EmployeeLog(this.clockIn, this.clockOut,
                this.logDate, this.taskID);

        assertEquals(this.clockIn, testObj.getClockIn());
        assertEquals(this.clockOut, testObj.getClockOut());
        assertEquals(this.logDate, testObj.getLogDate());
        assertEquals(this.taskID, testObj.getTaskID());
        assertEquals(-1, testObj.getEmployeeID());
    }

    @Test
    public void testSecondConstructorAndGetterMethods(){
        EmployeeLog testObj = new EmployeeLog(this.clockIn, this.clockOut,
                this.logDate, this.taskID, this.employeeID);

        assertEquals(this.clockIn, testObj.getClockIn());
        assertEquals(this.clockOut, testObj.getClockOut());
        assertEquals(this.logDate, testObj.getLogDate());
        assertEquals(this.taskID, testObj.getTaskID());
        assertEquals(this.employeeID, testObj.getEmployeeID());
    }
}