import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    public GUI_Layout(){
        setContentPane(cardStack);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setSize(400,200);
        setResizable(false);
        setVisible(true);

        LoginButtonListener login_listener = new LoginButtonListener();
        loginLoginButton.addActionListener(login_listener);

        ClockInButtonListener clockin_listener = new ClockInButtonListener();
        clockInDevClockInButton.addActionListener(clockin_listener);

        ClockOutButtonListener clockout_listener = new ClockOutButtonListener();
        clockOutDevClockOutButton.addActionListener(clockout_listener);

        TimeSheetButton timesheet_listener = new TimeSheetButton();
        clockInDevTimeSheetButton.addActionListener(timesheet_listener);
        clockOutDevTimeSheetButton.addActionListener(timesheet_listener);

        BackButton back_listener = new BackButton();
        timeSheetDevBackButton.addActionListener(back_listener);

        GenTimeSheetButton timeSheetDevGenerateButton_listener = new GenTimeSheetButton();
        timeSheetDevGenerateButton.addActionListener(timeSheetDevGenerateButton_listener);
    }

    public void setDevLoginDropdowns(userLogIn ln){
        ArrayList<String> projDropDown = ln.projectList(map);
        ArrayList<String> taskDropDown = ln.tasksInProject(projDropDown.get(0), map);

        for(String proj: projDropDown)
            clockInDevProjectSelector.addItem(proj);

        for(String task: taskDropDown)
            clockInDevTaskSelector.addItem(task);

        ActionListener clockInDevProjSelectorListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == clockInDevProjectSelector) {
                    String projectSelected = (String) clockInDevProjectSelector.getSelectedItem();
                    clockInDevTaskSelector.removeAllItems();

                    for(String taskName: ln.tasksInProject(projectSelected, map))
                        clockInDevTaskSelector.addItem(taskName);
                }
            }
        };

        clockInDevProjectSelector.addActionListener(clockInDevProjSelectorListener);
    }

    public void prepClockOutScreen(String projSel, String taskSel){
        clockOutDevCurrentProject.setText(projSel);
        clockOutDevCurrentTask.setText(taskSel);
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

    public void prepTimeSheetDev(){
        ArrayList<String> projDropDown = logInObj.projectList(map);
        ArrayList<String> taskDropDown = logInObj.tasksInProject(projDropDown.get(0), map);

        timeSheetDevProjectSelector.removeAllItems();
        timeSheetDevTaskSelector.removeAllItems();
        for(String proj: projDropDown)
            timeSheetDevProjectSelector.addItem(proj);

        for(String task: taskDropDown)
            timeSheetDevTaskSelector.addItem(task);

        timeSheetDevProjectSelector.addItem("All Projects");
        timeSheetDevTaskSelector.addItem("All Tasks");
        ActionListener timeSheetDevProjSelectorListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == timeSheetDevProjectSelector) {
                    String projectSelected = (String) timeSheetDevProjectSelector.getSelectedItem();

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
            }
        };
        timeSheetDevProjectSelector.addActionListener(timeSheetDevProjSelectorListener);
        timeSheetDevIntervalSelector.addItem("Current Week");
        timeSheetDevIntervalSelector.addItem("Current Month");
        timeSheetDevIntervalSelector.addItem("Current Year");
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

                    CardLayout layout = (CardLayout) (cardStack.getLayout());
                    if(result[1] == 0) {
                        if (result[2] == 0) {
                            setClock(clockinDevTimer);
                            setDevLoginDropdowns(logInObj);
                            layout.show(cardStack, "ClockInDev");
                            clockedInOrOut = "ClockInDev";
                        }else{
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

                        }
                    }
                }
            }
        }
    }

    private class ClockInButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource() == clockInDevClockInButton){
                String projectSelected = (String) clockInDevProjectSelector.getSelectedItem();
                String taskSelected = (String) clockInDevTaskSelector.getSelectedItem();
                userClockIn clockInUser = new userClockIn(employeeID, map.getTaskID(taskSelected));
                clockInUser.clockIn();

                destroyClock();
                prepClockOutScreen(projectSelected, taskSelected);
                setClock(clockOutDevClock);
                CardLayout layout = (CardLayout) (cardStack.getLayout());
                layout.show(cardStack, "ClockOutDev");
                clockedInOrOut = "ClockOutDev";
            }
        }
    }

    private class ClockOutButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == clockOutDevClockOutButton){
                String taskSelected = clockOutDevCurrentTask.getText();

                userClockOut clockOutUser = new userClockOut(employeeID, map.getTaskID(taskSelected));
                clockOutUser.clockOut();

                destroyClock();
                setDevLoginDropdowns(logInObj);
                setClock(clockinDevTimer);
                CardLayout layout = (CardLayout) (cardStack.getLayout());
                layout.show(cardStack, "ClockInDev");
                clockedInOrOut = "ClockInDev";
            }
        }
    }

    private class TimeSheetButton implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            CardLayout layout = (CardLayout) (cardStack.getLayout());
            prepTimeSheetDev();
            layout.show(cardStack, "timeSheetDev");
            setSize(400,300);
        }
    }

    private class BackButton implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            CardLayout layout = (CardLayout) (cardStack.getLayout());
            layout.show(cardStack, clockedInOrOut);
            setSize(400,200);
        }
    }

    private class GenTimeSheetButton implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String timeIntervalSelected = (String) timeSheetDevIntervalSelector.getSelectedItem();
            String projectSelected = (String) timeSheetDevProjectSelector.getSelectedItem();
            String taskSelected = (String) timeSheetDevTaskSelector.getSelectedItem();

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

            GenTimeSheet genHelper = new GenTimeSheet("Developer", employeeID, projectOption, taskOption, timeIntervalSelected);
            StringBuilder genList = genHelper.createReport();
            if(genList != null)
                JOptionPane.showMessageDialog(null, genList.toString());
            else
                JOptionPane.showMessageDialog(null, "NO RESULTS FOUND");
        }
    }
}
