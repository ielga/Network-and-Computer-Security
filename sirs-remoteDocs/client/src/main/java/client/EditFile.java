package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static DataBaseLib.Messages.*;

public class EditFile extends JFrame{
    private JButton saveChangesButton;
    private JTextField textFileName;
    private JTextArea textContent;
    private JPanel EditPanel;
    private JButton exitButton;

    public EditFile(){
        setContentPane(EditPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450,300));
        setVisible(true);
        pack();
        saveChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String FileName = textFileName.getText();
                String textEditContent = textContent.getText();
                JOptionPane.showMessageDialog(null,"Save Changes");

                // String result = clientService.editDocumentContent(filename, contributor, owner, newContent){
                // clientService.Log("EditFile: ", result);

                // if (result.equals(EDIT_CONTENT_ERROR))
                //       display an error ?
                // else
                //        moves to the file list

                dispose();
                FileForm fileForm = new FileForm();

            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                FileForm fileForm = new FileForm();
            }
        });
    }
}
