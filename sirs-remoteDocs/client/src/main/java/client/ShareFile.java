package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static DataBaseLib.Messages.*;

public class ShareFile extends JFrame {
    private JTextField textOwner;
    private JTextField textContributor;
    private JTextField textFileName;
    private JTextField textPermission;
    private JPanel SharePanel;
    private JButton saveButton;
    private String file_name;
    private String owner;
    private String contributor;
    private String user_permission;

    public ShareFile(ClientService clientService) {
        setContentPane(SharePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450, 300));
        setVisible(true);
        pack();


        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                file_name = textFileName.getText();
                owner = textOwner.getText();
                contributor = textContributor.getText();
                user_permission = textPermission.getText();
                String name = textFileName.getText();

                if (contributor != null || user_permission != null) {
                    String result = clientService.addDocumentContributor(owner, contributor, file_name, user_permission);
                    clientService.Log("Edit  File: ", result);

                    switch (result) {
                        case CONTRIBUTOR_WAS_ADDED:
                            JOptionPane.showMessageDialog(null, "Save Changes");
                            break;
                        case ADD_CONTRIBUTOR_DENIED:
                            JOptionPane.showMessageDialog(null, "Add Contributor Denied");
                            break;
                        case INVALID_PERMISSION:
                            JOptionPane.showMessageDialog(null, INVALID_PERMISSION);
                            break;
                        case CONTRIBUTOR_DOES_NOT_EXIST:
                            JOptionPane.showMessageDialog(null, "Contributor doesn't Exist");
                            break;
                        case FILE_OR_OWNER_DOES_NOT_EXIST:
                            JOptionPane.showMessageDialog(null, "File or owner doesn't Exist");
                            break;
                        case ADD_CONTRIBUTOR_ERROR:
                            JOptionPane.showMessageDialog(null, ADD_CONTRIBUTOR_ERROR);
                            break;
                    }
                    dispose();
                    FileForm fileForm = new FileForm(clientService);
                }
            }
        });
    }

}
