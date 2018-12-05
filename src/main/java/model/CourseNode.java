package model;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import data.Student;
import view.ClassConfig;
import view.GradeCenter;
import view.StudentInfo;

import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class CourseNode extends DefaultMutableTreeNode {

    private ClassModel classModel;
    private GradeCenter gradeCenter;
    private StudentInfo studentInfo;
    private ClassConfig classConfig;
    private boolean needsUpdate = false;
    private ArrayList<Student> students = new ArrayList<Student>();

    public CourseNode(ClassModel classModel) {
        super(classModel.CourseNumber, true);
        DefaultMutableTreeNode stuInfo     = new DefaultMutableTreeNode("student info", false);
        DefaultMutableTreeNode classConfig = new DefaultMutableTreeNode("class configuration", false);
        DefaultMutableTreeNode gradeCenter = new DefaultMutableTreeNode("grade center", false);

        this.add(stuInfo);
        this.add(classConfig);
        this.add(gradeCenter);
        this.classModel = classModel;

        this.gradeCenter = new GradeCenter(this);
        this.studentInfo = new StudentInfo(this);
        this.classConfig = new ClassConfig(this);
    }

    public ClassModel getClassModel() {
        return classModel;
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
    
    public ClassConfig getClassConfig() {
    	if (needsUpdate) {
    		updateViews();
    	}
    	return this.classConfig;
    }
    
    private void updateViews() {
    	Student[] stuList = new Student[students.size()];
    	students.toArray(stuList);
    	studentInfo.setStudents(stuList);
    	needsUpdate = false;
    }
}