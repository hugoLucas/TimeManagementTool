import javax.swing.*;
import java.awt.event.*;

/**
 * Created by Hugo Lucas on 11/2/2016.
 */
public class Login extends JFrame{
    private JPanel rootPanel;
    private JTextField myUsernameTextField;
    private JPasswordField passwordPasswordField;
    private JButton loginButton;

    public Login(){
        setContentPane(rootPanel);
        //pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setVisible(true);

        /* Positions applet at center of screen and gives it dimensions 200px by 200px*/
        setLocationRelativeTo(null);
        setSize(400,200);

        /* Makes size constant */
        setResizable(false);

        /* Title */
        setTitle("User Login");

        /* Add Listener for button */
        ButtonListener login_listener = new ButtonListener();
        loginButton.addActionListener(login_listener);
    }

    public JPanel returnPanel(){
        return this.rootPanel;
    }

    private class ButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginButton){
                String username = myUsernameTextField.getText();
                String password = String.valueOf(passwordPasswordField.getPassword());
                DB_Reader log = new DB_Reader();

                int result = log.login_user(password, username);

                if(result == -2){
                    JOptionPane.showMessageDialog(null, "Unable to connect to CloudSQl, please try again.");
                }else if (result == -1){
                    JOptionPane.showMessageDialog(null, "Invalid username and passowrd combination, please try again.");
                }
            }
        }
    }

}
