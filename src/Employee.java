/**
 * Created by Hugo Lucas on 10/29/2016.
 */
public class Employee {

    private String firstName;
    private String lastName;
    private int employeeNumber;

    public Employee(String fn, String ln, int num){
        this.firstName = fn;
        this.lastName = ln;
        this.employeeNumber = num;
    }

    public int getEmployeeNumber(){
        return this.employeeNumber;
    }

    public String getName(){
        return (lastName + ", " + firstName);
    }
}
