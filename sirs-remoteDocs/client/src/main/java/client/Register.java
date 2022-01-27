package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static DataBaseLib.Messages.*;

public class Register extends JFrame{
    private JPanel Register;
    private JTextField textUserName;
    private JPasswordField textPassword;
    private JButton submitButton;
    private JLabel txtUserName;
    private JLabel txtPassword;
    private JPasswordField textconfirmp;
    private JLabel txtConfirmPassword;


    public Register(ClientService clientService) {
        setContentPane(Register);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450,300));
        setVisible(true);
        pack();

      submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = textUserName.getText();
                String password = String.valueOf(textPassword.getPassword());
                String confirmPassword = String.valueOf(textconfirmp.getPassword());
                dispose();

                String result = clientService.registerUser(userName,password, confirmPassword);
                System.out.println("Passwords: " + password + " --- " + confirmPassword);
                clientService.Log("Register: ", result);

                if (result.equals(MISMATCHED_PASSWORD) || result.equals(WRONG_PASSWORD_SYNTAX)) {
                    JOptionPane.showMessageDialog(null,"Wrong Credentials");
                    dispose();
                    Register register = new Register(clientService);
                } else {
                    dispose();
                    Login login = new Login(clientService);
                }

            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
