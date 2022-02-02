package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientApp extends JFrame {
    private JButton loginButton;
    private JPanel panel1;
    private JButton registerButton;
    public static ClientService clientService;

    public ClientApp() {
        dispose();

        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450, 300));
        setVisible(true);
        pack();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login login = new Login(clientService);
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Register register = new Register(clientService);

            }
        });
    }

    public static void main(String args[]) {
        clientService = new ClientService();
        ClientApp clientApp = new ClientApp();

    }

}
