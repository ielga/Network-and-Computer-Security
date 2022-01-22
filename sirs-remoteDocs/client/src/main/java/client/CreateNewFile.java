package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CreateNewFile extends JFrame {
    private JTextField txtReaders;
    private JButton saveButton;
    private JTextArea txtFileContent;
    private JButton cancelButton;
    private JPanel UserFile;
    private JTextField txtWriters;
    private JTextField txowner;
    public ArrayList<String> writerslist=new ArrayList<String>();
    public ArrayList<String> readerslist=new ArrayList<String>();
    public String fileContent;


    public CreateNewFile(){
        setContentPane(UserFile);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450,300));
        setVisible(true);
        pack();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Saved");
                String owner = txowner.getText();
                String writer_name = txtWriters.getText();
                String reader_name = txtReaders.getText();
                fileContent = txtFileContent.getText();

                if (writer_name != null) {
                    writerslist.add(writer_name);
                }
                if (reader_name != null) {
                    readerslist.add(reader_name);

                }
                System.out.println(writerslist);
                System.out.println(readerslist);
                System.out.println(fileContent);

                File file = new File(owner,writerslist,readerslist);
                FileForm fileForm = new FileForm();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
