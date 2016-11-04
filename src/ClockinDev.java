import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hugo on 11/3/2016.
 */
public class ClockinDev extends JFrame{
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton clockInButton;
    private JButton systemButton;
    private JPanel rootPanel;
    private JLabel clockLabel;

    public ClockinDev(){
        setContentPane(rootPanel);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        /* Positions applet at center of screen and gives it dimensions 200px by 200px*/
        setLocationRelativeTo(null);
        setSize(300,200);

        /* Makes size constant */
        setResizable(false);

        /* Title */
        setTitle("User Login");

        Timer clockTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat clock = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm z");
                clockLabel.setText(clock.format(new Date()));
            }
        });
        clockTimer.start();
    }

    public JPanel returnPanel(){
        return this.rootPanel;
    }
}
