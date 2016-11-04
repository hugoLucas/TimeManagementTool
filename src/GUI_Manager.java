import javax.swing.*;
import java.awt.*;

/**
 * Created by Hugo on 11/3/2016.
 */
public class GUI_Manager {
    final static String loginScreen = "LOGIN_PAGE";
    final static String clockinScreenDev = "CLOCKIN_PAGE_DEV";
    final static String clockinScreenMan = "CLOCKIN_PAGE_MAN";

    private JPanel cardStack;

    public GUI_Manager(){
        Login loginPage = new Login();
        ClockinDev clockinDevPage = new ClockinDev();

        cardStack = new JPanel(new CardLayout());
        cardStack.add(loginPage.returnPanel(), "LOGIN_PAGE");
        cardStack.add(clockinDevPage.returnPanel(), "CLOCKINDEV_PAGE");
    }

    public void run(){

    }
}
