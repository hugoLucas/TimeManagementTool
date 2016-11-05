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
    private JButton backToClockInButton;

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

                    if(result[1] == 0) {
                        setClock(clockinDevTimer);
                        setDevLoginDropdowns(logInObj);
                        CardLayout layout = (CardLayout) (cardStack.getLayout());
                        layout.show(cardStack, "ClockInDev");
                    }else{
                        setClock(clockOutDevClock);
                        int [] ids = logInObj.taskAndProjectIDs();

                        prepClockOutScreen(map.getProjectName(ids[1]), map.getTaskName(ids[0]));
                        CardLayout layout = (CardLayout) (cardStack.getLayout());
                        layout.show(cardStack, "ClockOutDev");
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
            }
        }
    }

    private class TimeSheetButton implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            CardLayout layout = (CardLayout) (cardStack.getLayout());
            layout.show(cardStack, "timeSheetDev");
            setSize(400,300);
        }
    }
}
