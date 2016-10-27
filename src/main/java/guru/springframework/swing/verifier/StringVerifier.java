package guru.springframework.swing.verifier;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class StringVerifier extends InputVerifier {
       
        public boolean verify(JComponent input) {
           String text = null;

           if (input instanceof JTextField) {
             text = ((JTextField) input).getText();
           } else if (input instanceof JComboBox) {
             text = ((JComboBox) input).getSelectedItem().toString();
           }

           try {
              //Integer.parseInt(text);
        	   if(text==null || text.equals(""))
        		   ;
           } catch (NumberFormatException e) {
              return false;
           }

           return true;
        }

        @Override
        public boolean shouldYieldFocus(JComponent input) {
           boolean valid = verify(input);
           if (!valid) {
              JOptionPane.showMessageDialog(null, "Invalid data");
           }

           return valid;
       }

    }
