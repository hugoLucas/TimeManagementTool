import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Hugo on 11/14/2016.
 */
public class ProgressReport implements ActionListener {
    /**
     * Generates a summary of project progress
     *
     * @param e     Action which triggered call
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        DB_Reader reader = new DB_Reader();
        ArrayList<ProjectLine> proj = reader.genProgressReport();

        StringBuilder outputMessage = new StringBuilder();
        for(ProjectLine p: proj)
            outputMessage.append(p.printReport() + System.lineSeparator());

        JOptionPane.showMessageDialog(null, outputMessage.toString());
    }
}
