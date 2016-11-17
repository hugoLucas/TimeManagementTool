import de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel;

import javax.swing.*;
import java.text.ParseException;

public class Main extends JApplet {

    /**
     * Main method of program
     *
     * @param args
     */
    public static void main(String [] args){

        /**
         * Object creates GUI and handles user input
         */
        try {
            UIManager.setLookAndFeel(new SyntheticaBlackMoonLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();}

        GUI_Layout d = new GUI_Layout();
    }

}