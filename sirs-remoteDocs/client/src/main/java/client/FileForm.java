package client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FileForm extends JFrame{
    private JList UsersFiles;
    private JPanel PanelMain12;
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

    public FileForm(ClientService clientService){
        setContentPane(PanelMain12);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450,300));
        setVisible(true);
        pack();

        filelist = new ArrayList<File>();
        fileListModel = new DefaultListModel();
        UsersFiles.setModel(fileListModel);

        refreshUserList();
        createNewFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //dispose();
                System.out.println("button create new file");
                CreateNewFile createNewFile = new CreateNewFile(clientService);

            }
        });
        UsersFiles.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int userID = UsersFiles.getSelectedIndex();
                if (userID >= 0){
                    File f = filelist.get(userID);
                    textOwner.setText(f.getOwner());
                    textContent.setText(f.getContent());

                }
            }
        });

        editFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                EditFile editFile = new EditFile(clientService);
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
    public void refreshUserList(){
        System.out.println("refresh");
        // mandar o username e devolver os ficheiros desse user name;
        // adicionar os files a filelist
        //
        file = new File("a","ana","abc");
        file2 = new File("b","Diana","def");
        filelist.add(file);
        filelist.add(file2);

        for(File l: filelist){
            fileListModel.addElement(l.getFileName());
        }
    }
}
