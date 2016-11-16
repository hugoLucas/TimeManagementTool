/**
 * Created by Hugo Lucas on 10/29/2016.
 */
public class Employee {

    private String firstName;   /* First name of the employee */
    private String lastName;    /* Last name of the employee */
    private int employeeNumber; /* Internal identification number of the employee */

    /**
     * Constructor binds input parameters to appropriate internal fields
     *
     * @param fn        employee fist name
     * @param ln        employee last name
     * @param num       employee identification number
     */
    public Employee(String fn, String ln, int num){
        this.firstName = fn;
        this.lastName = ln;
        this.employeeNumber = num;
    }

    /**
     * Employee number getter method
     * @return  employee number of given object
     */
    public int getEmployeeNumber(){
        return this.employeeNumber;
    }

    /**
     * Returns first name and last name of employee in one String in the format:
     * LAST, FIRST.
     *
     * @return  String representation of employee's name
     */
    public String getName(){
        return (lastName + ", " + firstName);
    }
}
