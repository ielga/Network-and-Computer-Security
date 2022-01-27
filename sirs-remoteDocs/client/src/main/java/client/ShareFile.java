package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static DataBaseLib.Messages.ADD_CONTRIBUTOR_DENIED;
import static DataBaseLib.Messages.CONTRIBUTOR_WAS_ADDED;

public class ShareFile extends JFrame{
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

    public ShareFile(ClientService clientService){
        setContentPane(SharePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450,300));
        setVisible(true);
        pack();


        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                file_name= textFileName.getText();
                owner = textOwner.getText();
                contributor = textContributor.getText();
                user_permission = textPermission.getText();
                String name = textFileName.getText();

                System.out.println(name);

                System.out.println("fileName: "+ file_name + "owner: "+owner);
                System.out.println("Contributor "+ contributor + "Permission: "+ user_permission );

                if(contributor!=null || user_permission!=null) {
                    String result = clientService.addDocumentContributor(owner, contributor, file_name, user_permission);
                    clientService.Log("Edit  File: ", result);

                    if (result.equals(CONTRIBUTOR_WAS_ADDED)) {
                        JOptionPane.showMessageDialog(null, "Save Changes");
                    } else if (result.equals(ADD_CONTRIBUTOR_DENIED)) {
                        JOptionPane.showMessageDialog(null, "Add Contributor Denied");
                    } else {
                        JOptionPane.showMessageDialog(null, "Contributor doesn't exits");
                    }
                    dispose();
                    FileForm fileForm = new FileForm(clientService);
                }
            }
        });
    }
}
