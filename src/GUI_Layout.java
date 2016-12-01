import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Hugo on 11/3/2016.
 */
public class GUI_Layout extends JFrame{

    /* Global private variables */
    private EmployeeProjectTaskMap PROJECT_TASK_MAPPING;     /* Maps project-task relationship */
    private Timer currentClockTimer;    /* Reference to JLabel current serving as proxy clock */
    private String clockedInOrOut;  /* Stores name of most recently used Clock-In/Clock-Out screen */
    private LoginUserData LOGIN_HANDLER; /* Handles new user login and contains information regarding projects/tasks */
    private int EMPLOYEE_RANK;  /* Determines what rank a user is */
    private int EMPLOYEE_ID; /* Stores the employee identifer used by SQL database to track current user */
    private boolean TIME_SHEET_FLAG = false;


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
        setSize(400,300);
        setResizable(false);
        setVisible(true);

        /* Create and bind action listeners to respective buttons */

        /* LoginScreen */
        loginLoginButton.addActionListener(new LoginButtonListener());


        loginNewEmployeeButton.addActionListener(new LoginNewUser());

        newUserCreateLoginButton.addActionListener(new NewUserLogin());

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
    public void setClockInDropdowns(){
        /* Gathers list of all projects/tasks available to current user */
        ArrayList<EmployeeProject> projDropDown = PROJECT_TASK_MAPPING.getProjects();
        ArrayList<EmployeeTask> taskDropDown = PROJECT_TASK_MAPPING.getProjectTasks(
                projDropDown.get(0).getProjectName());

        /* Assigns developer/manager GUI components to proxies in order to reduce code duplication */
        JComboBox projectProxy = null;
        JComboBox taskProxy = null;
        if(EMPLOYEE_RANK == 1){
            projectProxy = clockInManProjectSelector;
            taskProxy = clockInManTaskSelector; }
        else{
            projectProxy = clockInDevProjectSelector;
            taskProxy = clockInDevTaskSelector; }

        /* Removes all items from ComboBoxes and refills them with appropriate values */
        projectProxy.removeAllItems();
        for(EmployeeProject proj: projDropDown)
            projectProxy.addItem(proj.getProjectName());

        taskProxy.removeAllItems();
        for(EmployeeTask task: taskDropDown)
            taskProxy.addItem(task.getTaskName());

        /* Creates action listener that changes the tasks available for selection according to the project
        * selected by the user. Assures project/task selected have appropriate relationship */
        MatchProjectToTask projectSelectionListener = new MatchProjectToTask();
        projectProxy.addActionListener(projectSelectionListener);
    }

