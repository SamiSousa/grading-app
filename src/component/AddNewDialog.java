package component;

import model.NewClassForm;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddNewDialog extends JDialog {

    private boolean succeeded;
    private boolean closed;

    public AddNewDialog(Frame parent) {
        super(parent, "New class", true);

        NewClassForm classForm = new NewClassForm();
        JButton btnAdd = new JButton("Submit");

        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            // todo submit new class name & info here
            }
        });
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closed = true;
                dispose();
            }
        });

        JPanel bp = new JPanel();
        bp.add(btnAdd);
        bp.add(btnCancel);

        getContentPane().add(classForm, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

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


    public boolean isSucceeded() {
        return succeeded;
    }
    public boolean isClosed() {
        return closed;
    }
}