import javax.swing.*;
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
    private String clockedInOrOut;
    private userLogIn logInObj;
    private Timer currentClockTimer;
    private int employeeID;
    private EmployeeProjectTaskMap map;
    private int managerStatus;

    private JPanel cardStack;
    private JTextField loginUsername;
    private JPasswordField loginPassword;
    private JButton loginLoginButton;
    private JPanel Login;
    private JPanel ClockinDev;
    private JComboBox clockInDevProjectSelector;
    private JComboBox clockInDevTaskSelector;
    private JButton clockInDevClockInButton;
    private JButton clockInDevTimeSheetButton;
    private JPanel ClockoutDev;
    private JButton clockOutDevTimeSheetButton;
    private JButton clockOutDevClockOutButton;
    private JTextArea textArea1;
    private JLabel clockinDevTimer;
    private JLabel clockOutDevCurrentProject;
    private JLabel clockOutDevCurrentTask;
    private JLabel clockOutDevClock;
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
    private JButton generateAvialableTasksButton;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton bindButton;

    public GUI_Layout(){
        setContentPane(cardStack);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setSize(400,200);
        setResizable(false);
        setVisible(true);

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
        newUserBackButton.addActionListener(back_listener);

        GenTimeSheetButton timeSheetButton_listener = new GenTimeSheetButton();
        timeSheetDevGenerateButton.addActionListener(timeSheetButton_listener);
        timeSheetManGenerateTimeSheetButton.addActionListener(timeSheetButton_listener);

        SystemButton systemButton_listener = new SystemButton();
        clockInManSystemButton.addActionListener(systemButton_listener);
        clockOutManSystemButton.addActionListener(systemButton_listener);

    }

    public void setLoginDropdowns(){
        ArrayList<String> projDropDown = logInObj.projectList(map);;
        ArrayList<String> taskDropDown = logInObj.tasksInProject(projDropDown.get(0), map);
        JComboBox projectProxy = null;
        JComboBox taskProxy = null;

        if(managerStatus == 1){ projectProxy = clockInManProjectSelector; taskProxy = clockInManTaskSelector; }
        else{ projectProxy = clockInDevProjectSelector; taskProxy = clockInDevTaskSelector; }

        projectProxy.removeAllItems();
        for(String proj: projDropDown)
            projectProxy.addItem(proj);

        taskProxy.removeAllItems();
        for(String task: taskDropDown)
            taskProxy.addItem(task);

        ActionListener clockInDevProjSelectorListener = new ActionListener() {
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

        projectProxy.addActionListener(clockInDevProjSelectorListener);
    }

    public void prepClockOutScreen(String projSel, String taskSel){
        if (managerStatus == 0) {
            clockOutDevCurrentProject.setText(projSel);
            clockOutDevCurrentTask.setText(taskSel);
        }else if(managerStatus == 1){
            clockOutManCurrentProject.setText(projSel);
            clockOutManCurrentTask.setText(taskSel);
        }
    }

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

    public void destroyClock(){
        for(ActionListener al: currentClockTimer.getActionListeners())
            currentClockTimer.removeActionListener(al);
    }

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
                            setLoginDropdowns();
                            layout.show(cardStack, "ClockInDev");
                            clockedInOrOut = "ClockInDev";
                        }else{
                            setClock(clockInManTimer);
                            setLoginDropdowns();
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

        @Override
        public void actionPerformed(ActionEvent e) {
            CardLayout layout = (CardLayout) (cardStack.getLayout());
            if(e.getSource() == clockOutDevClockOutButton){
                String taskSelected = clockOutDevCurrentTask.getText();

                userClockOut clockOutUser = new userClockOut(employeeID, map.getTaskID(taskSelected));
                clockOutUser.clockOut();

                destroyClock();
                setLoginDropdowns();
                setClock(clockinDevTimer);
                layout.show(cardStack, "ClockInDev");
                clockedInOrOut = "ClockInDev";
            }else if(e.getSource() == clockOutManClockOutButton){
                String taskSelected = clockOutManCurrentTask.getText();

                userClockOut clockOutUser = new userClockOut(employeeID, map.getTaskID(taskSelected));
                clockOutUser.clockOut();

                destroyClock();
                setLoginDropdowns();
                setClock(clockInManTimer);
                layout.show(cardStack, "ClockInMan");
                clockedInOrOut = "ClockInMan";
            }
        }
    }

    private class TimeSheetButton implements ActionListener{

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

        @Override
        public void actionPerformed(ActionEvent e) {
            CardLayout layout = (CardLayout) (cardStack.getLayout());
            Object source = e.getSource();
            if(logInObj == null) {
                layout.show(cardStack, "Login");
                setSize(400,200);
            }else if (managerStatus == 0) {
                layout.show(cardStack, clockedInOrOut);
                setSize(400,200);
            } else if( managerStatus == 1){
                layout.show(cardStack,clockedInOrOut);
                setSize(400,300);
            }
        }
    }

    private class GenTimeSheetButton implements ActionListener{

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

        @Override
        public void actionPerformed(ActionEvent e) {

            systemManATAddTask.addActionListener(new AddTask(systemManATProjectSelector, systemManATTaskName, map.getProjects()));
            systemManAPAddProject.addActionListener(new AddEmployee(systemManFirstNameTextField, systemManLastNameTextField,
                    systemManHireDateTextField, systemManGroupSelector));
            systemManCPCreateProject.addActionListener(new AddProject(systemManCPProjectName));

            CardLayout layout = (CardLayout) (cardStack.getLayout());
            layout.show(cardStack, "systemMan");
            setSize(400,400);
        }
    }

    private class LoginNewUser implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            CardLayout layout = (CardLayout) (cardStack.getLayout());
            layout.show(cardStack, "newUser");
            setSize(400, 350);
        }
    }
}