    /**
     * Helps prepare the Developer or Manager Clock-Out screen by properly labeling the GUI
     * screen with the current project and task being worked on by the user.
     *
     * @param projSel   the name of the current project the user is working on
     * @param taskSel   the name of the current task the user is working on
     */
    public void prepClockOutScreen(String projSel, String taskSel){
        if (EMPLOYEE_RANK == 0) {
            clockOutDevCurrentProject.setText(projSel);
            clockOutDevCurrentTask.setText(taskSel);
        }else if(EMPLOYEE_RANK == 1){
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
        ArrayList<EmployeeProject> projDropDown = PROJECT_TASK_MAPPING.getProjects();
        ArrayList<EmployeeTask> taskDropDown = PROJECT_TASK_MAPPING.getProjectTasks(
                projDropDown.get(0).getProjectName());

        JComboBox projectProxy = null;
        JComboBox taskProxy = null;
        JComboBox intProxy = null;
        if(EMPLOYEE_RANK == 0){
            projectProxy = timeSheetDevProjectSelector;
            taskProxy = timeSheetDevTaskSelector;
            intProxy = timeSheetDevIntervalSelector;
        }else if(EMPLOYEE_RANK == 1){
            projectProxy = timeSheetManProjectSelector;
            taskProxy = timeSheetManTaskSelector;
            intProxy = timeSheetManIntervalSelector;

            timeSheetManEmployeeSelector.removeAllItems();
            for(Employee e: LOGIN_HANDLER.getAllEmployees())
                timeSheetManEmployeeSelector.addItem(e.getName());
            timeSheetManEmployeeSelector.addItem("All Employees");
        }

        projectProxy.removeAllItems();
        taskProxy.removeAllItems();
        if (projectProxy.getItemCount() == 0) {
            for(EmployeeProject proj: projDropDown)
                projectProxy.addItem(proj.getProjectName());
            projectProxy.addItem("All Projects");
        }

        if (taskProxy.getItemCount() == 0) {
            for (EmployeeTask task : taskDropDown)
                taskProxy.addItem(task.getTaskName());
            taskProxy.addItem("All Tasks");
        }
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

                            for (EmployeeTask t : PROJECT_TASK_MAPPING.getProjectTasks(projectSelected))
                                timeSheetDevTaskSelector.addItem(t.getTaskName());

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

                            for (EmployeeTask t : PROJECT_TASK_MAPPING.getProjectTasks(projectSelected))
                                timeSheetManTaskSelector.addItem(t.getTaskName());

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

        this.TIME_SHEET_FLAG = true;
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
            LoginUserData loginObject = new LoginUserData();
            int [] authenticationResult = loginObject.authenticate(loginUsername.getText(),
                    String.valueOf(loginPassword.getPassword()));

            GUI_Logic_Login loginHandler = new GUI_Logic_Login();

            if(loginHandler.acceptLogin(authenticationResult)) {
                LOGIN_HANDLER = loginObject;
                EMPLOYEE_ID = authenticationResult[0];
                EMPLOYEE_RANK = authenticationResult[2];
                PROJECT_TASK_MAPPING = loginObject.gatherMappingInformation(
                        authenticationResult[0]);

                CardLayout layout = (CardLayout) (cardStack.getLayout());
                JLabel timeLabelList [] = {clockinDevTimer, clockInManTimer,
                        clockOutDevClock, clockOutManTimer};
                JLabel timeLabel = loginHandler.determineTimeLabel(timeLabelList);

                String screenNameList [] = {"ClockInDev", "ClockInMan",
                        "ClockOutDev", "ClockOutMan"};
                String nextScreenName = loginHandler.determineNextScreenName(screenNameList);

                layout.show(cardStack, nextScreenName);
                clockedInOrOut = nextScreenName;
                setClock(timeLabel);

                /* Clock-In Screen Check */
                if (authenticationResult[1] == 0) {
                    if(authenticationResult[2] != 0)
                        setSize(400,330); /* Clocked-In Manager */

                    setClockInDropdowns();
                } else {
                    int[] taskProjectIDNumbers = LOGIN_HANDLER.taskAndProjectIDs();
                    prepClockOutScreen(PROJECT_TASK_MAPPING.getProjectName(
                            taskProjectIDNumbers[1]),
                            PROJECT_TASK_MAPPING.getTaskName(taskProjectIDNumbers[0]));

                    if(authenticationResult[2] == 0)
                        setSize(400, 250);
                    else
                        setSize(400, 320);
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
            String projectSelected = "";
            String taskSelected = "";
            JLabel clockLabel = null;
            String panelName = "";

            if(e.getSource() == clockInDevClockInButton){
                projectSelected = (String) clockInDevProjectSelector.getSelectedItem();
                taskSelected = (String) clockInDevTaskSelector.getSelectedItem();
                clockLabel = clockOutDevClock;
                panelName = "ClockOutDev";
            }else if(e.getSource() == clockInManClockInButton){
                projectSelected = (String) clockInManProjectSelector.getSelectedItem();
                taskSelected = (String) clockInManTaskSelector.getSelectedItem();
                clockLabel = clockOutManTimer;
                panelName = "ClockOutMan";
                setSize(400, 350);
            }

            ClockInUser clockInUser = new ClockInUser(EMPLOYEE_ID, PROJECT_TASK_MAPPING.getTaskID(taskSelected));
            boolean result = clockInUser.clockIn();
            if(result) {
                destroyClock();
                setClock(clockLabel);
                prepClockOutScreen(projectSelected, taskSelected);
                setClock(clockLabel);
                layout.show(cardStack, panelName);
                clockedInOrOut = panelName;
            }else
                JOptionPane.showMessageDialog(null, "Connectivity Error, please " +
                        "try again!");
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
            String taskSelected = "";
            String screeName = "";

            destroyClock();
            if(e.getSource() == clockOutDevClockOutButton){
                taskSelected = clockOutDevCurrentTask.getText();
                setClock(clockinDevTimer);
                screeName = "ClockInDev";
            }else if(e.getSource() == clockOutManClockOutButton){
                taskSelected = clockOutManCurrentTask.getText();
                setClock(clockInManTimer);
                screeName = "ClockInMan";
            }

            ClockOutUser clockOutUser = new ClockOutUser(EMPLOYEE_ID, PROJECT_TASK_MAPPING.getTaskID(taskSelected));
            boolean result = clockOutUser.clockOut();

            if (result) {
                setClockInDropdowns();
                layout.show(cardStack, screeName);
                clockedInOrOut = screeName;
            } else
                JOptionPane.showMessageDialog(null, "Connectivity Error, please " +
                        "try again!");
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
                layout.show(cardStack, "timeSheetDev");
                setSize(400,300);
            } else if( source == clockInManTimeSheetsButton || source == clockOutManTimeSheetsButton){
                layout.show(cardStack, "timeSheetMan");
                setSize(400,400);
            }

            if(!TIME_SHEET_FLAG)
                prepTimeSheet();
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
            if(LOGIN_HANDLER == null) {
                layout.show(cardStack, "Login");
                setSize(400,250);
            }else if (EMPLOYEE_RANK == 0) {
                layout.show(cardStack, clockedInOrOut);
                setSize(400,300);
            } else if( EMPLOYEE_RANK == 1){
                layout.show(cardStack,clockedInOrOut);
                setSize(400,330);
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
            if(EMPLOYEE_RANK == 0){
                projectProxy = timeSheetDevProjectSelector;
                taskProxy = timeSheetDevTaskSelector;
                intProxy = timeSheetDevIntervalSelector;
                empProxy = EMPLOYEE_ID;
                rank = "Developer";
            }else if(EMPLOYEE_RANK == 1){
                projectProxy = timeSheetManProjectSelector;
                taskProxy = timeSheetManTaskSelector;
                intProxy = timeSheetManIntervalSelector;

                String employeeSelected = (String) timeSheetManEmployeeSelector.getSelectedItem();

                if(employeeSelected.equals("All Employees"))
                    empProxy = 0;
                else
                    empProxy = LOGIN_HANDLER.getEmployeeNumberByName(employeeSelected);

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
                projectOption = PROJECT_TASK_MAPPING.getProjectID(projectSelected);

            if(taskSelected.equals("All Tasks"))
                taskOption = 0;
            else
                taskOption = PROJECT_TASK_MAPPING.getTaskID(taskSelected);


            GenTimeSheet genHelper = new GenTimeSheet(rank, empProxy, projectOption, taskOption, timeIntervalSelected);

            genHelper.createReport();
        }
    }

    private class SystemButton implements ActionListener{

        /**
         * Navigates to the Manager-only system panel.
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            systemManAddTask.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    String tabName = systemManAddTask.getSelectedComponent().getName();
                    if(tabName.equals("AssignTask"))
                        setSize(350,400);
                    else if(tabName.equals("AddProject"))
                        setSize(350,300);
                    else if(tabName.equals("AddTask"))
                        setSize(350,350);
                    else if(tabName.equals("AddEmployee"))
                        setSize(350,400);
                }
            });

            for(EmployeeProject p: LOGIN_HANDLER.getAllProjects())
                systemManATProjectSelector.addItem(p.getProjectName());

            systemManATAddTask.addActionListener(new AddTaskListener());
            systemManAPAddProject.addActionListener(new AddEmployeeListener());
            systemManCPCreateProject.addActionListener(new AddProjectListener());
            systemManAsTGenButton.addActionListener(new AssignTaskGenerateButtonListener());
            systemManAstAssignButton.addActionListener(new AssignTaskListener());
            systemManAsTEmployeeSelector.addActionListener(new AssignTaskEmployeeListener());

            CardLayout layout = (CardLayout) (cardStack.getLayout());
            layout.show(cardStack, "systemMan");
            setSize(350,400);
        }
    }

    private class AddTaskListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            if(systemManATTaskName.getText().equals(""))
                JOptionPane.showMessageDialog(null, "Please enter a valid task name");
            else{
                try {
                    String projectSelected = (String) systemManATProjectSelector.getSelectedItem();
                    int hoursSelected = Integer.parseInt(systemManATEstimateTime.getText());
                    String taskSelected = systemManATTaskName.getText();

                    AddTask atObj = new AddTask(projectSelected, taskSelected, hoursSelected);
                    boolean result = atObj.addTaskToDatabase();

                    if(result)
                        JOptionPane.showMessageDialog(null, "Addition Successful");
                    else
                        JOptionPane.showMessageDialog(null, "Error: Please try again!");

                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number");
                }
            }
        }
    }

    private class AddEmployeeListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String firstNameEntered = systemManFirstNameTextField.getText();
            String lastNameEntered = systemManLastNameTextField.getText();
            String startDateEntered = systemManHireDateTextField.getText();
            int groupSelected = systemManGroupSelector.getSelectedIndex();

            AddEmployee adObj = new AddEmployee(firstNameEntered, lastNameEntered,
                    startDateEntered, groupSelected);
            int result = adObj.addEmployeeToDatabase();

            if(result == -1)
                JOptionPane.showMessageDialog(null, "Invalid date entry, please try again");
            else if(result == 0)
                JOptionPane.showMessageDialog(null, "Error: Please try again!");
            else if(result == 1)
                JOptionPane.showMessageDialog(null, "Addition Successful!");
        }
    }

    private class AddProjectListener implements ActionListener{

        /**
         * Triggered when user presses the add new project button. Extarcts data from
         * saved JTextField and passes information to the database.
         *
         * @param e     Action which triggered method call
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(systemManCPProjectName.getText().equals(""))
                JOptionPane.showMessageDialog(null, "Please enter a valid project name");
            else{
                String newProjectName = systemManCPProjectName.getText();
                AddProject apObj = new AddProject(newProjectName);

                boolean result = apObj.addProjectToDatabase();
                if(result)
                    JOptionPane.showMessageDialog(null, "Addition Successful");
                else
                    JOptionPane.showMessageDialog(null, "Error: Please try again!");
            }
        }
    }

    private class AssignTaskGenerateButtonListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            AssignTask atObj = new AssignTask();
            ArrayList<Employee> employeeList = atObj.employeeList();
            ArrayList<EmployeeTask> taskList = atObj.buildTaskList(
                    employeeList.get(0).getEmployeeNumber());

            systemManAsTEmployeeSelector.removeAllItems();
            for(Employee emp: employeeList)
                systemManAsTEmployeeSelector.addItem(emp.getName());

            systemManAsTTaskSelector.removeAllItems();
            for(EmployeeTask task : taskList)
                systemManAsTTaskSelector.addItem(task.getTaskName());
        }
    }

    private class AssignTaskListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String employeeSelected = (String) systemManAsTEmployeeSelector.
                    getSelectedItem();
            String taskSelected = (String) systemManAsTTaskSelector.
                    getSelectedItem();
            AssignTask atObj = new AssignTask(employeeSelected, taskSelected);
            boolean result = atObj.addAssignmentToDatabase();

            if(result)
                JOptionPane.showMessageDialog(null, "Addition Successful");
            else
                JOptionPane.showMessageDialog(null, "Error: Please try again!");
        }
    }

    private class AssignTaskEmployeeListener implements ActionListener{
        /**
         * Extracts the employee selected and determines their ID. Using that information
         * the listener then repopulates the task dropdown in order for it to only show
         * tasks that the employee is not assigned to.
         *
         * @param e     Action which triggered method call
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String employeeName = (String) systemManAsTEmployeeSelector
                    .getSelectedItem();

            AssignTask atObj = new AssignTask();
            ArrayList<EmployeeTask> tskList = atObj.dropDownTaskList(employeeName);

            if(tskList.size() == 0)
                JOptionPane.showMessageDialog(null,
                        "Error: No Task to Assign. Selected another employee.");

            systemManAsTTaskSelector.removeAllItems();
            for(EmployeeTask tsk: tskList)
                systemManAsTTaskSelector.addItem(tsk.getTaskName());
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

    private class MatchProjectToTask implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String projectSelected;
            JComboBox componentProxy;

            if(e.getSource() == clockInDevProjectSelector) {
                projectSelected = (String) clockInDevProjectSelector.getSelectedItem();
                componentProxy = clockInDevTaskSelector;
            }else{
                projectSelected = (String) clockInManProjectSelector.getSelectedItem();
                componentProxy = clockInManTaskSelector;
            }

            componentProxy.removeAllItems();
            for(EmployeeTask task: PROJECT_TASK_MAPPING.getProjectTasks(projectSelected))
                componentProxy.addItem(task.getTaskName());
        }
    }

    private class NewUserLogin implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String firstName = newUserFirstName.getText();
            String lastName = newUserLastName.getText();
            String dateHired = newUserDateHired.getText();
            String userName = newUserUsernameField.getText();
            String password = String.valueOf(newUserPasswordField.getPassword());

            if(firstName.equals("") || lastName.equals("") || dateHired.equals("")
                    || userName.equals("") || password.equals(""))
                JOptionPane.showMessageDialog(null, "Please enter all the required information");
            else {
                CreateNewUser cnuObj = new CreateNewUser(firstName, lastName,
                        dateHired, userName, password);

                boolean result = cnuObj.createNewLogin();
                if(!result)
                    JOptionPane.showMessageDialog(null, "Error: Please try again!");
            }
        }
    }
}
