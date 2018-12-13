package model;

import data.Assignment;
import data.Grade;
import data.Student;

import java.util.ArrayList;
import java.util.List;


public class GradeModel {
    private Student student;
    private List<Grade> grades;

    public GradeModel(Student stu, List<Grade> grades){
        student = stu;
        if(grades == null){
            this.grades = new ArrayList<>();
        } else {
            this.grades = grades;
        }

    }

    public int getStudentId() {
        return student.getStudentID();
    }

    public Student getStudent() {
        return student;
    }

    public List<Grade> getGrades() {
        return grades;
    }
    public int getGradesCount(){
        return grades.size();
    }
    public void addGrade(Grade grade) {
        this.grades.add(grade);
    }

}
