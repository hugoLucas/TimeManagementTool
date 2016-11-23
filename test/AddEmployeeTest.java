import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by Hugo Lucas on 11/19/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AddEmployee.class)
public class AddEmployeeTest {
    private int O = 1;
    private int Z = 0;
    private int N = -1;

    private DB_Writer testWriter;

    private String testFirstNameInput;
    private String testLastNameInput;
    private String testDateInput;
    private int testGroupIndex;

    @Before
    public void setUp(){
        this.testWriter = mock(DB_Writer.class);

        this.testFirstNameInput = "Hugo";
        this.testLastNameInput = "Lucas";
        this.testDateInput = "11/21/2016";
        this.testGroupIndex = 0;
    }

    @Test
    public void properlyFormattedInputsShouldReturnOne() throws Exception {
        when(this.testWriter.addEmployee(anyString(), anyString(), anyObject(), anyInt())).thenReturn(Boolean.TRUE);
        PowerMockito.whenNew(DB_Writer.class).withNoArguments().thenReturn(this.testWriter);


        AddEmployee addEmployeeTestObj = new AddEmployee(this.testFirstNameInput,
                this.testLastNameInput, this.testDateInput, this.testGroupIndex);
        int testResult = addEmployeeTestObj.addEmployeeToDatabase();

        assertEquals(this.O, testResult);
    }

    @Test
    public void wordSuppliedInsteadOfDateShouldReturnNegativeOne() throws Exception {
        this.testDateInput = "generic_string";

        AddEmployee addEmployeeTestObj = new AddEmployee(this.testFirstNameInput,
                this.testLastNameInput, this.testDateInput, this.testGroupIndex);
        int testResult = addEmployeeTestObj.addEmployeeToDatabase();

        assertEquals(this.N, testResult);
    }

    @Test
    public void dateLeftEmptyShouldReturnNegativeOne() throws Exception {
        this.testDateInput = null;

        AddEmployee addEmployeeTestObj = new AddEmployee(this.testFirstNameInput,
                this.testLastNameInput, this.testDateInput, this.testGroupIndex);
        int testResult = addEmployeeTestObj.addEmployeeToDatabase();

        assertEquals(this.N, testResult);
    }

    @Test
    public void errorWithDatabaseObjectShouldReturnZero() throws Exception {
        when(this.testWriter.addEmployee(anyString(), anyString(), anyObject(), anyInt())).thenReturn(Boolean.FALSE);
        PowerMockito.whenNew(DB_Writer.class).withNoArguments().thenReturn(this.testWriter);


        AddEmployee addEmployeeTestObj = new AddEmployee(this.testFirstNameInput,
                this.testLastNameInput, this.testDateInput, this.testGroupIndex);
        int testResult = addEmployeeTestObj.addEmployeeToDatabase();

        assertEquals(this.Z, testResult);
    }

    @Test
    public void dateSuppliedIsIllogicalShouldReturnNegativeOne() throws Exception {
        this.testDateInput = "11/99/2016";

        AddEmployee addEmployeeTestObj = new AddEmployee(this.testFirstNameInput,
                this.testLastNameInput, this.testDateInput, this.testGroupIndex);
        int testResult = addEmployeeTestObj.addEmployeeToDatabase();

        assertEquals(this.N, testResult);
    }

    @Test
    public void groupSelectorNotInRangeShouldReturnNegativeOne () throws Exception {
        this.testGroupIndex = -1;

        AddEmployee addEmployeeTestObj = new AddEmployee(this.testFirstNameInput,
                this.testLastNameInput, this.testDateInput, this.testGroupIndex);
        int testResultOne = addEmployeeTestObj.addEmployeeToDatabase();

        this.testGroupIndex = 5;

        addEmployeeTestObj = new AddEmployee(this.testFirstNameInput,
                this.testLastNameInput, this.testDateInput, this.testGroupIndex);
        int testResultTwo = addEmployeeTestObj.addEmployeeToDatabase();

        assertEquals(this.N, testResultOne);
        assertEquals(this.N, testResultTwo);
    }

    @Test
    public void oneFieldLeftEmptyShouldReturnNegativeOne () throws Exception {
        AddEmployee addEmployeeTestObj = new AddEmployee("",
                this.testLastNameInput, this.testDateInput, this.testGroupIndex);
        int testResultOne = addEmployeeTestObj.addEmployeeToDatabase();

        addEmployeeTestObj = new AddEmployee(this.testFirstNameInput,
                "", this.testDateInput, this.testGroupIndex);
        int testResultTwo = addEmployeeTestObj.addEmployeeToDatabase();

        addEmployeeTestObj = new AddEmployee(this.testFirstNameInput,
                this.testLastNameInput, "", this.testGroupIndex);
        int testResultThree = addEmployeeTestObj.addEmployeeToDatabase();


        assertEquals(this.N, testResultOne);
        assertEquals(this.N, testResultTwo);
        assertEquals(this.N, testResultThree);
    }

    @After
    public void tearDown(){
        this.testWriter = null;

        this.testFirstNameInput = null;
        this.testLastNameInput = null;
        this.testDateInput = null;
        this.testGroupIndex = -1;
    }

}