/**
 * This class is in retrieving information from the database. Although DB_Writer also
 * retrieves information from the database, this class will handle the majority of reading
 * of database information in the program.
 *
 * Created by Hugo Lucas on 10/28/2016.
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DB_Reader {

    private Connection reader_connection;       /* Connection shared by all methods, use in smallest possible scope */
    static private String db_username;          /* Username used to access database */
    static private String db_password;          /* Password used to access database */
    static private String url;                  /* Url used to establish JDBC connection */

    /* VARIABLES SHOULD BE USED TO VALIDATE METHODS ONLY -- DO NOT USE IN ANYTHING BESIDES TEST CLASSES */
    private ArrayList<String> sqlStatements;         /* String representation of the SQL code passed to the database */

    /**
     * Default constructor. Initializes fields to their default values. If testing is being done use the local
     * host url, otherwise use the CloudSQL url.
     */
    public DB_Reader(){
        this.db_username = "root";
        this.db_password = "mysql";
        this.sqlStatements = new ArrayList<>();

        /* Use this for release testing */
        //this.url = "jdbc:mysql://google/time_management_system?cloudSqlInstance=tmtproject-148101:us-central1:timemanagementsystem&socketFactory=com.google.cloud.sql.mysql.SocketFactory";

        /* Use this for all other internal testing */
        this.url = "jdbc:mysql://localhost:3306/time_management_system";
    }

    /**
     * Given a user's login information, method will return the number, work status, and rank of the
     * user if the login credentials are valid. Invalid credentials will result in negative
     * values.
     *
     * @param input_password    password retrieved by GUI page
     * @param input_username    username retrieved by GUI page
     * @return                  positive numbers for the variables described above, negative values if login is invalid
     */
    public int[] login_user(String input_password, String input_username) {
        int[] result = {-1,-1, -1};
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.reader_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepare query to database */
            String sqlQuery = "SELECT employee.employeeID, employee.Manager, employee.WorkStatus, login.Password FROM employee, login WHERE login.Username = ?";
            PreparedStatement stmt = reader_connection.prepareStatement(sqlQuery);
            stmt.setString(1, input_username);

            /* Execute statement */
            ResultSet queryResult = stmt.executeQuery();

            /* Check if statement contains rows with matching db_username */
            while (queryResult.next()){
                if (queryResult.getString("password").equals(input_password)) {
                    result[0] = queryResult.getInt("EmployeeID");
                    result[1] = queryResult.getInt("workstatus");
                    result[2] = queryResult.getInt("manager");
                    break;
                }
            }

            ArrayList<Object> parameterList = new ArrayList<>();
            parameterList.add(input_username);
            this.addStatement(sqlQuery, parameterList);

            return result;
        } catch (Exception e) {
            /* JAR may not be configured right or JDBC may not be working */
            result[0] = -2;
            return result;
        } finally{
            if (reader_connection != null)
                try { reader_connection.close(); }catch (Exception e){ /* Ignore this I guess! */}
        }
    }

    /**
     * Looks through database in order to find all employees entered into the database.
     *
     * @return      ArrayList containing Employee objects representing employees in database
     */
    public ArrayList<Employee> allEmployees() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.reader_connection = DriverManager.getConnection(url, db_username, db_password);
            String sqlQuery = "SELECT EmployeeID, FirstName, LastName, EndDate FROM employee";
            PreparedStatement stmt = reader_connection.prepareStatement(sqlQuery);
            ResultSet queryResult = stmt.executeQuery();

            ArrayList<Employee> empList = new ArrayList<Employee>();
            while(queryResult.next()){
                String endDate = queryResult.getString("EndDate");

                if(endDate == null){
                    int employeeId = queryResult.getInt("EmployeeID");
                    String firstName = queryResult.getString("FirstName");
                    String lastName = queryResult.getString("LastName");

                    empList.add(new Employee(firstName, lastName, employeeId));
                }
            }
            stmt.close();
            queryResult.close();

            return empList;
        } catch (Exception e) {
            return null;
        } finally {
            if (reader_connection != null)
                try { reader_connection.close(); } catch (Exception e) { /* Ignore this I guess! */}
        }
    }

    /**
     * Generates a mapping of all task and project relationships in the database.
     *
     * @param employee_number       Limits results
     * @return                      EmployeeProjectTaskMap which represents relationships between tasks and projects
     */
    public EmployeeProjectTaskMap projectTaskMap(int employee_number) {
        EmployeeProjectTaskMap projTask = new EmployeeProjectTaskMap();
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.reader_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepares query for get all tasks for a given employee */
            String sqlQueryOne = "SELECT * FROM tasks WHERE tasks.TaskID IN (SELECT taskID" +
                    " FROM employee_task_map WHERE EmployeeID = ?)";
            PreparedStatement taskStatement = reader_connection.prepareStatement(sqlQueryOne);
            taskStatement.setInt(1, employee_number);
            ResultSet taskQueryResult = taskStatement.executeQuery();

            ArrayList<Object> parameterList = new ArrayList<>();
            parameterList.add(employee_number);
            this.addStatement(sqlQueryOne, parameterList);
            while(taskQueryResult.next()){
                /* Create Task object from query results*/
                int taskID = taskQueryResult.getInt("TaskID");
                EmployeeTask tk = new EmployeeTask(taskQueryResult.getString("TaskName"), taskID);

                String sqlQueryTwo = "SELECT * FROM projects WHERE ProjectID = ( SELECT projectID FROM project_task_map WHERE TaskID = ?)";
                PreparedStatement projectStatement = reader_connection.prepareStatement(sqlQueryTwo);
                projectStatement.setInt(1, taskID);
                ResultSet projectQueryResult = projectStatement.executeQuery();

                parameterList = new ArrayList<>();
                parameterList.add(taskID);
                this.addStatement(sqlQueryTwo, parameterList);
                while(projectQueryResult.next()){
                    EmployeeProject emp = new EmployeeProject(projectQueryResult.getString("projectname"), projectQueryResult.getInt("projectid"));
                    projTask.addMapping(emp, tk);
                }
            }

            return projTask;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader_connection != null)
                try {
                    reader_connection.close();
                } catch (Exception e) { /* Ignore this I guess! */}
        }
    }

    /**
     * Using an employee's ID, method pulls the projects that the given
     * employee is working on. Can be used to populate the tasks employee is
     * working on as well.
     *
     * @param employeeID
     * @return the projectID for the employee, hence allowing you to populate tasks
     */
    public int[] getCurrentEmployeeTaskAndProject(int employeeID){
        int[] ret = {0,0};
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.reader_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepares query to get task in row with null timeout*/
            String sqlQuery = "SELECT TaskID FROM time_logs WHERE TimeOut IS NULL AND EmployeeID = ?";
            PreparedStatement stmt = reader_connection.prepareStatement(sqlQuery);
            stmt.setInt(1, employeeID);

            ResultSet queryRes = stmt.executeQuery();


            ArrayList<Object> parameterList = new ArrayList<>();
            parameterList.add(employeeID);
            this.addStatement(sqlQuery, parameterList);
            if(queryRes.next())
                ret[0] = queryRes.getInt("TaskID");

            /* Get project that has task as assigned task */
            sqlQuery = "SELECT ProjectID FROM project_task_map WHERE TaskID = ?";
            stmt = reader_connection.prepareStatement(sqlQuery);
            stmt.setInt(1, ret[0]);

            parameterList = new ArrayList<>();
            parameterList.add(ret[0]);
            this.addStatement(sqlQuery, parameterList);
            queryRes = stmt.executeQuery();
            if(queryRes.next())
                ret[1] = queryRes.getInt("ProjectID");
            return ret;
        } catch (Exception e) {
            return null;
        } finally {
            if (reader_connection != null)
                try { reader_connection.close(); } catch (Exception e) { /* Ignore this I guess! */}
        }
    }

    /**
     * Generates TimeLog objects that will constitute a single employees timesheet.
     * TaskID = 0 when the "All Tasks" option has been selected, ProjectID = 0 and TaskID = 0
     * when the "All Projects" option has been selected.
     * @param employeeNumber
     * @param taskID
     * @param start
     * @param end
     * @return  container of Employee Logs to generate report
     */
    public ArrayList<EmployeeLog> genEmployeeTimeSheet(int employeeNumber, int projID,  int taskID, Date start, Date end){
        ArrayList<EmployeeLog>  records = new ArrayList<>();
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.reader_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepares query to get task in row with null timeout*/
            String sqlQuery;
            if (projID == 0) {
                /* Select all rows that for the employee */
                sqlQuery = "SELECT TimeIn, TimeOut, Date, TaskID FROM time_logs WHERE (Date BETWEEN ? AND ?) AND EmployeeID = ?";
            } else if (taskID == 0) {
                /* Select all tasks associated with a given project */
                sqlQuery = "SELECT TimeIn, TimeOut, Date, TaskID FROM time_logs WHERE (Date BETWEEN ? AND ?) AND EmployeeID = ? AND TaskID IN ( SELECT TaskID FROM project_task_map WHERE ProjectID = ?)";
            } else {
                /* Select all logs for a given task and project */
                sqlQuery = "SELECT TimeIn, TimeOut, Date FROM time_logs WHERE (Date BETWEEN ? AND ?) AND EmployeeID = ? AND TaskID = ?";
            }

            /* Prepare query and set parameters in accordance with function inputs */
            ArrayList<Object> parameterList = new ArrayList<>();

            int index = 1;
            PreparedStatement stmt = reader_connection.prepareStatement(sqlQuery);
            stmt.setDate(index++, end);
            parameterList.add(end);

            stmt.setDate(index++, start);
            parameterList.add(start);

            stmt.setInt(index++, employeeNumber);
            parameterList.add(employeeNumber);

            if (taskID == 0 && projID != 0) {
                stmt.setInt(index++, projID);
                parameterList.add(projID);
            } else if (projID != 0){
                stmt.setInt(index++, taskID);
                parameterList.add(taskID);
            }
            this.addStatement(sqlQuery, parameterList);

            /* Execute and store query results */
            ResultSet queryRes = stmt.executeQuery();
            while(queryRes.next()){
                Time in = queryRes.getTime("TimeIn");
                Time out = queryRes.getTime("TimeOut");
                Date d = queryRes.getDate("Date");

                if(projID == 0 || taskID == 0) {
                    int tkID = queryRes.getInt("TaskID");
                    records.add(new EmployeeLog(in, out, d, tkID));
                }else
                    records.add(new EmployeeLog(in, out, d, taskID));
            }

            return records;
        } catch (Exception e) {return null;
        } finally {
            if (reader_connection != null) try { reader_connection.close(); } catch (Exception e) { /* Ignore this I guess! */}
        }
    }

    /**
     * Takes in an input of employeeNumber and for a particular project and task,
     * within a date range. While checking for potential blank data or errors, it
     * pulls all of the time logs for that employee's task and returns it.
     *
     * @param employeeNumber
     * @param projID
     * @param taskID
     * @param start
     * @param end
     * @return records of a particular employee's timesheet
     */
    public ArrayList<EmployeeLog> genManagerTimeSheet(int employeeNumber, int projID,  int taskID, Date start, Date end){
        ArrayList<EmployeeLog>  records = new ArrayList<>();
        ArrayList<Object> parameterList = new ArrayList<>();
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.reader_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepares query to get task in row with null timeout*/
            String sqlQuery;
            if(projID == 0 && employeeNumber != 0){
                /* Select all rows that for the employee */
                sqlQuery = "SELECT TimeIn, TimeOut, Date, TaskID FROM time_logs WHERE (Date BETWEEN ? AND ?) AND EmployeeID = ?";
            }else if(projID == 0 && employeeNumber == 0){
                sqlQuery = "SELECT TimeIn, TimeOut, Date, TaskID, EmployeeID FROM time_logs WHERE (Date BETWEEN ? AND ?)";
            }else if(taskID == 0 && employeeNumber != 0){
                /* Select all tasks associated with a given project */
                sqlQuery = "SELECT TimeIn, TimeOut, Date, TaskID FROM time_logs WHERE (Date BETWEEN ? AND ?) AND EmployeeID = ? AND TaskID IN ( SELECT TaskID FROM project_task_map WHERE ProjectID = ?)";
            }else if(taskID == 0 && employeeNumber == 0){
                sqlQuery = "SELECT TimeIn, TimeOut, Date, TaskID, EmployeeID FROM time_logs WHERE (Date BETWEEN ? AND ?) AND TaskID IN ( SELECT TaskID FROM project_task_map WHERE ProjectID = ?)";
            }else if (employeeNumber == 0){
                /* Select all logs for a given task and project */
                sqlQuery = "SELECT TimeIn, TimeOut, Date, EmployeeID FROM time_logs WHERE (Date BETWEEN ? AND ?) AND TaskID = ?";
            }else{
                sqlQuery = "SELECT TimeIn, TimeOut, Date FROM time_logs WHERE (Date BETWEEN ? AND ?) AND EmployeeID = ? AND TaskID = ?";
            }

            /* Prepare query and set parameters in accordance with function inputs */
            int index = 1;
            PreparedStatement stmt = reader_connection.prepareStatement(sqlQuery);
            stmt.setDate(index++, end);
            parameterList.add(end);
            stmt.setDate(index++, start);
            parameterList.add(start);
            if(employeeNumber != 0) {
                stmt.setInt(index++, employeeNumber);
                parameterList.add(employeeNumber);
            } if(taskID == 0 && projID != 0) {
                stmt.setInt(index++, projID);
                parameterList.add(projID);
            } else if(projID != 0) {
                stmt.setInt(index++, taskID);
                parameterList.add(taskID);
            }

            this.addStatement(sqlQuery, parameterList);
            /* Execute and store query results */
            ResultSet queryRes = stmt.executeQuery();
            while(queryRes.next()){
                Time in = queryRes.getTime("TimeIn");
                Time out = queryRes.getTime("TimeOut");
                Date d = queryRes.getDate("Date");

                int eID = employeeNumber;
                if(employeeNumber == 0)
                    eID = queryRes.getInt("EmployeeID");

                if(projID == 0 || taskID == 0) {
                    int tkID = queryRes.getInt("TaskID");

                    if(eID == 0)
                        records.add(new EmployeeLog(in, out, d, tkID));
                    else
                        records.add(new EmployeeLog(in, out, d, tkID, eID));
                }else
                    if(eID == 0)
                        records.add(new EmployeeLog(in, out, d, taskID));
                    else
                        records.add(new EmployeeLog(in, out, d, taskID, eID));

            }

            return records;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader_connection != null)
                try {
                    reader_connection.close();
                } catch (Exception e) { /* Ignore this I guess! */}
        }
    }

    /**
     * Looks through database and determines how many hours a project's tasks have been worked. Query compares this to the
     * estimated amount of work-hours needed to complete the project.
     *
     * @return      a project line object representing the time worked on a specific project
     */
    public ArrayList<ProjectLine> genProgressReport(){
        ArrayList<ProjectLine> rep = new ArrayList<>();
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.reader_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            String reportQuery = "select p.ProjectID, sum(t.TimeEstimate) as TimeEstimate, sum(t.TimeWorked) as TimeWorked" +
                    " from tasks as t, project_task_map as p where p.TaskID = t.TaskID group by p.ProjectID";
            PreparedStatement reportStmt = reader_connection.prepareStatement(reportQuery);
            ResultSet reportSet = reportStmt.executeQuery();
            while(reportSet.next()) {
                int projID = reportSet.getInt("ProjectID");
                int hourEst = reportSet.getInt("TimeEstimate");
                int hourWrk = reportSet.getInt("TimeWorked");
                String projectName = "";

                PreparedStatement subStmt = reader_connection.prepareStatement("SELECT ProjectName FROM projects WHERE ProjectID = ?");
                subStmt.setInt(1, projID);
                ResultSet subSet = subStmt.executeQuery();
                if(subSet.next())
                    projectName = subSet.getString("ProjectName");
                rep.add(new ProjectLine(projID, projectName, hourWrk, hourEst));
            }
            return rep;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader_connection != null)
                try { reader_connection.close(); } catch (Exception e) { /* Ignore this I guess! */}
        }
    }

    /**
     * Returns all tasks not assigned to a given employee.
     *
     * @param employeeID    employee number
     * @return  An array list containing all tasks the employee is not assigned to.
     */
    public ArrayList<EmployeeTask> unassignedTask(int employeeID){
        ArrayList<EmployeeTask> tskList = new ArrayList<>();
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.reader_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            String taskQuery = "SELECT TaskID, TaskName FROM tasks WHERE taskID NOT IN (" +
                    " SELECT TaskID FROM employee_task_map WHERE employeeID = ?)";
            PreparedStatement taskStmt = reader_connection.prepareStatement(taskQuery);
            taskStmt.setInt(1, employeeID);
            ResultSet taskSet = taskStmt.executeQuery();

            while(taskSet.next()){
                int taskID = taskSet.getInt("TaskID");
                String taskName = taskSet.getString("TaskName");

                tskList.add(new EmployeeTask(taskName, taskID));
            }

            taskStmt.close();
            taskSet.close();

            return tskList;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader_connection != null)
                try { reader_connection.close(); } catch (Exception e) { /* Ignore this I guess! */}
        }
    }

    /* TESTING METHODS -- DO NOT USE OUTSIDE OF TESTING */

    private void addStatement(String sqlStatement, ArrayList<Object> parameters){
        int listIndex = 0;
        StringBuilder str = new StringBuilder(sqlStatement);
        for(int i = 0; i < str.length(); i ++)
            if(str.charAt(i) == '?') {
                str.replace(i, i+1, parameters.get(listIndex).toString());
                if(parameters.size() <= listIndex + 1)
                    break;
                else
                    listIndex ++;
            }

        this.sqlStatements.add(str.toString());
    }

    public ArrayList<String> returnStatements(){
        return this.sqlStatements;
    }

    public void clearList(){
        this.sqlStatements = new ArrayList<>();
    }
}
