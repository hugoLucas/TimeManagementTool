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


    /**
     * Default constructor which takes in the components housing the properites
     * of the new employee to be added.
     *
     * @param fn    JTextField in which user inputs the first name of the new employee
     * @param ln    JTextField in which user inputs the last name of the new employee
     * @param hd    JTextField in which user inputs the hire date of the new employee
     * @param gs    JComboBox which allows the user to select the rank of the new employee
     *
     */
    public AddEmployee(JTextField fn, JTextField ln, JTextField hd, JComboBox gs/*, JList ps*/){
        this.firstName = fn;
        this.lastName = ln;
        this.hireDate = hd;
        this.groupSelector = gs;
    }

    /**
     * When an action is detected, all data is gathered from the object parameters
     * and then passed to the database writer.
     *
     * @param e     Action which triggered method call
     */
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

    /**
     * If the given JTextField has valid, non-blank data, this method extracts
     * that information.
     *
     * @param name  JTextField parameter
     * @return data captured from the text field
     *
     */
    private String getEntry (JTextField name){
        String toRet = null;
        if(!name.getText().equals(""))
            toRet = name.getText();

        return toRet;
    }

    /**
     * If the hireDate isn't blank method parses the String date representation
     * into a valid java.sql.Date object.
     *
     * @return Date objected to be passed to database writer
     *
     */
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
