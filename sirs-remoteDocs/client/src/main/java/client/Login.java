package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static DataBaseLib.Messages.*;

public class Login extends JFrame {
    private JPanel Login;
    private JTextField textUserName;
    private JPasswordField textPassword;
    private JButton loginButton;
    public String userName;

    public Login(ClientService clientService) {

        setContentPane(Login);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450, 300));
        setVisible(true);
        pack();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userName = textUserName.getText();
                String UserPassword = String.valueOf(textPassword.getPassword());
                String result = clientService.loginUser(userName, UserPassword);
                clientService.Log("Login: ", result);

                if (result.equals(WRONG_PASSWORD) || result.equals(WRONG_USERNAME) || result.equals(LOGIN_ERROR)) {
                    dispose();
                    Login login = new Login(clientService);
                } else {
                    dispose();
                    FileForm fileForm = new FileForm(clientService);
                }

            }
        });
    }



}
