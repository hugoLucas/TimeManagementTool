import javax.swing.*;

/**
 * Created by Hugo Lucas on 11/19/2016.
 */
public class GUI_Logic_Login {

    private boolean proceedToClockIn;
    private boolean isDeveloper;

    public boolean acceptLogin(int [] authenticationResult){
        if(authenticationResult[0] == -2){
            JOptionPane.showMessageDialog(null, "Unable to connect to CloudSQL!");
            return false;
        }else if (authenticationResult[0] == -1){
            JOptionPane.showMessageDialog(null, "Invalid username and passowrd " +
                    "combination, please try again.");
            return false;
        }else {
            this.setBooleanFields(authenticationResult);
            return true;
        }
    }

   private void setBooleanFields(int [] authenticationResult){
       if(authenticationResult[1] == 0)
           this.proceedToClockIn = true;
       else
           this.proceedToClockIn = false;

       if(authenticationResult[2] == 0)
           this.isDeveloper = true;
       else
           this.isDeveloper = false;
   }

   public JLabel determineTimeLabel(JLabel [] possibleJLabels){
       return possibleJLabels[this.returnListIndex()];
   }

   public String determineNextScreenName(String [] possibleNextScreenNames){
       return possibleNextScreenNames[this.returnListIndex()];
   }

   private int returnListIndex(){
       if(this.proceedToClockIn)
           if(this.isDeveloper)
               return 0;
           else
               return 1;
       else
           if(this.isDeveloper)
               return 2;
           else
               return 3;

   }
}
