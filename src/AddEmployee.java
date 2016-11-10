import javafx.beans.property.IntegerPropertyBase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Lucas on 11/9/2016.
 */

public class AddEmployee implements ActionListener {

    private JTextField firstName;
    private JTextField lastName;
    private JTextField hireDate;
    private JComboBox groupSelector;
    //private JList projectSelector;

    public AddEmployee(JTextField fn, JTextField ln, JTextField hd, JComboBox gs/*, JList ps*/){
        this.firstName = fn;
        this.lastName = ln;
        this.hireDate = hd;
        this.groupSelector = gs;
        //this.projectSelector = ps;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String firstNameEntered = this.getEntry(this.firstName);
        String lastNameEntered = this.getEntry(this.lastName);
        Date startDateEntered = this.extractDate();
        int groupSelected = this.groupSelector.getSelectedIndex();
        //List<String> projectsSelected = this.projectSelector.getSelectedValuesList();

        DB_Writer writer = new DB_Writer();
        writer.addEmployee(firstNameEntered, lastNameEntered, startDateEntered, groupSelected);
    }

    public String getEntry (JTextField name){
        String toRet = null;
        if(!name.getText().equals(""))
            toRet = name.getText();

        return toRet;
    }

    public Date extractDate(){
        Date toRet = null;

        if(!this.hireDate.getText().equals("")){
            String dateEntered = this.hireDate.getText();
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            try {
                long selectedTimeInMillis = df.parse(dateEntered).getTime();
                toRet = new Date(selectedTimeInMillis);
            } catch (ParseException e) {
                return null;
            }
        }

        return toRet;
    }
}
