package view;

import component.AddStudentDialog;
import component.EditableTableDisplay;
import data.Student;
import database.GetStudentsInClassQuery;
import database.SQLQuery;
import model.CourseNode;
import model.EditableTableModel;

import javax.swing.*;

import java.awt.BorderLayout;
import java.io.File;
import java.util.List;

public class StudentInfo extends JPanel{
	private final CourseNode course;
	private Object[][] data;
	private EditableTableDisplay display;
	private EditableTableModel model;
	
    public StudentInfo(CourseNode course){
        this.course = course;
        display = new EditableTableDisplay(this);

        this.setLayout(new BorderLayout());
        setStudentsModel();

        display.setPanel(this);

        JLabel lbClassName = new JLabel("Students",SwingConstants.CENTER);
        lbClassName.setFont(lbClassName.getFont().deriveFont (16.0f));
        this.add(lbClassName,BorderLayout.NORTH);
        
        JButton btnAdd = new JButton("Add Student(s)");
        this.add(btnAdd, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            StudentInfo panel = (StudentInfo) ((JButton) e.getSource()).getParent();
            AddStudentDialog addStudent = new AddStudentDialog((JFrame) SwingUtilities.getWindowAncestor(panel), course.getClassModel().ClassID);
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
                    System.out.println("Repainting views...");

                    panel.getCourse().getStudentInfo().setStudentsModel();
                    panel.revalidate();
                    panel.repaint();
                }
            }
        });
        
    }

    public void setStudentsModel() {

        SQLQuery query = new GetStudentsInClassQuery(this.course.getClassModel().ClassID);
        List<Student> students = query.execute();
        System.out.println("Number of students in "+course.getClassModel().CourseNumber+":" +students.size());
        if(students.size() == 0) {
            //fixes weird error with adding the first student to a class
            data = new Object[1][];
            data[0] = Student.getDefaultStudent().getDataRow();
        } else {
            data = new Object[students.size()][];
            for(int i = 0; i < students.size(); i++) {
                data[i] = students.get(i).getDataRow();
            }
        }

        this.model = new EditableTableModel(Student.getStudentDataColumns(),data);
        display.setTableModel(model);
    }


    public CourseNode getCourse() {
    	return this.course;
    }
}
