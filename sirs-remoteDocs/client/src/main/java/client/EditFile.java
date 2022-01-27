package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static DataBaseLib.Messages.*;

public class EditFile extends JFrame{
    private JButton saveChangesButton;
    private JTextField textFileName;
    private JTextArea textContentEdit;
    private JPanel EditPanel;
    private JButton exitButton;
    private JTextField textOwner;
    private String file_name;
    private String owner;
    private String contributor;

    public EditFile(ClientService clientService){
        setContentPane(EditPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450,300));
        setVisible(true);
        pack();
        saveChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                file_name = textFileName.getText();
                String newContent = textContentEdit.getText();
                owner = textOwner.getText();
                contributor = clientService.getLoggedInUser();

                String result = clientService.editDocumentContent(file_name, contributor, owner, newContent);
                clientService.Log("EditFile: ", result);
                if (result.equals(EDIT_CONTENT_ERROR)) {
                    JOptionPane.showMessageDialog(null, "Edit content error,Try again");
                }
                else if(result.equals(CONTENT_UPDATED)){
                    JOptionPane.showMessageDialog(null, CONTENT_UPDATED);
                }
                else if(result.equals(USER_DOES_NOT_HAVE_PERMISSION)){
                    JOptionPane.showMessageDialog(null, USER_DOES_NOT_HAVE_PERMISSION);
                }
                else {
                    JOptionPane.showMessageDialog(null, "An internal error has occured!");
                }
                dispose();
                FileForm fileForm = new FileForm(clientService);
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                FileForm fileForm = new FileForm(clientService);
            }
        });
    }
}
