package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static DataBaseLib.Messages.*;

public class Login extends JFrame{
    private JPanel Login1;
    private JLabel UserLogin;
    private JTextField textUserName;
    private JPasswordField textPassword;
    private JButton loginButton;
    
    public Login(ClientService clientService){
        setContentPane(Login1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450,300));
        setVisible(true);
        pack();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String UserName = textUserName.getText();
                String UserPassword = String.valueOf(textPassword.getPassword());
                //int canLogin = clientmain.login(username,password)
                // if(canLogin == 1) --> fileForm
                //if(canLogin == 2) ---> Wrong inputs, Try again with a message
                String result = clientService.loginUser(UserName, UserPassword);
                clientService.Log("Login: ", result);

                if (result.equals(LOGIN_ERROR)) {
                    // alguma coisa estava errada
                    // voltar a fazer load do login form
                }
                else {
                    // passa para a seguinte form de Files
                }

                System.out.println("login");
                dispose();
                FileForm fileForm = new FileForm();

            }
        });
    }

}
