import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel;

/**
 * Created by Hugo on 11/3/2016.
 */
public class GUI_Layout extends JFrame{

    /* Global private variables */
    private EmployeeProjectTaskMap map;     /* Maps project-task relationship */
    private Timer currentClockTimer;    /* Reference to JLabel current serving as proxy clock */
    private String clockedInOrOut;  /* Stores name of most recently used Clock-In/Clock-Out screen */
    private userLogIn logInObj; /* Handles new user login and contains information regarding projects/tasks */
    private int managerStatus;  /* Determines what rank a user is */
    private int employeeID; /* Stores the employee identifer used by SQL database to track current user */


    /* IntelliJ generated GUI components */
    private JPanel cardStack; /* Main JPanel that contains all other GUI pages*/
    private JTextField loginUsername; /* JTextField which allows user to supply a username */
    private JPasswordField loginPassword; /* JPasswordField that allows user to supply their login password */
    private JButton loginLoginButton; /* JButton that triggers login sequence */
    private JPanel Login; /* Parent panel containing all login components */
    private JPanel ClockinDev; /* Parent panel containing all ClockInDev components */
    private JComboBox clockInDevProjectSelector; /* Allows developer users to select project to work on */
    private JComboBox clockInDevTaskSelector; /* Allows developer users to select a task to work on */
    private JButton clockInDevClockInButton; /* Allows developer user to trigger clock in actions */
    private JButton clockInDevTimeSheetButton; /* Allows developer users to navigate to developer timesheet panel */
    private JPanel ClockoutDev; /* Parent panel containing all ClockoutDev components */
    private JButton clockOutDevTimeSheetButton; /* Allows developer users to navigate to developer timesheet panel */
    private JButton clockOutDevClockOutButton; /* Allows developer users to trigger clock out actions */
    private JTextArea clockOutDevCommentSection; /* Allows for the entry of unsaved comments */
    private JLabel clockinDevTimer; /* JLabel that is updated every second with the current time to simulate a digital clock */
    private JLabel clockOutDevCurrentProject; /* Displays the current project a developer is working on */
    private JLabel clockOutDevCurrentTask; /* Displays the current task a developer is working on */
    private JLabel clockOutDevClock; /* JLabel that is updated every second with the current time to simulate a digital clock */
    private JPanel TimesheetDev;
    private JLabel timeSheetLabel;
    private JComboBox timeSheetDevProjectSelector;
    private JComboBox timeSheetDevTaskSelector;
    private JComboBox timeSheetDevIntervalSelector;
    private JButton timeSheetDevGenerateButton;
    private JButton timeSheetDevBackButton;
    private JPanel ClockinMan;
    private JLabel clockInManTimer;
    private JComboBox clockInManProjectSelector;
    private JComboBox clockInManTaskSelector;
    private JButton clockInManClockInButton;
    private JButton clockInManTimeSheetsButton;
    private JButton clockInManSystemButton;
    private JPanel ClockoutMan;
    private JLabel clockOutManTimer;
    private JTextArea textArea2;
    private JButton clockOutManClockOutButton;
    private JButton clockOutManTimeSheetsButton;
    private JButton clockOutManSystemButton;
    private JLabel clockOutManCurrentProject;
    private JLabel clockOutManCurrentTask;
    private JPanel TimesheetMan;
    private JComboBox timeSheetManEmployeeSelector;
    private JComboBox timeSheetManProjectSelector;
    private JComboBox timeSheetManTaskSelector;
    private JComboBox timeSheetManIntervalSelector;
    private JButton timeSheetManGenerateTimeSheetButton;
    private JButton timeSheetManBackButton;
    private JPanel SystemMan;
    private JTabbedPane systemManAddTask;
    private JPanel systemManAddEmployee;
    private JPanel systemManCreateTask;
    private JPanel systemManAddProject;
    private JTextField systemManLastNameTextField;
    private JTextField systemManFirstNameTextField;
    private JTextField systemManHireDateTextField;
    private JComboBox systemManGroupSelector;
    private JButton systemManAPAddProject;
    private JButton systemManBackButton;
    private JComboBox systemManATProjectSelector;
    private JTextField systemManATTaskName;
    private JButton systemManATAddTask;
    private JButton systemManATBackButton;
    private JTextField systemManCPProjectName;
    private JButton systemManCPCreateProject;
    private JButton loginNewEmployeeButton;
    private JPanel NewUser;
    private JTextField newUserFirstName;
    private JTextField newUserLastName;
    private JTextField newUserDateHired;
    private JTextField newUserUsernameField;
    private JPasswordField newUserPasswordField;
    private JButton newUserCreateLoginButton;
    private JButton newUserBackButton;
    private JPanel systemManBindTask;
    private JButton systemManBTGen;
    private JComboBox systemManBTTasksSel;
    private JComboBox systemManBTProjSel;
    private JButton systemManBTBind;
    private JButton systemManCPBack;
    private JButton systemManBTBack;
    private JButton timeSheetManProgressReportButton;
    private JTextField systemManATEstimateTime;
    private JPanel systemManAssignTask;
    private JButton systemManAsTGenButton;
    private JComboBox systemManAsTTaskSelector;
    private JComboBox systemManAsTEmployeeSelector;
    private JButton systemManAstAssignButton;

