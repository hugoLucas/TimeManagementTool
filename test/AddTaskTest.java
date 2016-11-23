import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.internal.BooleanSupplier;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Hugo on 11/22/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AddTask.class)
public class AddTaskTest {

    private DB_Writer testWriter;

    private String testProjectSelectedInput;
    private String testProjectNameInput;
    private int testHoursInput;

    @Before
    public void setUp() throws Exception {
        this.testWriter = mock(DB_Writer.class);

        this.testProjectSelectedInput = "TEST_PROJECT";
        this.testProjectNameInput = "TEST_NAME";
        this.testHoursInput = 100;
    }

    @After
    public void tearDown() throws Exception {
        this.testWriter = null;

        this.testProjectSelectedInput = null;
        this.testProjectNameInput = null;
        this.testHoursInput = -1;
    }

    @Test
    public void properlyFormattedInputReturnTrue() throws Exception {
        when(this.testWriter.addTask(anyString(), anyString(), anyInt()))
                .thenReturn(Boolean.TRUE);
        PowerMockito.whenNew(DB_Writer.class).withNoArguments()
                .thenReturn(this.testWriter);

        AddTask addTaskTestObject = new AddTask(this.testProjectSelectedInput,
                this.testProjectNameInput, this.testHoursInput);
        boolean result = addTaskTestObject.addTaskToDatabase();

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void oneEmptyFieldReturnFalse() throws Exception {
        AddTask addTaskTestObject = new AddTask("",  this.testProjectNameInput,
                this.testHoursInput);
        boolean result = addTaskTestObject.addTaskToDatabase();

        assertEquals(Boolean.FALSE, result);

        addTaskTestObject = new AddTask(this.testProjectSelectedInput, "",
                this.testHoursInput);
        result = addTaskTestObject.addTaskToDatabase();

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void negativeHoursRequiredReturnFalse() throws Exception {
        AddTask addTaskTestObject = new AddTask(this.testProjectSelectedInput,
                this.testProjectNameInput, -1);
        boolean result = addTaskTestObject.addTaskToDatabase();

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void databaseErrorReturnFalse() throws Exception {
        when(this.testWriter.addTask(anyString(), anyString(), anyInt()))
                .thenReturn(Boolean.FALSE);
        PowerMockito.whenNew(DB_Writer.class).withNoArguments()
                .thenReturn(this.testWriter);

        AddTask addTaskTestObject = new AddTask(this.testProjectSelectedInput,
                this.testProjectNameInput, this.testHoursInput);
        boolean result = addTaskTestObject.addTaskToDatabase();

        assertEquals(Boolean.FALSE, result);
    }

}