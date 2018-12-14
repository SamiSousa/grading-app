package database;

import data.Grade;

public class InsertStudentInGrade extends SQLUpdate{
    private int assignmentId;
    private int studentId;
    private int lostPoints;
    public InsertStudentInGrade(int assignmentId, int studentId, int lostPoints){
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.lostPoints = lostPoints;
    }
    @Override
    public String getQueryString() {
        String query = "INSERT INTO Grade(AssignmentID, StudentID, LostPoints) VALUES(";
        query += assignmentId+",";
        query += studentId +",";
        query += lostPoints +")";
        return query;
    }
}
