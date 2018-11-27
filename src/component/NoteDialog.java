package component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NoteDialog extends JDialog {
    private String studentName;
    private String assignment;
    private boolean closed;
    private boolean succeed;

    public NoteDialog(JFrame parent, String sName, String assign){
        super(parent, "Note for "+sName+"'s "+assign, true);
        studentName = sName;
        assignment = assign;

        JPanel textField = new JPanel();
        textField.setBorder(new EmptyBorder(5,5,5,5));
        JTextArea noteField = new JTextArea();
        noteField.setPreferredSize(new Dimension( 300, 120 ));
        textField.add(noteField);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // todo submit info here
                succeed = true;

                dispose();
            }
        });

        getContentPane().add(textField, BorderLayout.CENTER);
        getContentPane().add(btnSubmit, BorderLayout.PAGE_END);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closed = true;
            }
        });
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean isSucceed() {
        return succeed;
    }
}
