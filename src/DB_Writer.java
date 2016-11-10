import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Lucas on 11/4/2016.
 */
public class DB_Writer {

    private Connection writer_connection;   //Always use connection in shortest possible scope
    static private String db_username;
    static private String db_password;
    static private String url;

    public DB_Writer(){
        this.db_username = "root";
        this.db_password = "mysql";
        //this.url = "jdbc:mysql://google/time_management_system?cloudSqlInstance=tmtproject-148101:us-central1:timemanagementsystem&socketFactory=com.google.cloud.sql.mysql.SocketFactory";
        this.url = "jdbc:mysql://localhost:3306/time_management_system";
    }

    public void clockInUser(int employeeNumber, int taskID){
        long currentMilliTime = System.currentTimeMillis();
        Time clockInTime = new Time(currentMilliTime);
        Date clockInDate = new Date(currentMilliTime);
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            Connection writer_connection = DriverManager.getConnection(url, db_username, db_password);
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

    public void clockOutUser(int employeeNumber, int taskID) {
        Time clockOutTime = new Time(System.currentTimeMillis());
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            Connection writer_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepare query to database */
            /* Could result in only one time log for a given task */
            String updateQuery = "UPDATE time_logs SET TimeOut = ? WHERE EmployeeID = ? AND TaskID = ?";
            PreparedStatement updateStatement = writer_connection.prepareStatement(updateQuery);
            updateStatement.setTime(1, clockOutTime);
            updateStatement.setInt(2, employeeNumber);
            updateStatement.setInt(3, taskID);

            /* Execute statement */
            updateStatement.executeUpdate();

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

    public void addEmployee(String firstName, String lastName, Date hireDate, int managerStatus/*, List<String> assignedProjects*/){
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            Connection writer_connection = DriverManager.getConnection(url, db_username, db_password);
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

    public void addTask(String newTaskName, String projectSelected){
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            Connection writer_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepare query to database */
            String insertQuery = "INSERT INTO tasks(TaskName) VALUES (?)";
            PreparedStatement insertStatement = writer_connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, newTaskName);

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

    public void setWorkStatus(int employeeNumber, int newStatus){
        try {
            /* Boiler plate to create class and establish connection */
            Class.forName("com.mysql.jdbc.Driver");
            Connection writer_connection = DriverManager.getConnection(url, db_username, db_password);
            /* Boiler plate to create class and establish connection */

            /* Prepare query to database */
            String updateQuery = "UPDATE employee SET WorkStatus = ? WHERE EmployeeID = ?";
            PreparedStatement updateStatement = writer_connection.prepareStatement(updateQuery);
            updateStatement.setInt(1,newStatus);
            updateStatement.setInt(2, employeeNumber);

            /* Execute statement */
            updateStatement.executeUpdate();

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

}