    /**
     * Constructor for main class that holds all GUI components. Attaches listeners to buttons
     * and sets default dimensions and behavior for main GUI screen.
     */
    public GUI_Layout(){

        /* Set defualt properties for GUI window */
        setContentPane(cardStack);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setSize(400,250);
        setResizable(false);
        setVisible(true);

        /* Create and bind action listeners to respective buttons */
        loginLoginButton.addActionListener(new LoginButtonListener());
        loginNewEmployeeButton.addActionListener(new LoginNewUser());

        newUserCreateLoginButton.addActionListener(new CreateNewUser(newUserFirstName, newUserLastName,
                newUserDateHired, newUserUsernameField, newUserPasswordField));

        ClockInButtonListener clockin_listener = new ClockInButtonListener();
        clockInDevClockInButton.addActionListener(clockin_listener);
        clockInManClockInButton.addActionListener(clockin_listener);

        ClockOutButtonListener clockout_listener = new ClockOutButtonListener();
        clockOutDevClockOutButton.addActionListener(clockout_listener);
        clockOutManClockOutButton.addActionListener(clockout_listener);

        TimeSheetButton timesheet_listener = new TimeSheetButton();
        clockInDevTimeSheetButton.addActionListener(timesheet_listener);
        clockOutDevTimeSheetButton.addActionListener(timesheet_listener);
        clockInManTimeSheetsButton.addActionListener(timesheet_listener);
        clockOutManTimeSheetsButton.addActionListener(timesheet_listener);

        BackButton back_listener = new BackButton();
        timeSheetDevBackButton.addActionListener(back_listener);
        timeSheetManBackButton.addActionListener(back_listener);
        systemManBackButton.addActionListener(back_listener);
        systemManATBackButton.addActionListener(back_listener);
        systemManCPBack.addActionListener(back_listener);
        systemManBTBack.addActionListener(back_listener);
        newUserBackButton.addActionListener(back_listener);

        GenTimeSheetButton timeSheetButton_listener = new GenTimeSheetButton();
        timeSheetDevGenerateButton.addActionListener(timeSheetButton_listener);
        timeSheetManGenerateTimeSheetButton.addActionListener(timeSheetButton_listener);

        SystemButton systemButton_listener = new SystemButton();
        clockInManSystemButton.addActionListener(systemButton_listener);
        clockOutManSystemButton.addActionListener(systemButton_listener);

        ProgressReport reportButton_listener = new ProgressReport();
        timeSheetManProgressReportButton.addActionListener(reportButton_listener);
    }

