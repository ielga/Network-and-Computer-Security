package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame{
    private JPanel Register;
    private JTextField textUserName;
    private JPasswordField passwordField1;
    private JPasswordField passwordField;
    private JButton submitButton;
    private JLabel txtUserName;
    private JLabel txtPassword;
    private JLabel txtConfirmPassword;
    public static ClientService clientService;

    public Register() {

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = textUserName.getText();
                // String userPassword = textUserPassword();
                //JOptionPane.showMessageDialog(null,"Submit done");
                clientService.registerUser(userName,userName);
                System.out.println("submit");
                Login login = new Login(clientService);
            }
        });
    }

    public static void main(String args[]){
        Register user = new Register();
        clientService = new ClientService();
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
