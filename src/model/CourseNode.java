package model;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.*;

public class CourseNode extends DefaultMutableTreeNode {

    public CourseNode(String courseName) {
        super(courseName,true);
        DefaultMutableTreeNode stuInfo     = new DefaultMutableTreeNode("student info", false);
        DefaultMutableTreeNode classConfig = new DefaultMutableTreeNode("class configuration", false);
        DefaultMutableTreeNode gradeCenter = new DefaultMutableTreeNode("grade center", false);

        this.add(stuInfo);
        this.add(classConfig);
        this.add(gradeCenter);
    }


}