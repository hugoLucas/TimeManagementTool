import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Hugo Lucas on 11/22/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DB_Reader.class)
public class DB_ReaderTest extends BasicJDBCTestCaseAdapter {

    private MockConnection connection;
    private PreparedStatementResultSetHandler statementHandler;
    private MockResultSet resultOne;
    private MockResultSet resultTwo;

    @Before
    public void setUp() {
        this.connection = getJDBCMockObjectFactory().getMockConnection();
        this.statementHandler = connection.getPreparedStatementResultSetHandler();
        this.resultOne = statementHandler.createResultSet();
        this.resultTwo = statementHandler.createResultSet();
    }

    @Test
    public void userExistsInDatabaseReturnsContentsOfOnlyRow() throws Exception {
        this.resultOne.addColumn("password");
        this.resultOne.addColumn("EmployeeID");
        this.resultOne.addColumn("workstatus");
        this.resultOne.addColumn("manager");
        this.resultOne.addColumn("Username");

        String row [] = {"password", "1", "0", "1", "Hugo"};
        this.resultOne.addRow(row);
        statementHandler.prepareResultSet("SELECT employee", resultOne);

        DB_Reader testObj = new DB_Reader();
        int result [] = testObj.login_user("password","Hugo");

        assertEquals(1, result[0]);
        assertEquals(0, result[1]);
        assertEquals(1, result[2]);
    }

    @Test
    public void multipleUsersExistInDatabaseReturnsFirstUser() throws Exception {
        this.resultOne.addColumn("password");
        this.resultOne.addColumn("EmployeeID");
        this.resultOne.addColumn("workstatus");
        this.resultOne.addColumn("manager");
        this.resultOne.addColumn("Username");

        String rowOne [] = {"password", "1", "0", "1", "Hugo"};
        String rowTwo [] = {"password", "2", "1", "0", "Hugo"};
        this.resultOne.addRow(rowOne);
        this.resultOne.addRow(rowTwo);
        statementHandler.prepareResultSet("SELECT employee", resultOne);

        DB_Reader testObj = new DB_Reader();
        int result [] = testObj.login_user("password","Hugo");

        assertEquals(1, result[0]);
        assertEquals(0, result[1]);
        assertEquals(1, result[2]);
    }

    @Test
    public void noUserExistsInDatabaseReturnsNegativeArray() throws Exception {
        this.resultOne.addColumn("password");
        this.resultOne.addColumn("EmployeeID");
        this.resultOne.addColumn("workstatus");
        this.resultOne.addColumn("manager");
        this.resultOne.addColumn("Username");

        statementHandler.prepareResultSet("SELECT employee", resultOne);

        DB_Reader testObj = new DB_Reader();
        int result [] = testObj.login_user("password","Hugo");

        assertEquals(-1, result[0]);
        assertEquals(-1, result[1]);
        assertEquals(-1, result[2]);
    }

    @Test
    public void oneOnlyEmployeeReturnListOfSizeOne() throws Exception {
        this.resultOne.addColumn("EmployeeID");
        this.resultOne.addColumn("FirstName");
        this.resultOne.addColumn("LastName");
        this.resultOne.addColumn("EndDate");

        Object rowOne [] = {1, "Hugo", "Flores", null};
        resultOne.addRow(rowOne);
        statementHandler.prepareResultSet("SELECT EmployeeID", resultOne);

        DB_Reader testObj = new DB_Reader();
        ArrayList<Employee> result = testObj.allEmployees();

        assertEquals(1, result.size());
    }

    @Test
    public void multipleEmployeeReturnListOfSizeN() throws Exception {
        this.resultOne.addColumn("EmployeeID");
        this.resultOne.addColumn("FirstName");
        this.resultOne.addColumn("LastName");
        this.resultOne.addColumn("EndDate");

        Object rowOne [] = {1, "Hugo", "Flores", null};
        Object rowTwo [] = {2, "John", "Smith", null};
        Object rowThree [] = {3, "Jack", "0'Lantern", null};
        resultOne.addRow(rowOne);
        resultOne.addRow(rowTwo);
        resultOne.addRow(rowThree);
        statementHandler.prepareResultSet("SELECT EmployeeID", resultOne);

        DB_Reader testObj = new DB_Reader();
        ArrayList<Employee> result = testObj.allEmployees();

        assertEquals(3, result.size());
    }

    @Test
    public void noEmployeesReturnListOfSizeZero() throws Exception {
        statementHandler.prepareGlobalResultSet(null);
        DB_Reader testObj = new DB_Reader();
        ArrayList<Employee> result = testObj.allEmployees();

        assertEquals(0, result.size());
    }

