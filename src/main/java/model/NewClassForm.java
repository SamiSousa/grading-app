package model;

import component.EditableTableDisplay;
import component.Login;
import component.LoginDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class NewClassForm extends JPanel{
    private JTextField txClassName;
    private EditableTableModel tableModel;
    private JTextField txStudentFileName;
    private File studentFile;
    final private JFileChooser fc = new JFileChooser();

    public NewClassForm() {
        super();
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbClassName = new JLabel("Class Name: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbClassName, cs);

        txClassName = new JTextField("",10);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(txClassName,cs);
        
        lbClassName.setLabelFor(txClassName);
        
        JLabel lbStudents = new JLabel("Student Filename: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbStudents, cs);

        txStudentFileName = new JTextField("",10);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(txStudentFileName,cs);
        
        JButton fileSelectButton = new JButton("Select File");
        fileSelectButton.setActionCommand("selectfile");
        fileSelectButton.addActionListener(e -> {
            System.out.println("Selecting student file...");
            int returnVal = fc.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                studentFile = fc.getSelectedFile();
                txStudentFileName.setText(fc.getSelectedFile().getAbsolutePath());
            } else {
                System.out.println("Cancelled file select");
            }
        });
        
        cs.gridx = 2; cs.gridy = 2; cs.gridwidth = 1;
        panel.add(fileSelectButton, cs);
        
        lbStudents.setLabelFor(txStudentFileName);

        add(panel,BorderLayout.NORTH);

    }
    
    public String getClassName() {
        return txClassName.getText();
    }

    public Object getValueAt(int row, int col){
        return tableModel.getValueAt(row, col);
    }
    public Object[][] getArray() {
        int colCount = tableModel.getColumnCount();
        int rowCount = tableModel.getRowCount();
        Object[][] data = new Object[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                data[i][j] = tableModel.getValueAt(i, j);
            }
        }
        return data;
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
}
