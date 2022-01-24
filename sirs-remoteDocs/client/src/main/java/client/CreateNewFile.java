package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static DataBaseLib.Messages.*;

public class CreateNewFile extends JFrame {
    private JTextField txtReaders;
    private JButton saveButton;
    private JTextArea txtFileContent;
    private JButton cancelButton;
    private JPanel UserFile;
    private JTextField txtWriters;
    private JTextField txowner;
    private JTextField textFileName;
    //public ArrayList<String> writerslist=new ArrayList<>();
    //public ArrayList<String> readerslist=new ArrayList<>();
    public String fileContent;
    public String fileName;


    public CreateNewFile(){
        System.out.println("create New File");
        setContentPane(UserFile);
        System.out.println("create New File1");
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
                fileName = textFileName.getText();
                fileContent = txtFileContent.getText();

                /*if (writer_name != null) {
                    writerslist.add(writer_name);
                }
                if (reader_name != null) {
                    readerslist.add(reader_name);

                }*/
                //System.out.println(writerslist);
                //System.out.println(readerslist);
                System.out.println(fileContent);
                System.out.println(fileName);

                //File file = new File(fileName,owner,writerslist,readerslist,fileContent);

                /*
                 String result = clientService.createDocument(owner, filename, content)
                 clientService.Log("Create New File: ", result);
                 if (result.equals(FILE_CREATED))
                    // passa para o fileForm ou ao seguinte form
                 else
                    // nao consiguiu criar o ficheiro
                */


                dispose();
                FileForm fileForm = new FileForm();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