    /**
     * Depending on the rank of the employee, method helps prepare the Manager Clock-In Screen or the Developer
     * Clock-In Screen by populating the dropdown menus present in both screens. Method also attaches listeners
     * to the Project Selector in order to display only Tasks associated with the user selected Project.
     */
    public void setClockIDropdowns(){
        /* Gathers list of all projects/tasks available to current user */
        ArrayList<String> projDropDown = logInObj.projectList(map);
        ArrayList<String> taskDropDown = logInObj.tasksInProject(projDropDown.get(0), map);

        /* Assigns developer/manager GUI components to proxies in order to reduce code duplication */
        JComboBox projectProxy = null;
        JComboBox taskProxy = null;
        if(managerStatus == 1){ projectProxy = clockInManProjectSelector; taskProxy = clockInManTaskSelector; }
        else{ projectProxy = clockInDevProjectSelector; taskProxy = clockInDevTaskSelector; }

        /* Removes all items from ComboBoxes and refills them with appropriate values */
        projectProxy.removeAllItems();
        for(String proj: projDropDown)
            projectProxy.addItem(proj);
        taskProxy.removeAllItems();
        for(String task: taskDropDown)
            taskProxy.addItem(task);

        /* Creates action listener that changes the tasks available for selection according to the project
        * selected by the user. Assures project/task selected have appropriate relationship */
        ActionListener clockInProjSelectorListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == clockInDevProjectSelector) {
                    String projectSelected = (String) clockInDevProjectSelector.getSelectedItem();
                    clockInDevTaskSelector.removeAllItems();

                    for(String taskName: logInObj.tasksInProject(projectSelected, map))
                        clockInDevTaskSelector.addItem(taskName);
                }else{
                    String projectSelected = (String) clockInManProjectSelector.getSelectedItem();
                    clockInManTaskSelector.removeAllItems();

                    for(String taskName: logInObj.tasksInProject(projectSelected, map))
                        clockInManTaskSelector.addItem(taskName);
                }
            }
        };

        projectProxy.addActionListener(clockInProjSelectorListener);
    }

    /**
     * Helps prepare the Developer or Manager Clock-Out screen by properly labeling the GUI
     * screen with the current project and task being worked on by the user.
     *
     * @param projSel   the name of the current project the user is working on
     * @param taskSel   the name of the current task the user is working on
     */
    public void prepClockOutScreen(String projSel, String taskSel){
        if (managerStatus == 0) {
            clockOutDevCurrentProject.setText(projSel);
            clockOutDevCurrentTask.setText(taskSel);
        }else if(managerStatus == 1){
            clockOutManCurrentProject.setText(projSel);
            clockOutManCurrentTask.setText(taskSel);
        }
    }

    /**
     * Adds an ActionListner to the JLabel provided. This ActionListener then updates the text in the JLabel
     * periodically in order to simulate a digital clock. Stores a reference to the proxy clock in a global
     * variable to make destruction of clock easier.
     *
     * @param clockToSet    JLabel object to use as a proxy clock
     */
    public void setClock(JLabel clockToSet){
        currentClockTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat clock = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm z");
                clockToSet.setText(clock.format(new Date()));
            }
        });

        currentClockTimer.start();
    }

    /**
     * Destroys the current proxy clock by removing the ActionListener attached to it.
     */
    public void destroyClock(){
        for(ActionListener al: currentClockTimer.getActionListeners())
            currentClockTimer.removeActionListener(al);
    }

    /**
     * Prepares the Manager and Developer Timesheet pages by populating the drop down menus in
     * each as well as attaching listeners to the selectors.
     */
    public void prepTimeSheet(){
        ArrayList<String> projDropDown = logInObj.projectList(map);
        ArrayList<String> taskDropDown = logInObj.tasksInProject(projDropDown.get(0), map);

        JComboBox projectProxy = null;
        JComboBox taskProxy = null;
        JComboBox intProxy = null;
        if(managerStatus == 0){
            projectProxy = timeSheetDevProjectSelector;
            taskProxy = timeSheetDevTaskSelector;
            intProxy = timeSheetDevIntervalSelector;
        }else if(managerStatus == 1){
            projectProxy = timeSheetManProjectSelector;
            taskProxy = timeSheetManTaskSelector;
            intProxy = timeSheetManIntervalSelector;

            for(Employee e: logInObj.getAllEmployees())
                timeSheetManEmployeeSelector.addItem(e.getName());
            timeSheetManEmployeeSelector.addItem("All Employees");
        }

        projectProxy.removeAllItems();
        taskProxy.removeAllItems();
        for(String proj: projDropDown)
            projectProxy.addItem(proj);

        for(String task: taskDropDown)
            taskProxy.addItem(task);

        projectProxy.addItem("All Projects");
        taskProxy.addItem("All Tasks");
        ActionListener timeSheetDevProjSelectorListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == timeSheetDevProjectSelector) {
                    String projectSelected = (String) timeSheetDevProjectSelector.getSelectedItem();

                    if (projectSelected!=null) {
                        if(projectSelected.equals("All Projects")){
                            timeSheetDevTaskSelector.removeAllItems();
                            timeSheetDevTaskSelector.addItem("All Tasks");
                        }else{
                            timeSheetDevTaskSelector.removeAllItems();

                            for (String taskName : logInObj.tasksInProject(projectSelected, map))
                                timeSheetDevTaskSelector.addItem(taskName);

                            timeSheetDevTaskSelector.addItem("All Tasks");
                        }
                    }
                }else if(e.getSource() == timeSheetManProjectSelector){
                    String projectSelected = (String) timeSheetManProjectSelector.getSelectedItem();

                    if (projectSelected != null) {
                        if(projectSelected.equals("All Projects")){
                            timeSheetManTaskSelector.removeAllItems();
                            timeSheetManTaskSelector.addItem("All Tasks");
                        }else{
                            timeSheetManTaskSelector.removeAllItems();

                            for (String taskName : logInObj.tasksInProject(projectSelected, map))
                                timeSheetManTaskSelector.addItem(taskName);

                            timeSheetManTaskSelector.addItem("All Tasks");
                        }
                    }
                }
            }
        };
        projectProxy.addActionListener(timeSheetDevProjSelectorListener);

        intProxy.removeAllItems();
        intProxy.addItem("Current Week");
        intProxy.addItem("Current Month");
        intProxy.addItem("Current Year");
    }


    /************************ ACTION LISTENERS BELOW *****************************************/


    private class LoginButtonListener implements ActionListener {

        /**
         * Method handles the log-in of a user as well as the display of the first screen the
         * user will encounter, be they developer or manager.
         * @param e     Action triggering call
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginLoginButton){
                logInObj = new userLogIn();
                logInObj.login(loginUsername.getText(), String.valueOf(loginPassword.getPassword()));
                int [] result = logInObj.results();

                if(result[0] == -2){
                    JOptionPane.showMessageDialog(null, "Unable to connect to CloudSQL!");
                }else if (result[0] == -1){
                    JOptionPane.showMessageDialog(null, "Invalid username and passowrd combination, please try again.");
                }else{
                    employeeID = result[0];
                    if(map == null)
                        map = logInObj.populateDropDown(result[0]);

                    managerStatus = result[2];
                    CardLayout layout = (CardLayout) (cardStack.getLayout());
                    if(result[1] == 0) {
                        if (result[2] == 0) {
                            setClock(clockinDevTimer);
                            setClockIDropdowns();
                            layout.show(cardStack, "ClockInDev");
                            clockedInOrOut = "ClockInDev";
                        }else{
                            setClock(clockInManTimer);
                            setClockIDropdowns();
                            setSize(400,300);
                            layout.show(cardStack, "ClockInMan");
                            clockedInOrOut = "ClockInMan";
                        }
                    }else{
                        if (result[2] == 0) {
                            setClock(clockOutDevClock);
                            int [] ids = logInObj.taskAndProjectIDs();
                            prepClockOutScreen(map.getProjectName(ids[1]), map.getTaskName(ids[0]));
                            layout.show(cardStack, "ClockOutDev");
                            clockedInOrOut = "ClockOutDev";
                        }else{
                            setClock(clockOutManTimer);
                            int [] ids = logInObj.taskAndProjectIDs();
                            prepClockOutScreen(map.getProjectName(ids[1]), map.getTaskName(ids[0]));
                            setSize(400,300);
                            layout.show(cardStack, "ClockOutMan");
                            clockedInOrOut = "ClockOutMan";
                        }
                    }
                }
            }
        }
    }

    private class ClockInButtonListener implements ActionListener{

        /**
         * Extracts text/selections from the dropdown menus in the Clock-In screen and passes that information
         * to userClockIn object in order to alter the database.
         *
         * @param e     action that triggers call
         */
        @Override
        public void actionPerformed(ActionEvent e){
            CardLayout layout = (CardLayout) (cardStack.getLayout());
            if(e.getSource() == clockInDevClockInButton){
                String projectSelected = (String) clockInDevProjectSelector.getSelectedItem();
                String taskSelected = (String) clockInDevTaskSelector.getSelectedItem();
                userClockIn clockInUser = new userClockIn(employeeID, map.getTaskID(taskSelected));
                clockInUser.clockIn();

                destroyClock();
                prepClockOutScreen(projectSelected, taskSelected);
                setClock(clockOutDevClock);
                layout.show(cardStack, "ClockOutDev");
                clockedInOrOut = "ClockOutDev";
            }else if(e.getSource() == clockInManClockInButton){
                String projectSelected = (String) clockInManProjectSelector.getSelectedItem();
                String taskSelected = (String) clockInManTaskSelector.getSelectedItem();
                userClockIn clockInUser = new userClockIn(employeeID, map.getTaskID(taskSelected));
                clockInUser.clockIn();

                destroyClock();
                prepClockOutScreen(projectSelected, taskSelected);
                setClock(clockOutManTimer);
                layout.show(cardStack, "ClockOutMan");
                clockedInOrOut = "ClockOutMan";
            }
        }
    }

    private class ClockOutButtonListener implements ActionListener{

        /**
         * Creates a userClockOut object to handle the altering of the database in order to clock a
         * user out.
         *
         * @param e     action that triggers call
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            CardLayout layout = (CardLayout) (cardStack.getLayout());
            if(e.getSource() == clockOutDevClockOutButton){
                String taskSelected = clockOutDevCurrentTask.getText();

                userClockOut clockOutUser = new userClockOut(employeeID, map.getTaskID(taskSelected));
                clockOutUser.clockOut();

                destroyClock();
                setClockIDropdowns();
                setClock(clockinDevTimer);
                layout.show(cardStack, "ClockInDev");
                clockedInOrOut = "ClockInDev";
            }else if(e.getSource() == clockOutManClockOutButton){
                String taskSelected = clockOutManCurrentTask.getText();

                userClockOut clockOutUser = new userClockOut(employeeID, map.getTaskID(taskSelected));
                clockOutUser.clockOut();

                destroyClock();
                setClockIDropdowns();
                setClock(clockInManTimer);
                layout.show(cardStack, "ClockInMan");
                clockedInOrOut = "ClockInMan";
            }
        }
    }

    private class TimeSheetButton implements ActionListener{

        /**
         * This button changes the current screen to the rank-appropriate Timesheet screen for both Developers
         * and Managers.
         *
         * @param e     action that triggered call
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            CardLayout layout = (CardLayout) (cardStack.getLayout());
            Object source = e.getSource();
            if (source == clockInDevTimeSheetButton || source== clockOutDevTimeSheetButton) {
                prepTimeSheet();
                layout.show(cardStack, "timeSheetDev");
                setSize(400,300);
            } else if( source == clockInManTimeSheetsButton || source == clockOutManTimeSheetsButton){
                prepTimeSheet();
                setSize(400,400);
                layout.show(cardStack, "timeSheetMan");
            }
        }
    }

    private class BackButton implements ActionListener{

        /**
         * Navigates to the previous screen the user was in.
         *
         * @param e     action that triggered call
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            CardLayout layout = (CardLayout) (cardStack.getLayout());
            Object source = e.getSource();
            if(logInObj == null) {
                layout.show(cardStack, "Login");
                setSize(400,250);
            }else if (managerStatus == 0) {
                layout.show(cardStack, clockedInOrOut);
                setSize(400,250);
            } else if( managerStatus == 1){
                layout.show(cardStack,clockedInOrOut);
                setSize(400,300);
            }
        }
    }

    private class GenTimeSheetButton implements ActionListener{

        /**
         * Extracts data from Timesheet screen and creates a GenTimeSheet object to handle the
         * database querry. Results in a pop-up with timesheet report if any results were found
         * and a failure message if no results were found.
         *
         * @param e     action triggered by generate time sheet button
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox projectProxy = null;
            JComboBox taskProxy = null;
            JComboBox intProxy = null;
            int empProxy = -1;
            String rank = null;
            if(managerStatus == 0){
                projectProxy = timeSheetDevProjectSelector;
                taskProxy = timeSheetDevTaskSelector;
                intProxy = timeSheetDevIntervalSelector;
                empProxy = employeeID;
                rank = "Developer";
            }else if(managerStatus == 1){
                projectProxy = timeSheetManProjectSelector;
                taskProxy = timeSheetManTaskSelector;
                intProxy = timeSheetManIntervalSelector;

                String employeeSelected = (String) timeSheetManEmployeeSelector.getSelectedItem();

                if(employeeSelected.equals("All Employees"))
                    empProxy = 0;
                else
                    empProxy = logInObj.getEmployeeNumberByName(employeeSelected);

                rank = "Manager";
            }

            String timeIntervalSelected = (String) intProxy.getSelectedItem();
            String projectSelected = (String) projectProxy.getSelectedItem();
            String taskSelected = (String) taskProxy.getSelectedItem();

            int projectOption, taskOption;
            if(projectSelected.equals("All Projects")) {
                projectOption = 0;
                taskOption = 0;
            } else
                projectOption = map.getProjectID(projectSelected);

            if(taskSelected.equals("All Tasks"))
                taskOption = 0;
            else
                taskOption = map.getTaskID(taskSelected);


            GenTimeSheet genHelper = new GenTimeSheet(rank, empProxy, projectOption, taskOption, timeIntervalSelected);

            StringBuilder genList = genHelper.createReport();
            if(genList != null)
                JOptionPane.showMessageDialog(null, genList.toString());
            else
                JOptionPane.showMessageDialog(null, "NO RESULTS FOUND");
        }
    }

    private class SystemButton implements ActionListener{

        /**
         * Navigates to the Manager-only system panel.
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            systemManATAddTask.addActionListener(new AddTask(systemManATProjectSelector, systemManATTaskName, systemManATEstimateTime, map.getProjects()));
            systemManAPAddProject.addActionListener(new AddEmployee(systemManFirstNameTextField, systemManLastNameTextField,
                    systemManHireDateTextField, systemManGroupSelector));
            systemManCPCreateProject.addActionListener(new AddProject(systemManCPProjectName));
            systemManBTBind.addActionListener(new BindTask(systemManBTGen, systemManBTTasksSel, systemManBTProjSel, map));
            systemManAsTGenButton.addActionListener(new AssignTask(systemManAsTEmployeeSelector, systemManAsTTaskSelector, systemManAstAssignButton));

            CardLayout layout = (CardLayout) (cardStack.getLayout());
            layout.show(cardStack, "systemMan");
            setSize(500,400);
        }
    }

    private class LoginNewUser implements ActionListener {

        /**
         * Changes current screen to the new user screen.
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            CardLayout layout = (CardLayout) (cardStack.getLayout());
            layout.show(cardStack, "newUser");
            setSize(400, 350);
        }
    }
}
