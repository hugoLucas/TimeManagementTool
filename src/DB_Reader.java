/**
 * Created by Hugo Lucas on 10/28/2016.
 */
import java.sql.*;
import java.util.ArrayList;

public class DB_Reader implements Database_Reader{

    private Connection reader_connection;   //Always use connection in shortest possible scope
    static private String db_username;
    static private String db_password;
    static private String url;

    /**
     * Default constructor. Initializes db_username and db_password
     * and url to default.
     */
    public DB_Reader(){
        this.db_username = "root";
        this.db_password = "mysql";
        //this.url = "jdbc:mysql://google/time_management_system?cloudSqlInstance=tmtproject-148101:us-central1:timemanagementsystem&socketFactory=com.google.cloud.sql.mysql.SocketFactory";
        this.url = "jdbc:mysql://localhost:3306/time_management_system";
    }

    /**
     * Given a user's login information, method will return the employee_number of the
     * user if the login credentials are valid. Invalid credentials will result in negative
     * value.
     * @param input_password    password retrieved by GUI page
     * @param input_username    username retrieved by GUI page
     * @return  Employee number of user if login is valid, -1 if invalid, -2 if other errors
     */
    public int[] login_user(String input_password, String input_username) {
        int[] result = {-1,-1, -1};
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            Connection reader_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepare query to database */
            PreparedStatement stmt = reader_connection.prepareStatement("SELECT employee.employeeID, employee.Manager, employee.WorkStatus, login.Password FROM employee, login WHERE login.Username = ?");
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

            return result;
        } catch (ClassNotFoundException e) {
            /* JAR may not be configured right or JDBC may not be working */
            e.printStackTrace();
            result[0] = -2;
            return result;
        } catch (SQLException e) {
            /* Catch all for errors I have not yet encountered */
            e.printStackTrace();
            result[0] = -2;
            return result;
        } finally{
            if (reader_connection != null)
                try { reader_connection.close(); }catch (Exception e){ /* Ignore this I guess! */}
        }
    }

    /**
     * Given a valid employee identification number, method will return a list of
     * projects an employee can work on.
     *
     * @param employee_number EmployeeID
     * @return list of all projects an employee can work, can be used by GUI to populate
     * input selection menus
     */
    @Override
    public ArrayList<EmployeeProject> projectsAvailable(int employee_number) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection reader_connection = DriverManager.getConnection(url, db_username, db_password);
            String query = "SELECT TaskID FROM employee_task_map WHERE EmployeeID = ?";
            PreparedStatement stmt = reader_connection.prepareStatement(query);
            stmt.setInt(1, employee_number);

            ResultSet queryResult = stmt.executeQuery();
            ArrayList<Integer> projs = new ArrayList<Integer>();
            while (queryResult.next()){
                int taskID = queryResult.getInt("TaskID");

                query = "SELECT ProjectID FROM project_task_map WHERE TaskID = ?";
                PreparedStatement stmt_inner = reader_connection.prepareStatement(query);
                stmt_inner.setInt(1, taskID);

                ResultSet queryResult_inner = stmt_inner.executeQuery();

                while(queryResult_inner.next()) {
                    int projectID = queryResult_inner.getInt("ProjectID");
                    if (!projs.contains(projectID))
                        projs.add(projectID);
                }

                stmt_inner.close();
                queryResult_inner.close();
            }

            stmt.close();
            queryResult.close();

            if (projs.size() > 0) {
                ArrayList<EmployeeProject> projectList = new ArrayList<EmployeeProject>(projs.size());
                for (int projectID: projs){
                    query = "SELECT ProjectName FROM projects WHERE ProjectID = ?";
                    stmt = reader_connection.prepareStatement(query);
                    stmt.setInt(1, projectID);

                    queryResult = stmt.executeQuery();

                    while(queryResult.next())
                        projectList.add(new EmployeeProject(queryResult.getString("ProjectName"), projectID));
                }

                return projectList;
            } else {return null; }
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
     * Given a valid employee identification number, method will return a list of
     * tasks an employee can work on.
     *
     * @param employee_number EmployeeID
     * @return list of all tasks an employee can work, can be used by GUI to populate
     * input selection menus
     */
    @Override
    public ArrayList<EmployeeTask> tasksAvailable(int employee_number) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection reader_connection = DriverManager.getConnection(url, db_username, db_password);
            String query = "SELECT TaskID FROM employee_task_map WHERE EmployeeID = ?";
            PreparedStatement stmt = reader_connection.prepareStatement(query);
            stmt.setInt(1, employee_number);

            ResultSet queryResult = stmt.executeQuery();
            ArrayList<EmployeeTask> tasks = new ArrayList<EmployeeTask>();
            while(queryResult.next()){
                int taskID = queryResult.getInt("TaskID");
                if(!tasks.contains(taskID)){
                    query = "SELECT TaskName FROM tasks WHERE TaskID = ?";
                    stmt = reader_connection.prepareStatement(query);
                    stmt.setInt(1, taskID);

                    ResultSet queryResult_inner = stmt.executeQuery();
                    while (queryResult_inner.next()){
                        String taskName = queryResult_inner.getString("TaskName");
                        tasks.add(new EmployeeTask(taskName, taskID));
                    }
                    queryResult_inner.close();
                }
            }

            stmt.close();
            queryResult.close();

            return tasks;
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
     * Helper method for employee times sheet generation.
     *
     * @param list list of all employee whose work hours are needed
     * @return list of all employee's work hours
     */
    @Override
    public ArrayList<EmployeeLog> employeeWorkHours(ArrayList<Employee> list){
        ArrayList<EmployeeLog> timeLogs = new ArrayList<>();
        int paramNum = 1;
        try {
            for (Employee currentEmployee: list) {
                Class.forName("com.mysql.jdbc.Driver");

                Connection reader_connection = DriverManager.getConnection(url, db_username, db_password);
                String query = "SELECT TimeIn, TimeOut, Date, TaskID FROM time_logs WHERE EmployeeID = ?";

                PreparedStatement stmt = reader_connection.prepareStatement(query);
                stmt.setInt(paramNum, currentEmployee.getEmployeeNumber());

                ResultSet queryResult = stmt.executeQuery();

                while(queryResult.next())
                    timeLogs.add(new EmployeeLog(queryResult.getTime("TimeIn"),
                            queryResult.getTime("TimeOut"), queryResult.getDate("Date"),
                            queryResult.getInt("TaskID")));

                stmt.close();
                queryResult.close();
            }

            return timeLogs;
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
     * Generates a list of all employees currently working at the company. Used
     * by GUI in manager report generation.
     *
     * @return list of employees
     */
    @Override
    public ArrayList<Employee> allEmployees() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection reader_connection = DriverManager.getConnection(url, db_username, db_password);
            String query = "SELECT EmployeeID, FirstName, LastName, EndDate FROM employee";
            PreparedStatement stmt = reader_connection.prepareStatement(query);
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
     * Tries to search for assigned tasks to a particular employee via their
     * employee number. As long as the tasks exist it's put into a container
     * and then returned.
     *
     * @param employee_number
     * @return the tasks for a particular project
     */
    public EmployeeProjectTaskMap projectTaskMap(int employee_number) {
        EmployeeProjectTaskMap projTask = new EmployeeProjectTaskMap();
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            Connection reader_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepares query for get all tasks for a given employee */
            String query = "SELECT * FROM tasks";
            PreparedStatement taskStatement = reader_connection.prepareStatement(query);
            ResultSet taskQueryResult = taskStatement.executeQuery();

            while(taskQueryResult.next()){
                /* Create Task object from query results*/
                int taskID = taskQueryResult.getInt("TaskID");
                EmployeeTask tk = new EmployeeTask(taskQueryResult.getString("TaskName"), taskID);

                query = "SELECT * FROM projects WHERE ProjectID = ( SELECT projectID FROM project_task_map WHERE TaskID = ?)";
                PreparedStatement projectStatement = reader_connection.prepareStatement(query);
                projectStatement.setInt(1, taskID);
                ResultSet projectQueryResult = projectStatement.executeQuery();

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
            Connection reader_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepares query to get task in row with null timeout*/
            String query = "SELECT TaskID FROM time_logs WHERE TimeOut IS NULL AND EmployeeID = ?";
            PreparedStatement stmt = reader_connection.prepareStatement(query);
            stmt.setInt(1, employeeID);

            ResultSet queryRes = stmt.executeQuery();

            if(queryRes.next())
                ret[0] = queryRes.getInt("TaskID");

            /* Get project that has task as assigned task */
            query = "SELECT ProjectID FROM project_task_map WHERE TaskID = ?";
            stmt = reader_connection.prepareStatement(query);
            stmt.setInt(1, ret[0]);

            queryRes = stmt.executeQuery();
            if(queryRes.next())
                ret[1] = queryRes.getInt("ProjectID");

            return ret;

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
            Connection reader_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepares query to get task in row with null timeout*/
            String query;
            if(projID == 0){
                /* Select all rows that for the employee */
                query = "SELECT TimeIn, TimeOut, Date, TaskID FROM time_logs WHERE (Date BETWEEN ? AND ?) AND EmployeeID = ?";
            }else if(taskID == 0){
                /* Select all tasks associated with a given project */
                query = "SELECT TimeIn, TimeOut, Date, TaskID FROM time_logs WHERE (Date BETWEEN ? AND ?) AND EmployeeID = ? AND TaskID IN ( SELECT TaskID FROM project_task_map WHERE ProjectID = ?)";
            }else{
                /* Select all logs for a given task and project */
                query = "SELECT TimeIn, TimeOut, Date FROM time_logs WHERE (Date BETWEEN ? AND ?) AND EmployeeID = ? AND TaskID = ?";
            }

            /* Prepare query and set parameters in accordance with function inputs */
            int index = 1;
            PreparedStatement stmt = reader_connection.prepareStatement(query);
            stmt.setDate(index++, end);
            stmt.setDate(index++, start);
            stmt.setInt(index++, employeeNumber);
            if(taskID == 0 && projID != 0)
                stmt.setInt(index++, projID);
            else if(projID != 0)
                stmt.setInt(index++, taskID);

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
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            Connection reader_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepares query to get task in row with null timeout*/
            String query;
            if(projID == 0 && employeeNumber != 0){
                /* Select all rows that for the employee */
                query = "SELECT TimeIn, TimeOut, Date, TaskID FROM time_logs WHERE (Date BETWEEN ? AND ?) AND EmployeeID = ?";
            }else if(projID == 0 && employeeNumber == 0){
                query = "SELECT TimeIn, TimeOut, Date, TaskID, EmployeeID FROM time_logs WHERE (Date BETWEEN ? AND ?)";
            }else if(taskID == 0 && employeeNumber != 0){
                /* Select all tasks associated with a given project */
                query = "SELECT TimeIn, TimeOut, Date, TaskID FROM time_logs WHERE (Date BETWEEN ? AND ?) AND EmployeeID = ? AND TaskID IN ( SELECT TaskID FROM project_task_map WHERE ProjectID = ?)";
            }else if(taskID == 0 && employeeNumber == 0){
                query = "SELECT TimeIn, TimeOut, Date, TaskID, EmployeeID FROM time_logs WHERE (Date BETWEEN ? AND ?) AND TaskID IN ( SELECT TaskID FROM project_task_map WHERE ProjectID = ?)";
            }else if (employeeNumber == 0){
                /* Select all logs for a given task and project */
                query = "SELECT TimeIn, TimeOut, Date, EmployeeID FROM time_logs WHERE (Date BETWEEN ? AND ?) AND TaskID = ?";
            }else{
                query = "SELECT TimeIn, TimeOut, Date FROM time_logs WHERE (Date BETWEEN ? AND ?) AND EmployeeID = ? AND TaskID = ?";
            }

            /* Prepare query and set parameters in accordance with function inputs */
            int index = 1;
            PreparedStatement stmt = reader_connection.prepareStatement(query);
            stmt.setDate(index++, end);
            stmt.setDate(index++, start);
            if(employeeNumber != 0)
                stmt.setInt(index++, employeeNumber);
            if(taskID == 0 && projID != 0)
                stmt.setInt(index++, projID);
            else if(projID != 0)
                stmt.setInt(index++, taskID);

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
     * Generates a list of all Tasks that are not assigned to a specific project.
     * Used to bing tasks to project by Manager level employee.
     *
     * @return  list of all orphaned tasks
     */
    public ArrayList<EmployeeTask> orphanTasks(){
        ArrayList<EmployeeTask> taskList = new ArrayList<>();
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            Connection reader_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepares query to get task in row with null timeout*/
            String query = "Select tasks.TaskID, tasks.TaskName FROM tasks WHERE taskID NOT IN(SELECT project_task_map.TaskID " +
                    "FROM project_task_map)";

            PreparedStatement stmt = reader_connection.prepareStatement(query);
            ResultSet res = stmt.executeQuery();

            while(res.next())
                taskList.add(new EmployeeTask(res.getString("TaskName"), res.getInt("TaskID")));

            if(taskList.size() > 0)
                return taskList;
            else
                return null;

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
     */
    public ArrayList<ProjectLine> genProgressReport(){
        ArrayList<ProjectLine> rep = new ArrayList<>();
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            Connection reader_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            String reportQuery = "select p.ProjectID, sum(t.EstimatedTimel) as TimeEstimate, sum(t.TimeWorked) as TimeWorked" +
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
            Connection reader_connection = DriverManager.getConnection(url, db_username, db_password);
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
}
