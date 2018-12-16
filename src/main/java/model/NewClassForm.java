package model;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class NewClassForm extends JPanel{
    private JTextField txClassName;
    private JTextField txStudentFileName;
    private File studentFile;
    final private JFileChooser fc = new JFileChooser();

    public NewClassForm() {
        super();
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

        txStudentFileName = new JTextField("",10);
        txClassName = new JTextField("",10);
        JPanel namePanel = setNamePanel(txClassName,"Class Name: ");
        JPanel studentPanel = setNamePanel(txStudentFileName,"Student Filename: ");

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

        add(namePanel);
        add(studentPanel);
        add(fileSelectButton);
        setMaximumSize(new Dimension(20,20));
    }

    private JPanel setNamePanel(JTextField txName, String label){
        JPanel panel = new JPanel(new GridLayout());
        panel.setBorder(new EmptyBorder(0,10,0,10));
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbClassName = new JLabel(label);
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbClassName, cs);
        
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(txName,cs);

        lbClassName.setLabelFor(txName);
        return panel;

    }

    public String getClassName() {
        return txClassName.getText();
    }


    public File getStudentFile() {
    	if (studentFile != null && studentFile.getAbsolutePath().equals(txStudentFileName.getText())) {
    		return studentFile;
    	}

    	if(!txStudentFileName.getText().isEmpty()) {
            studentFile = new File(txStudentFileName.getText());
            if (studentFile.exists()) {
                return studentFile;
            }
        }
    	return null;
    }
}
