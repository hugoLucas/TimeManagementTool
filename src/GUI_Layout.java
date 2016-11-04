import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Hugo on 11/3/2016.
 */
public class GUI_Layout extends JFrame{
    private JPanel cardStack;
    private JTextField loginUsername;
    private JPasswordField loginPassword;
    private JButton loginLoginButton;
    private JPanel Login;
    private JPanel ClockinDev;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton clockInButton;
    private JButton systemButton;
    private JPanel ClockoutDev;
    private JButton systemButton1;
    private JButton clockOutButton;
    private JTextArea textArea1;

    public GUI_Layout(){
        setContentPane(cardStack);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setSize(400,200);
        setResizable(false);
        setVisible(true);

        ButtonListener login_listener = new ButtonListener();
        loginLoginButton.addActionListener(login_listener);
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginLoginButton){
                String username = loginUsername.getText();
                String password = String.valueOf(loginPassword.getPassword());
                //DB_Reader log = new DB_Reader();

                //int result = log.login_user(password, username);
                CardLayout layout = (CardLayout)(cardStack.getLayout());
                layout.show(cardStack, "ClockIn");
               /* if(result == -2){
                    JOptionPane.showMessageDialog(null, "Unable to connect to CloudSQL!");
                }else if (result == -1){
                    JOptionPane.showMessageDialog(null, "Invalid username and passowrd combination, please try again.");
                }else{
                    CardLayout layout = (CardLayout)(cardStack.getLayout());
                    layout.show(cardStack, "ClockIn");
                }*/
            }
        }
    }
}
