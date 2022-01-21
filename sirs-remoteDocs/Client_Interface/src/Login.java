import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame{
    private JPanel Login;
    private JLabel UserLogin;
    private JTextField textowner;
    private JPasswordField passwordField1;
    private JButton loginButton;
    
    public Login(){
        setContentPane(Login);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450,300));
        setVisible(true);
        pack();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("login");
                FileForm fileForm = new FileForm();

            }
        });
    }

}
