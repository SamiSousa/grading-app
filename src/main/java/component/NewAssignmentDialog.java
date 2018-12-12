package component;

import model.NewAssignmentForm;
import model.NewClassForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NewAssignmentDialog extends JDialog{
    private String assignmentName;
    private int maxPoints;
    private boolean closed;
    private boolean succeed;

    public NewAssignmentDialog(Frame parent) {
        super(parent, "New assignment", true);

        NewAssignmentForm assignmentForm = new NewAssignmentForm();
        JButton btnAdd = new JButton("Submit");

        btnAdd.addActionListener(e -> {
            // todo submit new assignment name & info here
            assignmentName = assignmentForm.getTxAssignmentName();
            maxPoints = assignmentForm.getTxMaxPoint();
            succeed = true;

            dispose();
        });
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> {
            closed = true;
            dispose();
        });

        JPanel bp = new JPanel();
        bp.add(btnAdd);
        bp.add(btnCancel);

        getContentPane().add(assignmentForm, BorderLayout.CENTER);
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


    public String getAssignmentName() {
        return assignmentName;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public boolean isClosed() {
        return closed;
    }
    public boolean isSucceed() {
        return succeed;
    }
}
