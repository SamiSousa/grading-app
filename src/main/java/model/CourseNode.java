package model;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import data.Student;
import view.GradeCenter;
import view.StudentInfo;

import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class CourseNode extends DefaultMutableTreeNode {

    int classID;
    private String courseName;
    private String semester;
    private GradeCenter gradeCenter;
    private StudentInfo studentInfo;
    private boolean needsUpdate = false;
    private ArrayList<Student> students = new ArrayList<Student>();

    public CourseNode(String courseName, String semester, int classID) {
        super(courseName,true);
        DefaultMutableTreeNode stuInfo     = new DefaultMutableTreeNode("student info", false);
        DefaultMutableTreeNode classConfig = new DefaultMutableTreeNode("class configuration", false);
        DefaultMutableTreeNode gradeCenter = new DefaultMutableTreeNode("grade center", false);

        this.add(stuInfo);
        this.add(classConfig);
        this.add(gradeCenter);
        this.classID = classID;
        this.courseName = courseName;
        this.semester = semester;
        
        this.gradeCenter = new GradeCenter(this);
        this.studentInfo = new StudentInfo(this);
    }
    
    public int getCourseId() {
    	return classID;
    }
    
    public void addStudent(Student student) {
    	if (student != null) {
			this.students.add(student);
			needsUpdate = true;
    	}
    }
    
    public void addStudents(Student[] students) {
    	for(Student s : students) {
    		addStudent(s);
    	}
    }
    
    public void addStudents(File studentFile) {
    	addStudents(Student.loadStudentsFromFile(studentFile));
    }
    
    public StudentInfo getStudentInfo() {
    	if (needsUpdate) {
    		updateViews();
    	}
    	return studentInfo;
    }
    
    public GradeCenter getGradeCenter() {
    	if (needsUpdate) {
    		updateViews();
    	}
    	return gradeCenter;
    }
    
    private void updateViews() {
    	Student[] stuList = new Student[students.size()];
    	students.toArray(stuList);
    	studentInfo.setStudents(stuList);
    	needsUpdate = false;
    }
}