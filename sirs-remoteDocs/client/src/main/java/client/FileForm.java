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
    private JPanel PanelMain1;
    private JPanel Panel;
    private JButton createNewFileButton;
    private JButton editFileButton;
    private JButton logOutButton;
    private JTextArea textContent;
    private JTextField textOwner;
    private JTextField textWriters;
    private JTextField textReaders;
    private JButton submitChangesButton;
    private ArrayList<File> filelist;
    private DefaultListModel fileListModel;
    public ArrayList<String> writerslist=new ArrayList<String>();
    public ArrayList<String> readerslist=new ArrayList<String>();
    public File file;
    public File file2;

    public FileForm(){
        setContentPane(PanelMain1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450,300));
        setVisible(true);
        pack();

        filelist = new ArrayList<File>();
        fileListModel = new DefaultListModel();
        UsersFiles.setModel(fileListModel);

        writerslist.add("ANA");
        readerslist.add("Fatima");
        refreshUserList();
        createNewFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //dispose();
                System.out.println("button create new file");
                CreateNewFile createNewFile = new CreateNewFile();

            }
        });
        UsersFiles.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int userID = UsersFiles.getSelectedIndex();
                if (userID >= 0){
                    File f = filelist.get(userID);
                    textOwner.setText(f.getFileName());
                    textContent.setText(f.getContent());

                }
            }
        });

        editFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditFile editFile = new EditFile();

            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                //Login login = new Login();
            }
        });
    }
    public void refreshUserList(){
        System.out.println("refresh");
        file = new File("a","ana",writerslist,readerslist,"abc");
        file2 = new File("b","Diana",writerslist,readerslist,"def");
        filelist.add(file);
        filelist.add(file2);

        for(File l: filelist){
            System.out.println("cheguei");
            System.out.println(l.getOwner());
            fileListModel.addElement(l.getFileName());
        }
    }
}
