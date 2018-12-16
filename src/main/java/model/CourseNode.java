package model;


import javax.swing.tree.DefaultMutableTreeNode;

import data.Student;
import database.InsertStudentIntoClass;
import view.ClassConfig;
import view.GradeCenter;
import view.StudentInfo;

import java.io.File;
import java.util.ArrayList;


public class CourseNode extends DefaultMutableTreeNode {

    private ClassModel classModel;
    private GradeCenter gradeCenter;
    private StudentInfo studentInfo;
    private ClassConfig classConfig;
    private boolean needsUpdate = false;
    private ArrayList<Student> students = new ArrayList<Student>();

    public CourseNode(ClassModel classModel, String semester) {
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
        this.classConfig = new ClassConfig(this, semester);
    }

    public ClassModel getClassModel() {
        return classModel;
    }

    public void addStudent(Student student) {
    	if (student != null) {
			Student withId = InsertStudentIntoClass.insert(student.getDataRowString(), classModel.ClassID);
			this.students.add(withId);
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

    	return gradeCenter;
    }
    
    public ClassConfig getClassConfig() {

    	return this.classConfig;
    }

    public void setGradeCenter(GradeCenter gradeCenter) {
        this.gradeCenter = gradeCenter;
    }

    private void updateViews() {
    	studentInfo.setStudentsModel();
    	needsUpdate = false;
    }
}