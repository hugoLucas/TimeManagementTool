import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Hugo Lucas on 11/22/2016.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClockInUser.class)
public class ClockInUserTest {

    private DB_Writer testWriter;

    private int testEmployeeID;
    private int testTaskID;


    @Before
    public void setUp() throws Exception {
        this.testWriter = mock(DB_Writer.class);

        this.testEmployeeID = 1;
        this.testTaskID = 1;
    }

    @After
    public void tearDown() throws Exception {
        this.testWriter = null;

        this.testEmployeeID = -1;
        this.testTaskID = -1;
    }

    @Test
    public void correctInputReturnTrue() throws Exception {
        when(testWriter.clockInUser(anyInt(), anyInt())).
                thenReturn(Boolean.TRUE);
        when(testWriter.setWorkStatus(anyInt(), anyInt())).
                thenReturn(Boolean.TRUE);
        whenNew(DB_Writer.class).withNoArguments().thenReturn(this.testWriter);

        ClockInUser clockInUserTestObject = new ClockInUser(this.testEmployeeID,
                this.testTaskID);
        boolean result = clockInUserTestObject.clockIn();

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void invalidIDsSuppliedReturnFalse() throws Exception {
        this.testTaskID = -100;
        this.testEmployeeID = -200;

        ClockInUser clockInUserTestObject = new ClockInUser(this.testEmployeeID,
                this.testTaskID);
        boolean result = clockInUserTestObject.clockIn();
        assertEquals(Boolean.FALSE, result);

        this.testEmployeeID = 1;
        clockInUserTestObject = new ClockInUser(this.testEmployeeID,
                this.testTaskID);
        result = clockInUserTestObject.clockIn();
        assertEquals(Boolean.FALSE, result);

        this.testTaskID = 1;
        this.testEmployeeID = -1;
        clockInUserTestObject = new ClockInUser(this.testEmployeeID,
                this.testTaskID);
        result = clockInUserTestObject.clockIn();
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void errorWithClockInUserReturnFalse() throws Exception {
        when(testWriter.clockInUser(anyInt(), anyInt())).
                thenReturn(Boolean.FALSE);
        whenNew(DB_Writer.class).withNoArguments().thenReturn(this.testWriter);

        ClockInUser clockInUserTestObject = new ClockInUser(this.testEmployeeID,
                this.testTaskID);
        boolean result = clockInUserTestObject.clockIn();

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void errorWithSetWorkStatusReturnTrue() throws Exception {
        when(testWriter.clockInUser(anyInt(), anyInt())).
                thenReturn(Boolean.TRUE);
        when(testWriter.setWorkStatus(anyInt(), anyInt())).
                thenReturn(Boolean.FALSE);

        whenNew(DB_Writer.class).withNoArguments().thenReturn(this.testWriter);

        ClockInUser clockInUserTestObject = new ClockInUser(this.testEmployeeID,
                this.testTaskID);
        boolean result = clockInUserTestObject.clockIn();

        assertEquals(Boolean.FALSE, result);
    }
}