package data;

public class ClassCategory {

    private int categoryID;
    private int classID;
    private String name;
    private int weight;
    private int assigmentId;

    public ClassCategory(int cid, int classId, String categoryName, int weight){
        setClassCategory(cid,classId,categoryName,weight);
    }
    public ClassCategory(int cid, int classId, String categoryName, int weight, int assignmentId){
        setClassCategory(cid,classId,categoryName,weight);
        this.assigmentId = assignmentId;
    }
    private void setClassCategory(int cid, int classId, String categoryName, int weight){
        this.categoryID = cid;
        this.classID = classId;
        this.name = categoryName;
        this.weight = weight;
    }
    public int getCategoryID() {
        return categoryID;
    }

    public int getClassID() {
        return classID;
    }

    public int getAssigmentId() {
        return assigmentId;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }
}
