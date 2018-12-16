package component;

import javax.swing.*;

import data.Student;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class AddStudentDialog extends JDialog {
    private File studentFile;
    private boolean closed;
    private boolean succeed;
    private Student addedStudent;
    
    private JTextField txFirst, txLast, txId, txEmail, txYear;
    private JTextField txStudentFileName;
    final private JFileChooser fc = new JFileChooser();

    public AddStudentDialog(Frame parent, int classID) {
        super(parent, "Add student", true);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel studentForm = populateForm();
        JPanel loadForm = setLoadFromFile();
        panel.add(studentForm,BorderLayout.CENTER);
        panel.add(loadForm,BorderLayout.PAGE_END);
        panel.setMaximumSize(new Dimension(30,10));

        JButton btnAdd = new JButton("Submit");

        btnAdd.addActionListener(e -> {
            String[] studentData = getStudentData();
            studentFile = getStudentFile();
            // If file is valid, add students from file
            if (studentFile != null) {
            	succeed = true;
            } else {
            	addedStudent = new Student(-1, studentData);
            	succeed = true;
            }
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
    
	private JPanel populateForm() {
        JPanel form = new JPanel(new GridLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbFirst = new JLabel("First Name: ");
        cs.gridx = 0; cs.gridy = 1;
        form.add(lbFirst, cs);
        txFirst = new JTextField("",5);
        cs.gridx = 0; cs.gridy = 2;
        form.add(txFirst,cs);
        lbFirst.setLabelFor(txFirst);
        
        JLabel lbLast = new JLabel("Last Name: ");
        cs.gridx = 1; cs.gridy = 1;
        form.add(lbLast, cs);
        txLast = new JTextField("",5);
        cs.gridx = 1; cs.gridy = 2;
        form.add(txLast,cs);
        lbLast.setLabelFor(txLast);
        
        JLabel lbId = new JLabel("Student ID: ");
        cs.gridx = 2; cs.gridy = 1;
        form.add(lbId, cs);
        txId = new JTextField("",5);
        cs.gridx = 2; cs.gridy = 2;
        form.add(txId,cs);
        lbId.setLabelFor(txId);
        
        JLabel lbEmail = new JLabel("Email: ");
        cs.gridx = 3; cs.gridy = 1;
        form.add(lbEmail, cs);
        txEmail = new JTextField("",5);
        cs.gridx = 3; cs.gridy = 2;
        form.add(txEmail,cs);
        lbEmail.setLabelFor(txEmail);
        
        JLabel lbYear = new JLabel("Year: ");
        cs.gridx = 4; cs.gridy = 1;
        form.add(lbYear, cs);
        txYear = new JTextField("",5);
        cs.gridx = 4; cs.gridy = 2;
        form.add(txYear,cs);
        lbYear.setLabelFor(txYear);

        return form;
	}
    private JPanel setLoadFromFile(){
        JPanel loadPanel = new JPanel(new GridLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;
        JLabel lbStudents = new JLabel("Load Students from File: ");
        cs.gridx = 0;
        cs.gridy = 1;
        loadPanel.add(lbStudents, cs);

        txStudentFileName = new JTextField("",5);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 1;
        loadPanel.add(txStudentFileName,cs);

        JButton fileSelectButton = new JButton("Select File");
        fileSelectButton.setActionCommand("selectfile");
        fileSelectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Selecting student file...");
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    studentFile = fc.getSelectedFile();
                    txStudentFileName.setText(fc.getSelectedFile().getAbsolutePath());
                } else {
                    System.out.println("Cancelled file select");
                }
            }
        });
        cs.gridx = 0;
        cs.gridy = 2;
        loadPanel.add(fileSelectButton,cs);
        lbStudents.setLabelFor(txStudentFileName);
        return loadPanel;

    }
    public String[] getStudentData() {
    	// Check that each field is at least partially filled
        String[] studentData = new String[5];
    	if (txFirst.getText().length() > 0 && txLast.getText().length() > 0 && txId.getText().length() > 0 &&
        		txEmail.getText().length() > 0 && txYear.getText().length() > 0) {
            studentData[0] = txFirst.getText();
            studentData[1] = txLast.getText();
            studentData[2] = txId.getText();
            studentData[3] = txEmail.getText();
            studentData[4] = txYear.getText();
    	}
    	return studentData;
    }
    public File getStudentFile() {
    	if (studentFile != null && studentFile.getAbsolutePath().equals(txStudentFileName.getText())) {
    		return studentFile;
    	}
    	
    	studentFile = new File(txStudentFileName.getText());
    	if (studentFile.exists()) {
    		return studentFile;
    	}
    	return null;
    }
    public boolean isClosed() {
        return closed;
    }
    public boolean isSucceed() {
        return succeed;
    }

    public Student getAddedStudent() {
        return addedStudent;
    }
}
