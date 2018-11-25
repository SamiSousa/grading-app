package model;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.*;

public class MenuTree extends JTree {

    public MenuTree(DefaultMutableTreeNode dmtn) {
        super(dmtn);

//        MouseListener ml = new MouseAdapter() {
//            public void mousePressed(MouseEvent e) {
//                int selRow = tree.getRowForLocation(e.getX(), e.getY());
//                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
//                if(selRow != -1) {
//                    if(e.getClickCount() == 1) {
//                        mySingleClick(selRow, selPath);
//                    }
//                    else if(e.getClickCount() == 2) {
//                        myDoubleClick(selRow, selPath);
//                    }
//                }
//            }
//        };

    }


}