package component;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import data.Student;

public class AddSemesterDialog extends JDialog {
    private boolean closed;
    private boolean succeed;
    private JTextField txSemester;
	
	public AddSemesterDialog(Frame parent) {
		super(parent, "Add Semester", true);
		this.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        JLabel lbSemester = new JLabel("Semester Name: ");
        panel.add(lbSemester);
        
        txSemester = new JTextField("", 10);
        panel.add(txSemester);
        lbSemester.setLabelFor(txSemester);
        
        JButton btnAdd = new JButton("Create Semester");
        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String name = getSemesterName();
                if (name == null || name.isEmpty()) {
                	closed = true;
                	dispose();
                	return;
                }
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

        getContentPane().add(panel, BorderLayout.CENTER);
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
    public String getSemesterName() {
    	return txSemester.getText();
    }
    public boolean isClosed() {
        return closed;
    }
    public boolean isSucceed() {
        return succeed;
    }
}
