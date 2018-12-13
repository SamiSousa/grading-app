package database;

import data.Assignment;



public class UpdateAssignment extends SQLUpdate {

        private Assignment assignment;
        public UpdateAssignment(Assignment assignment){
            this.assignment = assignment;
        }

        @Override
        public String getQueryString() {
            String query = "UPDATE Assignment SET Name = ";
            query += "'"+assignment.getName()+"',";
            query += "MaxPoints = "+assignment.getMaxPoints()+",";
            query += "Weight = "+assignment.getWeight();
            query += " WHERE AssignmentID = "+ assignment.getAssignmentId();
            return query;
        }


}
