package component;

import model.NewAssignmentForm;
import model.NewCategoryForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NewCategoryDialog extends JDialog{
    private String categoryName;
    private int weight;
    private boolean closed;
    private boolean succeed;

    public NewCategoryDialog(Frame parent) {
        super(parent, "New category", true);

        NewCategoryForm categoryForm = new NewCategoryForm();
        JButton btnAdd = new JButton("Submit");

        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // todo submit new category name & info here
                categoryName = categoryForm.getCategoryName();
                weight = categoryForm.getCategoryWeight();
                succeed = true;

                dispose();
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

        getContentPane().add(categoryForm, BorderLayout.CENTER);
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


    public String getCategoryName() {
        return categoryName;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isClosed() {
        return closed;
    }
    public boolean isSucceed() {
        return succeed;
    }

}
