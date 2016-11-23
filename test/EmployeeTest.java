import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Hugo Lucas on 11/23/2016.
 */
public class EmployeeTest {

    private String firstName;
    private String lastName;
    private int idNumber;

    @Before
    public void startUp(){
        this.firstName = "Hugo";
        this.lastName = "Flores";
        this.idNumber = 1;
    }

    @Test
    public void getEmployeeNumber() throws Exception {
        Employee testObj = new Employee(this.firstName, this.lastName, this.idNumber);
        assertEquals(this.idNumber, testObj.getEmployeeNumber());
    }

    @Test
    public void getName() throws Exception {
        Employee testObj = new Employee(this.firstName, this.lastName, this.idNumber);
        String name = this.lastName + ", " + this.firstName;
        assertEquals(name, testObj.getName());
    }

}