    @Test
    public void allEmployeesFiredReturnListOfSizeZero() throws Exception {
        this.resultOne.addColumn("EmployeeID");
        this.resultOne.addColumn("FirstName");
        this.resultOne.addColumn("LastName");
        this.resultOne.addColumn("EndDate");

        Object rowOne [] = {1, "Hugo", "Flores", "2016-11-23"};
        Object rowTwo [] = {2, "John", "Smith", "2016-11-23"};
        Object rowThree [] = {3, "Jack", "0'Lantern", "2016-11-23"};
        resultOne.addRow(rowOne);
        resultOne.addRow(rowTwo);
        resultOne.addRow(rowThree);
        statementHandler.prepareResultSet("SELECT EmployeeID", resultOne);

        DB_Reader testObj = new DB_Reader();
        ArrayList<Employee> result = testObj.allEmployees();

        assertEquals(0, result.size());
    }

    @Test
    public void oneTaskPerProject() throws Exception {
        this.resultOne.addColumn("TaskID");
        this.resultOne.addColumn("TaskName");

        this.resultTwo.addColumn("ProjectName");
        this.resultTwo.addColumn("ProjectID");

        Object rowOne[] = {1, "Fix Bug"};
        Object rowTwo[] = {3, "Write Tests"};
        Object rowThree[] = {2, "Implement Feature"};
        resultOne.addRow(rowOne);
        resultOne.addRow(rowTwo);
        resultOne.addRow(rowThree);

        Object row1[] = {"TMT", 1};
        Object row2[] = {"ME", 2};
        Object row3[] = {"M", 3};
        resultTwo.addRow(row1);
        resultTwo.addRow(row2);
        resultTwo.addRow(row3);

        statementHandler.prepareResultSet("SELECT * FROM tasks", resultOne);
        statementHandler.prepareResultSet("SELECT * FROM projects", resultTwo);

        DB_Reader testObj = new DB_Reader();
        EmployeeProjectTaskMap result = testObj.projectTaskMap(1);

        assertEquals(3, result.getProjects().size());
    }

    @Test
    public void multipleTasksPerProject() throws Exception {
        this.resultOne.addColumn("TaskID");
        this.resultOne.addColumn("TaskName");

        this.resultTwo.addColumn("ProjectName");
        this.resultTwo.addColumn("ProjectID");

        Object rowOne[] = {1, "Fix Bug"};
        Object rowTwo[] = {3, "Write Tests"};
        Object rowThree[] = {2, "Implement Feature"};
        resultOne.addRow(rowOne);
        resultOne.addRow(rowTwo);
        resultOne.addRow(rowThree);

        Object row1[] = {"TMT", 1};
        resultTwo.addRow(row1);

        statementHandler.prepareResultSet("SELECT * FROM tasks", resultOne);
        statementHandler.prepareResultSet("SELECT * FROM projects", resultTwo);

        DB_Reader testObj = new DB_Reader();
        EmployeeProjectTaskMap result = testObj.projectTaskMap(1);

        assertEquals(1, result.getProjects().size());
        assertEquals(3, result.getProjectTasks(result.getProjects()
                .get(0).getProjectName()).size());
    }


    @Test
    public void oneMatchPerQueryReturnOneOne() throws Exception {
        this.resultOne.addColumn("TaskID");
        Object row11[] = {1};

        this.resultTwo.addColumn("ProjectID");
        Object row21[] = {1};

        resultOne.addRow(row11);
        resultTwo.addRow(row21);

        statementHandler.prepareResultSet("SELECT TaskID", resultOne);
        statementHandler.prepareResultSet("SELECT ProjectID", resultTwo);

        DB_Reader testObj = new DB_Reader();
        int [] result = testObj.getCurrentEmployeeTaskAndProject(1);

        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
    }

    @Test
    public void multipleMatchesPerQueryReturnOneOne() throws Exception {
        this.resultOne.addColumn("TaskID");
        Object row11[] = {1};
        Object row12[] = {100};
        Object row13[] = {1000};

        this.resultTwo.addColumn("ProjectID");
        Object row21[] = {1};
        Object row22[] = {100};
        Object row23[] = {1000};

        resultOne.addRow(row11);
        resultTwo.addRow(row21);
        resultOne.addRow(row12);
        resultTwo.addRow(row22);
        resultOne.addRow(row13);
        resultTwo.addRow(row23);

        statementHandler.prepareResultSet("SELECT TaskID", resultOne);
        statementHandler.prepareResultSet("SELECT ProjectID", resultTwo);

        DB_Reader testObj = new DB_Reader();
        int [] result = testObj.getCurrentEmployeeTaskAndProject(1);

        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
    }

    @Test
    public void noMatchesForQueryReturnZeroZero() throws Exception {
        statementHandler.prepareResultSet("SELECT TaskID", resultOne);
        statementHandler.prepareResultSet("SELECT ProjectID", resultTwo);

        DB_Reader testObj = new DB_Reader();
        int [] result = testObj.getCurrentEmployeeTaskAndProject(1);

        assertEquals(0, result[0]);
        assertEquals(0, result[1]);
    }
}