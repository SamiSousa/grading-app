package model;

import javax.swing.tree.DefaultMutableTreeNode;

public class SemesterNode extends DefaultMutableTreeNode {

    private int semesterID;
    private String semesterName;

    public SemesterNode(String name, boolean bool, int semesterID) {
        super(name, bool);
        this.semesterID = semesterID;
        this.semesterName = name;
    }

    public int getSemesterID() {
        return semesterID;
    }

    public String getSemesterName() {
        return semesterName;
    }
}
