import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame{
    private JPanel Register;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JButton submitButton;
    private JLabel txtUserName;
    private JLabel txtPassword;
    private JLabel txtConfirmPassword;

    public Register() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //String userName = txtUserName.getText();
                //JOptionPane.showMessageDialog(null,"Submit done");
                System.out.println("submit");
                Login login = new Login();
            }
        });
    }

    public static void main(String args[]){
        Register user = new Register();
        user.setContentPane((new Register().Register));
        user.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        user.setMinimumSize(new Dimension(450,300));
        user.setVisible(true);
        user.pack();



    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
