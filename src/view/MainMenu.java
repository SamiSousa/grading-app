package view;


import component.AddNewDialog;
import model.CourseNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu{

    private JTree tree;
    private static MainMenu MainMenuInstance;
    private JPanel currentPanel;
    private MainMenu(JPanel cur){
        currentPanel = cur;

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("2018 Spring", true),
                node1 = new CourseNode("cs591 d1");
        tree = new JTree(root);
        root.add(node1);
        root.add(new DefaultMutableTreeNode("add new",false));

        ImageIcon listIcon = new ImageIcon(new ImageIcon(MainMenu.class.getResource("../resources/004-list.png")).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH));
        ImageIcon studentIcon = new ImageIcon(new ImageIcon(MainMenu.class.getResource("../resources/003-pass.png")).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH));

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setClosedIcon(listIcon);
        renderer.setOpenIcon(listIcon);
        renderer.setLeafIcon(studentIcon);

        tree.setCellRenderer(renderer);
        tree.setShowsRootHandles(true);

        expandOneLevel();

        tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2){
                    doubleClicked(me);
                }

            }
        });

    }
    void doubleClicked(MouseEvent me) {
        DefaultMutableTreeNode cur = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        TreePath tp = tree.getPathForLocation(me.getX(), me.getY());

        if (cur != null) {
            if (cur.toString().equals("add new")) {
                // todo add new
                System.out.println("new new new");
                AddNewDialog addNew = new AddNewDialog((JFrame) SwingUtilities.getWindowAncestor(currentPanel));
                addNew.setVisible(true);
//                System.out.println(addNew.isSucceed());
                if (addNew.isSucceed()) {
                    if(!addNew.getClassName().isEmpty()) {
                        addNewClassConfigNode(addNew, cur);
                    }
                }
            }
            if(cur.toString().equals("grade center")) {
                System.out.println("grade");
                GradeCenter gradeCenter = new GradeCenter(cur.getParent().toString(),cur.getRoot().toString());
                currentPanel.removeAll();
                currentPanel.add(gradeCenter);
                currentPanel.revalidate();
                currentPanel.repaint();
            }
            if (cur.toString().equals("student info")){
                StudentInfo studentInfo = new StudentInfo();
                currentPanel.removeAll();
                currentPanel.add(studentInfo);
                currentPanel.revalidate();
                currentPanel.repaint();
            }
        }
        else
            System.out.println("null");
    }
    public static MainMenu getMainMenuInstance(JPanel cur){
        if(MainMenuInstance == null){
            MainMenuInstance = new MainMenu(cur);
        }
        return MainMenuInstance;
    }
    public void setPanel(JPanel panel){
        JScrollPane treePanel = new JScrollPane((JTree) tree);
//        treePanel.setLayout(new GridLayout(0, 1));

        panel.add(treePanel);
//        setLayout(new BorderLayout());
//        add(new JScrollPane((JTree) tree), "Center");
    }
    private void expandOneLevel() {
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        do {
            if (currentNode.getLevel() < 1)
                tree.expandPath(new TreePath(currentNode.getPath()));
            currentNode = currentNode.getNextNode();
        }
        while (currentNode != null);
    }
    private void addNewClassConfigNode(AddNewDialog addNew, DefaultMutableTreeNode cur) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        CourseNode newCourse = new CourseNode(addNew.getClassName());

        root.add(newCourse);
        root.add(new DefaultMutableTreeNode("add new", true));

        removeNode(cur);
    }
    private void removeNode(DefaultMutableTreeNode cur){
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.removeNodeFromParent(cur);
        model.reload();
    }

}
