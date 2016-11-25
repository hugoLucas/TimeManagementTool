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

    private DB_Reader testObj;

    @Before
    public void setUp() {
        this.connection = getJDBCMockObjectFactory().getMockConnection();
        this.statementHandler = connection.getPreparedStatementResultSetHandler();
        this.resultOne = statementHandler.createResultSet();
        this.resultTwo = statementHandler.createResultSet();

        this.testObj = new DB_Reader();
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

        int result [] = testObj.login_user("password","Hugo");

        assertEquals(-1, result[0]);
        assertEquals(-1, result[1]);
        assertEquals(-1, result[2]);
    }

    @Test
    public void checkSQLStatementForLogin () throws Exception {
        statementHandler.prepareGlobalResultSet(this.resultOne);

        testObj.login_user("PW", "UN");
        ArrayList<String> result = testObj.returnStatements();

        assertEquals("SELECT employee.employeeID, employee.Manager, " +
                "employee.WorkStatus, login.Password FROM employee, " +
                "login WHERE login.Username = UN", result.get(0));
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

        ArrayList<Employee> result = testObj.allEmployees();

        assertEquals(3, result.size());
    }

    @Test
    public void noEmployeesReturnListOfSizeZero() throws Exception {
        statementHandler.prepareGlobalResultSet(null);
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

        EmployeeProjectTaskMap result = testObj.projectTaskMap(1);

        assertEquals(1, result.getProjects().size());
        assertEquals(3, result.getProjectTasks(result.getProjects()
                .get(0).getProjectName()).size());
    }

    @Test
    public void checkSQLStatementsForProjectTaskMap () throws Exception {
        this.resultOne.addColumn("TaskID");
        this.resultOne.addColumn("TaskName");

        this.resultTwo.addColumn("ProjectName");
        this.resultTwo.addColumn("ProjectID");

        Object rowOne[] = {1000, "Fix Bug"};
        Object rowTwo[] = {3000, "Write Tests"};
        Object rowThree[] = {2000, "Implement Feature"};
        resultOne.addRow(rowOne);
        resultOne.addRow(rowTwo);
        resultOne.addRow(rowThree);

        Object row1[] = {"TMT", 100};
        resultTwo.addRow(row1);

        statementHandler.prepareResultSet("SELECT * FROM tasks", resultOne);
        statementHandler.prepareResultSet("SELECT * FROM projects", resultTwo);

        this.testObj.projectTaskMap(1);
        ArrayList<String> sqlStatements = testObj.returnStatements();

        assertEquals("SELECT * FROM tasks WHERE tasks.TaskID IN " +
                "(SELECT taskID FROM employee_task_map WHERE EmployeeID = 1)",
                sqlStatements.get(0));
        assertEquals("SELECT * FROM projects WHERE ProjectID = ( " +
                        "SELECT projectID FROM project_task_map WHERE TaskID = 1000)",
                sqlStatements.get(1));
        assertEquals("SELECT * FROM projects WHERE ProjectID = ( " +
                        "SELECT projectID FROM project_task_map WHERE TaskID = 3000)",
                sqlStatements.get(2));
        assertEquals("SELECT * FROM projects WHERE ProjectID = ( " +
                        "SELECT projectID FROM project_task_map WHERE TaskID = 2000)",
                sqlStatements.get(3));
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

        int [] result = testObj.getCurrentEmployeeTaskAndProject(1);

        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
    }

    @Test
    public void checkSQLStatementsForGetCurrentEmployeeTaskAndProject(){
        this.resultOne.addColumn("TaskID");
        Object row11[] = {100};

        this.resultTwo.addColumn("ProjectID");
        Object row21[] = {1000};

        resultOne.addRow(row11);
        resultTwo.addRow(row21);

        statementHandler.prepareResultSet("SELECT TaskID", resultOne);
        statementHandler.prepareResultSet("SELECT ProjectID", resultTwo);

        testObj.getCurrentEmployeeTaskAndProject(1);
        ArrayList<String> sqlStatements = testObj.returnStatements();

        assertEquals("SELECT TaskID FROM time_logs WHERE TimeOut IS NULL " +
                "AND EmployeeID = 1", sqlStatements.get(0));
        assertEquals("SELECT ProjectID FROM project_task_map WHERE TaskID = 100",
                sqlStatements.get(1));
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

        int [] result = testObj.getCurrentEmployeeTaskAndProject(1);

        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
    }

    @Test
    public void noMatchesForQueryReturnZeroZero() throws Exception {
        statementHandler.prepareResultSet("SELECT TaskID", resultOne);
        statementHandler.prepareResultSet("SELECT ProjectID", resultTwo);

        int [] result = testObj.getCurrentEmployeeTaskAndProject(1);

        assertEquals(0, result[0]);
        assertEquals(0, result[1]);
    }

    @Test
    public void checkSQInGenEmployeeTimeSheet() throws Exception {
        statementHandler.prepareGlobalResultSet(resultOne);
        Date end = new Date(System.currentTimeMillis());
        Date start = new Date(System.currentTimeMillis()-600000000);

        testObj.genEmployeeTimeSheet(1, 1000, 100, start, end);
        ArrayList<String> sqlStatements = testObj.returnStatements();
        assertEquals("SELECT TimeIn, TimeOut, Date FROM time_logs WHERE " +
                "(Date BETWEEN 2016-11-24 AND 2016-11-17) AND EmployeeID = 1 " +
                "AND TaskID = 100", sqlStatements.get(0));
        testObj.clearList();

        testObj.genEmployeeTimeSheet(1, 0, 0, start, end);
        sqlStatements = testObj.returnStatements();
        assertEquals("SELECT TimeIn, TimeOut, Date, TaskID FROM time_logs WHERE " +
                "(Date BETWEEN 2016-11-24 AND 2016-11-17) AND EmployeeID = 1",
                sqlStatements.get(0));
        testObj.clearList();

        testObj.genEmployeeTimeSheet(1, 1000, 0, start, end);
        sqlStatements = testObj.returnStatements();
        assertEquals("SELECT TimeIn, TimeOut, Date, TaskID FROM time_logs WHERE " +
                "(Date BETWEEN 2016-11-24 AND 2016-11-17) AND EmployeeID = 1 AND " +
                "TaskID IN ( SELECT TaskID FROM project_task_map WHERE ProjectID = 1000)",
                sqlStatements.get(0));
    }

    @Test
    public void checkSQLInGenManagerTimeSheet() throws Exception {
        statementHandler.prepareGlobalResultSet(resultOne);
        Date end = new Date(System.currentTimeMillis());
        Date start = new Date(System.currentTimeMillis()-600000000);

        testObj.genManagerTimeSheet(1, 1000, 100, start, end);
        ArrayList<String> sqlStatements = testObj.returnStatements();
        assertEquals("SELECT TimeIn, TimeOut, Date FROM time_logs WHERE " +
                "(Date BETWEEN 2016-11-24 AND 2016-11-17) AND EmployeeID = 1 " +
                "AND TaskID = 100", sqlStatements.get(0));
        testObj.clearList();

        testObj.genManagerTimeSheet(1, 0, 100, start, end);
        sqlStatements = testObj.returnStatements();
        assertEquals("SELECT TimeIn, TimeOut, Date, TaskID FROM time_logs " +
                "WHERE (Date BETWEEN 2016-11-24 AND 2016-11-17) AND EmployeeID = 1",
                sqlStatements.get(0));
        testObj.clearList();

        testObj.genManagerTimeSheet(0, 0, 100, start, end);
        sqlStatements = testObj.returnStatements();
        assertEquals("SELECT TimeIn, TimeOut, Date, TaskID, EmployeeID FROM time_logs " +
                "WHERE (Date BETWEEN 2016-11-24 AND 2016-11-17)",
                sqlStatements.get(0));
        testObj.clearList();


        testObj.genManagerTimeSheet(1, 1000, 0, start, end);
        sqlStatements = testObj.returnStatements();
        assertEquals("SELECT TimeIn, TimeOut, Date, TaskID FROM time_logs " +
                        "WHERE (Date BETWEEN 2016-11-24 AND 2016-11-17) AND " +
                        "EmployeeID = 1 AND TaskID IN ( SELECT TaskID FROM " +
                        "project_task_map WHERE ProjectID = 1000)",
                sqlStatements.get(0));
        testObj.clearList();

        testObj.genManagerTimeSheet(0, 1000, 0, start, end);
        sqlStatements = testObj.returnStatements();
        assertEquals("SELECT TimeIn, TimeOut, Date, TaskID, EmployeeID FROM time_logs " +
                        "WHERE (Date BETWEEN 2016-11-24 AND 2016-11-17) AND " +
                        "TaskID IN ( SELECT TaskID FROM project_task_map WHERE " +
                        "ProjectID = 1000)",
                sqlStatements.get(0));
        testObj.clearList();

        testObj.genManagerTimeSheet(0, 1000, 100, start, end);
        sqlStatements = testObj.returnStatements();
        assertEquals("SELECT TimeIn, TimeOut, Date, EmployeeID FROM time_logs WHERE " +
                        "(Date BETWEEN 2016-11-24 AND 2016-11-17) AND TaskID = 100",
                sqlStatements.get(0));
        testObj.clearList();
    }
}