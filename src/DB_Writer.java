import java.sql.*;

/**
 * Created by Hugo Lucas on 11/4/2016.
 */
public class DB_Writer {

    private Connection writer_connection;       /* Connection used by all methods to access database */
    static private String db_username;          /* Username used to access database */
    static private String db_password;          /* Password used to access database */
    static private String url;                  /* Connection url to connect to database */

    /**
     *  Constructor, initialises private fields with default database login credentials
     */
    public DB_Writer(){
        this.db_username = "root";
        this.db_password = "mysql";
        //this.url = "jdbc:mysql://google/time_management_system?cloudSqlInstance=tmtproject-148101:us-central1:timemanagementsystem&socketFactory=com.google.cloud.sql.mysql.SocketFactory";
        //this.url = "jdbc:mysql://localhost:3306/time_management_system";
    }

    /**
     * Creates a new time log in the database in order to clock in the given employee.
     *
     * @param employeeNumber    employee identification number of the employee that is clocking in
     * @param taskID            the task identification number of the task the employee is going to work on
     */
    public boolean clockInUser(int employeeNumber, int taskID){
        long currentMilliTime = System.currentTimeMillis();
        Time clockInTime = new Time(currentMilliTime);
        Date clockInDate = new Date(currentMilliTime);
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.writer_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepare query to database */
            String queryFields = "INSERT INTO time_logs ( EmployeeID, TimeIn, Date, TaskID)";
            String queryValues = "VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = writer_connection.prepareStatement(queryFields + queryValues);
            insertStatement.setInt(1, employeeNumber);
            insertStatement.setTime(2, clockInTime);
            insertStatement.setDate(3, clockInDate);
            insertStatement.setInt(4, taskID);

            /* Execute statement */
            insertStatement.executeUpdate();
            return true;
        } catch (ClassNotFoundException e) {
            /* JAR may not be configured right or JDBC may not be working */
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            /* Catch all for errors I have not yet encountered */
            e.printStackTrace();
            return false;
        } finally{
            if (writer_connection != null)
                try { writer_connection.close(); }catch (Exception e){ /* Ignore this I guess! */}
        }
    }

    /**
     * Updates the time-out field for an employee time-log associated with a given task and employee identification
     * number and with a time-out field equal to null. Calculates the hours worked on task by an employee and adds
     * that number to the database in order to track progress of project.
     *
     * @param employeeNumber    employee identification number
     * @param taskID            task identification number
     */
    public void clockOutUser(int employeeNumber, int taskID) {
        Time clockOutTime = new Time(System.currentTimeMillis());
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.writer_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            String differenceQuery = "SELECT TimeIn, Date FROM time_logs WHERE EmployeeID = ? AND TaskID = ? AND TimeOut IS NULL";
            PreparedStatement difStatement = writer_connection.prepareStatement(differenceQuery);
            difStatement.setInt(1, employeeNumber);
            difStatement.setInt(2, taskID);

            /* Execute statement */
            ResultSet difTime = difStatement.executeQuery();
            long time = 0;
            if(difTime.next()) {
                Time timeObj = difTime.getTime("TimeIn");
                long miliHour = timeObj.getTime();
                time = miliHour - 86400000;
            }

            difTime.close();
            difStatement.close();

            long currentTime =  clockOutTime.getTime() % 86400000;
            double differenceInHours = (currentTime - time) * 2.78 * Math.pow(10,-7);

            /* Prepare query to database */
            String updateQuery = "UPDATE time_logs SET TimeOut = ? WHERE EmployeeID = ? AND TaskID = ? AND TimeOut IS NULL";
            PreparedStatement updateStatement = writer_connection.prepareStatement(updateQuery);
            updateStatement.setTime(1, clockOutTime);
            updateStatement.setInt(2, employeeNumber);
            updateStatement.setInt(3, taskID);

            updateStatement.executeUpdate();

            updateQuery = "UPDATE tasks SET TimeWorked = TimeWorked + ? WHERE TaskID = ?";
            updateStatement = writer_connection.prepareStatement(updateQuery);
            updateStatement.setDouble(1, differenceInHours);
            updateStatement.setInt(2, taskID);

            /* Execute statement */
            updateStatement.executeUpdate();
            updateStatement.close();



        } catch (ClassNotFoundException e) {
            /* JAR may not be configured right or JDBC may not be working */
            e.printStackTrace();
        } catch (SQLException e) {
            /* Catch all for errors I have not yet encountered */
            e.printStackTrace();
        } finally{
            if (writer_connection != null)
                try { writer_connection.close(); }catch (Exception e){ /* Ignore this I guess! */}
        }
    }

    /**
     * Adds a new employee to the database.
     *
     * @param firstName         first name of the new employee
     * @param lastName          last name of the new employee
     * @param hireDate          hire date of the new employee
     * @param managerStatus     rank of the new employee
     */
    public boolean addEmployee(String firstName, String lastName, Date hireDate, int managerStatus){
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.writer_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepare query to database */
            String updateQuery = "INSERT INTO employee (FirstName, LastName, HireDate, WorkStatus, Manager) ";
            String updateQueryValues = "VALUES(?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = writer_connection.prepareStatement(updateQuery + updateQueryValues);

            int index = 1;
            insertStatement.setString(index++, firstName);
            insertStatement.setString(index++, lastName);
            insertStatement.setDate(index++, hireDate);
            insertStatement.setInt(index++, 0);
            insertStatement.setInt(index, managerStatus);

            insertStatement.executeUpdate();
            insertStatement.close();

            return true;
        } catch (ClassNotFoundException e) {
            /* JAR may not be configured right or JDBC may not be working */
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            /* Catch all for errors I have not yet encountered */
            e.printStackTrace();
            return false;
        } finally{
            if (writer_connection != null)
                try { writer_connection.close(); }catch (Exception e){ /* Ignore this I guess! */}
        }
    }

    /**
     * Creates a new task in the database and then binds that task to a project.
     *
     * @param newTaskName           the name of the new task
     * @param projectSelected       the project that task will be bound to
     * @param hourEstimate          the estimated man hours needed to complete task
     */
    public boolean addTask(String newTaskName, String projectSelected, int hourEstimate){
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.writer_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepare query to database */
            String insertQuery = "INSERT INTO tasks(TaskName, EstimatedTimel) VALUES (?, ?)";
            PreparedStatement insertStatement = writer_connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, newTaskName);
            insertStatement.setInt(2, hourEstimate);

            insertStatement.executeUpdate();
            ResultSet keyGetter = insertStatement.getGeneratedKeys();
            int newTaskID = -1;
            if(keyGetter.next())
                newTaskID = keyGetter.getInt(1);

            insertQuery = "INSERT INTO project_task_map (TaskID, ProjectID) SELECT ?, projects.ProjectID FROM projects WHERE projects.ProjectName = ? ";
            insertStatement = writer_connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, newTaskID);
            insertStatement.setString(2, projectSelected);

            insertStatement.executeUpdate();

            return true;

        } catch (ClassNotFoundException e) {
            /* JAR may not be configured right or JDBC may not be working */
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            /* Catch all for errors I have not yet encountered */
            e.printStackTrace();
            return false;
        } finally{
            if (writer_connection != null)
                try { writer_connection.close(); }catch (Exception e){ /* Ignore this I guess! */}
        }
    }

    /**
     * Adds a new project to the database
     *
     * @param projectName   the name of the new project to add
     */
    public boolean addProject(String projectName){
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.writer_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepare query to database */
            String insertQuery = "INSERT INTO projects (ProjectName) VALUES (?);";
            PreparedStatement insertStatement = writer_connection.prepareStatement(insertQuery);
            insertStatement.setString(1, projectName);

            /* Execute statement */
            insertStatement.executeUpdate();
            return true;
        } catch (ClassNotFoundException e) {
            /* JAR may not be configured right or JDBC may not be working */
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            /* Catch all for errors I have not yet encountered */
            e.printStackTrace();
            return false;
        } finally{
            if (writer_connection != null)
                try { writer_connection.close(); }catch (Exception e){ /* Ignore this I guess! */}
        }
    }

    /**
     * Sets login credentials for newly added employees. Only works if the data provided is associated with
     * an employee that exists in the database but does not have a username and password.
     *
     * @param firstName     first name of newly hired employee
     * @param lastName      last name of the newly hired employee
     * @param dateHired     the date the employee was hired
     * @param userName      the desired username
     * @param passWord      the desired password
     * @return              1 if login crednentials are created, -1 if employee already has login credentials
     */
    public int createLogin(String firstName, String lastName, Date dateHired, String userName, String passWord){
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.writer_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            String inspectionQuery = "SELECT login.Username FROM login WHERE login.EmployeeID = ( SELECT employee.EmployeeID FROM employee " +
                "WHERE employee.FirstName = ? AND employee.HireDate = ?)";
            PreparedStatement inspectionStatement = writer_connection.prepareStatement(inspectionQuery);
            inspectionStatement.setString(1, firstName);
            inspectionStatement.setDate(2, dateHired);
            ResultSet inspectionSet = inspectionStatement.executeQuery();

            if(!inspectionSet.next()) {
                /* Prepare query to database */
                String insertQuery = "INSERT INTO login (Username, Password, EmployeeID) SELECT ?, ?, employee.EmployeeID " +
                        "FROM employee WHERE employee.FirstName = ? AND employee.LastName = ? AND employee.HireDate = ?";

                PreparedStatement insertStatement = writer_connection.prepareStatement(insertQuery);
                insertStatement.setString(1, userName);
                insertStatement.setString(2, passWord);
                insertStatement.setString(3, firstName);
                insertStatement.setString(4, lastName);
                insertStatement.setDate(5, dateHired);

                insertStatement.executeUpdate();
                insertStatement.close();

                return 1;
            }else
                return -1;

        } catch (ClassNotFoundException e) {
            /* JAR may not be configured right or JDBC may not be working */
            e.printStackTrace();
            return -2;
        } catch (SQLException e) {
            /* Catch all for errors I have not yet encountered */
            e.printStackTrace();
            return -2;
        } finally{
            if (writer_connection != null)
                try { writer_connection.close(); }catch (Exception e){ /* Ignore this I guess! */}
        }
    }

    /**
     * Changes the work status of an employee in order to track whether that employee is currently
     * clocked in or out.
     *
     * @param employeeNumber        employee identification number of the employee who's status will change
     * @param newStatus             final value of work status
     */
    public boolean setWorkStatus(int employeeNumber, int newStatus){
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.writer_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepare query to database */
            String updateQuery = "UPDATE employee SET WorkStatus = ? WHERE EmployeeID = ?";
            PreparedStatement updateStatement = writer_connection.prepareStatement(updateQuery);
            updateStatement.setInt(1,newStatus);
            updateStatement.setInt(2, employeeNumber);

            /* Execute statement */
            updateStatement.executeUpdate();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        } finally{
            if (writer_connection != null)
                try { writer_connection.close(); }catch (Exception e){ /* Ignore this I guess! */}
        }
    }


    /**
     * Assigns the given task to an employee by altering the mapping in the database.
     *
     * @param taskID        task identification number of the task to assign
     * @param employeeID    employee identification number of the employee task will be assigned to
     */
    public boolean assignTaskToEmployee(int taskID, int employeeID){
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            this.writer_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepare query to database */
            String insertQuery = "INSERT employee_task_map(EmployeeID, TaskID) VALUES (?, ?)";
            PreparedStatement insertStmt = writer_connection.prepareStatement(insertQuery);
            insertStmt.setInt(1, employeeID);
            insertStmt.setInt(2, taskID);

            insertStmt.execute();
            return true;

        } catch (ClassNotFoundException e) {
            /* JAR may not be configured right or JDBC may not be working */
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            /* Catch all for errors I have not yet encountered */
            e.printStackTrace();
            return false;
        } finally{
            if (writer_connection != null)
                try { writer_connection.close(); }catch (Exception e){ /* Ignore this I guess! */}
        }
    }
}
