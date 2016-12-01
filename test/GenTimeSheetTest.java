import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by Hugo on 11/23/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(GenTimeSheet.class)
public class GenTimeSheetTest {

    private GenTimeSheet testObj;

    private String timeInterval;
    private String rankStatus;
    private int employeeID;
    private int projectID;
    private int taskID;


    @Before
    public void setUp() throws Exception {
        this.rankStatus = "Developer";
        this.timeInterval = "Current Week";
        this.employeeID = 1000;
        this.projectID = 2000;
        this.taskID = 2100;
    }

    @After
    public void tearDown() throws Exception {
        this.testObj = null;

        this.timeInterval = null;
        this.rankStatus = null;
        this.employeeID = -1;
        this.projectID = -1;
        this.taskID = -1;
    }

    /* Test will only work on 11/23/2016, edit assert to test on another day */
    @Test
    public void testIntervalCreationDates() throws Exception {
        DB_Reader mockReader = mock(DB_Reader.class);
        TimeSheetReport mockReporter = mock(TimeSheetReport.class);
        whenNew(DB_Reader.class).withAnyArguments().thenReturn(mockReader);
        when(mockReader.genEmployeeTimeSheet(anyInt(), anyInt(), anyInt(),
                anyObject(), anyObject())).thenReturn(null);
        whenNew(TimeSheetReport.class).withAnyArguments().thenReturn(mockReporter);
        doNothing().when(mockReporter).buildReport();

        this.testObj = new GenTimeSheet(this.rankStatus, this.employeeID,
                this.projectID, this.taskID, this.timeInterval);
        testObj.createReport();
        ArrayList<Object> results = testObj.returnAllFields();

        assertEquals("2016-11-20", results.get(6).toString());
        assertEquals("2016-11-24", results.get(5).toString());

        this.testObj = new GenTimeSheet(this.rankStatus, this.employeeID,
                this.projectID, this.taskID, "Current Month");
        testObj.createReport();
        results = testObj.returnAllFields();

        assertEquals("2016-11-01", results.get(6).toString());
        assertEquals("2016-11-24", results.get(5).toString());

        this.testObj = new GenTimeSheet(this.rankStatus, this.employeeID,
                this.projectID, this.taskID, "Current Year");
        testObj.createReport();
        results = testObj.returnAllFields();

        assertEquals("2016-01-01", results.get(6).toString());
        assertEquals("2016-11-24", results.get(5).toString());
    }

    @Test
    public void testRankStatus() throws Exception{
        DB_Reader mockReader = mock(DB_Reader.class);
        TimeSheetReport mockReporter = mock(TimeSheetReport.class);
        whenNew(DB_Reader.class).withAnyArguments().thenReturn(mockReader);
        when(mockReader.genEmployeeTimeSheet(anyInt(), anyInt(), anyInt(),
                anyObject(), anyObject())).thenReturn(null);
        whenNew(TimeSheetReport.class).withAnyArguments().thenReturn(mockReporter);
        doNothing().when(mockReporter).buildReport();

        this.testObj = new GenTimeSheet(this.rankStatus, this.employeeID,
                this.projectID, this.taskID, this.timeInterval);
        testObj.createReport();
        ArrayList<Object> results = testObj.returnAllFields();
        assertEquals(0, results.get(3));

        this.testObj = new GenTimeSheet("Manager", this.employeeID,
                this.projectID, this.taskID, this.timeInterval);
        testObj.createReport();
        results = testObj.returnAllFields();
        assertEquals(1, results.get(3));
    }
}