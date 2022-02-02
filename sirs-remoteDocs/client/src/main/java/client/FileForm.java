package client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FileForm extends JFrame {
    private JList UsersFiles;
    private JPanel PanelMain1;
    private JPanel Panel;
    private JButton createNewFileButton;
    private JButton editFileButton;
    private JButton logOutButton;
    private JTextArea textContent;
    private JTextField textOwner;
    private JButton shareFileButton;
    private JTextField textWriters;
    private JTextField textReaders;
    private JButton submitChangesButton;
    private ArrayList<File> filelist;
    private DefaultListModel fileListModel;
    public File file;
    public File file2;
    public File selectedFile;

    public FileForm(ClientService clientService) {
        setContentPane(PanelMain1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450, 300));
        setVisible(true);
        pack();

        filelist = new ArrayList<File>();

        fileListModel = new DefaultListModel();
        UsersFiles.setModel(fileListModel);

        refreshUserList(clientService);
        createNewFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CreateNewFile createNewFile = new CreateNewFile(clientService);

            }
        });

        UsersFiles.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int userID = UsersFiles.getSelectedIndex();
                if (userID >= 0) {
                    selectedFile = filelist.get(userID);
                    textOwner.setText(selectedFile.getOwner());
                    textContent.setText(selectedFile.getPermission());
                }
            }
        });

        editFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                EditFile editFile = new EditFile(clientService, selectedFile);
            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ClientApp clientApp = new ClientApp();
            }
        });
        shareFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ShareFile shareFile = new ShareFile(clientService);
            }
        });
    }

    public void refreshUserList(ClientService clientService) {
        // mandar o username e devolver os ficheiros desse user name;
        // adicionar os files a filelist
        //

        clientService.getOwnerDocuments();
        clientService.getContributorDocuments();

        User user = clientService.getLoggedInUser();

        ArrayList<String> ownerFiles = user.getOwnerFiles();
        ArrayList<User.FileAsContributor> contributorFiles = user.getContributorFiles();

        //file = new File("a", "ana", "abc");
        // file2 = new File("b", "Diana", "def");

        for (String ownerFile: ownerFiles) {
            filelist.add(new File(ownerFile, user.username, "w"));
        }
        for (User.FileAsContributor contributorFile: contributorFiles) {
            filelist.add(new File(contributorFile.filename, contributorFile.owner, contributorFile.userPermission));
        }


        for (File l : filelist) {
            fileListModel.addElement(l.getFileName());
        }
    }


}
