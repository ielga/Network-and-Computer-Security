package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static DataBaseLib.Messages.*;

public class CreateNewFile extends JFrame {
    private JButton saveButton;
    private JButton cancelButton;
    private JPanel UserFile1;
    private JTextArea txtFileContent;
    private JTextField txtFileName;
    public String file_Content;
    public String file_Name;
    public String owner;


    public CreateNewFile(ClientService clientService) {
        setContentPane(UserFile1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450, 300));
        setVisible(true);
        pack();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Save button");
                owner = clientService.getLoggedInUsername();
                file_Name = txtFileName.getText();
                file_Content = txtFileContent.getText();

                String result = clientService.createDocument(owner, file_Name, file_Content);
                clientService.Log("Create New File: ", result);
                if (result.equals(FILE_CREATED)) {
                    dispose();
                    JOptionPane.showMessageDialog(null, "FILE CREATED!");
                    FileForm fileForm = new FileForm(clientService);

                } else {
                    dispose();
                    FileForm fileForm = new FileForm(clientService);
                    System.out.println("File error!");
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                FileForm fileForm = new FileForm(clientService);
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}
