package model;


public class AssignmentEntry {
    private int weight;
    private String assignmentName;
    private int maxPoints;
    private static String[] colNames = new String[]{"assignment", "max points", "weight(%)"};
    public AssignmentEntry(String name, int max, int weight){
        this.assignmentName = name;
        this.maxPoints = max;
        this.weight = weight;
    }
    public AssignmentEntry(){
        this.assignmentName = "default";
        this.maxPoints = 100;
        this.weight = 100;
    }
    public static String[] getColName(){
        return colNames;
    }
    public static int getFieldsCount(){
        return colNames.length;
    }
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }
}
