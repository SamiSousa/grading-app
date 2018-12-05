package view;

import component.AddNewDialog;
import component.AddStudentDialog;
import component.EditableTableDisplay;
import data.Student;
import model.CourseNode;
import model.EditableTableModel;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class StudentInfo extends JPanel{
	private final CourseNode course;
	private Object[][] data;
	private EditableTableDisplay display;
	private EditableTableModel model;
	
    public StudentInfo(CourseNode course){
    	this.course = course;
        display = new EditableTableDisplay(this);
        // TODO: Create an empty table to start
        // Use a dummy student row to initialize the table
        data = new Object[1][];
        data[0] = Student.getDefualtStudent().getDataRow();
        model = new EditableTableModel(Student.getStudentDataColumns(),data);
        display.setTableModel(model);
        JPanel tablePanel = new JPanel();
        display.setPanel(tablePanel);
        
        this.setLayout(new BorderLayout());
        JLabel lbClassName = new JLabel("Students",SwingConstants.CENTER);
        lbClassName.setFont(lbClassName.getFont().deriveFont (16.0f));
        this.add(lbClassName,BorderLayout.NORTH);
        this.add(tablePanel,BorderLayout.CENTER);
        
        JButton btnAdd = new JButton("Add Student(s)");
        this.add(btnAdd, BorderLayout.SOUTH);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	StudentInfo panel = (StudentInfo) ((JButton) e.getSource()).getParent();
            	AddStudentDialog addStudent = new AddStudentDialog((JFrame) SwingUtilities.getWindowAncestor(panel), course.classID);
                addStudent.setVisible(true);
                if (addStudent.isSucceed()) {
                    // Add Student object if valid
                	boolean updated = false;
                	Student s = addStudent.getAddedStudent();
                	if (s != null) {
                		panel.getCourse().addStudent(s);
                		updated = true;
                	}
                	
                	// Add Students from file if given
                	File f = addStudent.getStudentFile();
                	if (f != null && f.exists()) {
                		panel.getCourse().addStudents(f);
                		updated = true;
                	}
                	
                	if (updated) {
                		panel.getCourse().getStudentInfo();
                		panel.revalidate();
                        panel.repaint();
                	}
                }
            }
        });
        
    }
    
    public void setStudents(Student[] students) {
    	data = new Object[students.length][students[0].getDataRow().length];
        for(int i=0;i<students.length;i++){
            data[i] = students[i].getDataRow();
        }
        model = new EditableTableModel(Student.getStudentDataColumns(),data);
        display.setTableModel(model);
    }

    public CourseNode getCourse() {
    	return this.course;
    }
}
