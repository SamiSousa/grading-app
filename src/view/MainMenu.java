package view;


import component.AddNewDialog;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu{

    private JTree tree;
    private static MainMenu MainMenuInstance;
    private JFrame currentFrame;
    private MainMenu(JFrame cur){
        currentFrame = cur;

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("2018 Spring", true),
                node1 = new DefaultMutableTreeNode("cs 591 d1", true), node2 = new DefaultMutableTreeNode(
                "student info", true), node3 = new DefaultMutableTreeNode("add new", true);
        tree = new JTree(root);
        root.add(node1);
        node1.add(node2);
        root.add(node3);

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
//        tree.setRootVisible(false);

    }
    void doubleClicked(MouseEvent me) {
        DefaultMutableTreeNode cur = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
        if (cur != null)
            if(cur.toString().equals("add new")){
            // todo add new
                System.out.println("new new new");
                AddNewDialog addNew = new AddNewDialog(currentFrame);
                addNew.setVisible(true);
            }
        else
            System.out.println("null");
    }
    public static MainMenu getMainMenuInstance(JFrame cur){
        if(MainMenuInstance == null){
            MainMenuInstance = new MainMenu(cur);
        }
        return MainMenuInstance;
    }
    public void setPanel(JPanel panel){
        JScrollPane treePanel = new JScrollPane((JTree) tree);

        treePanel.setBounds(10,10,180,500);

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
}
