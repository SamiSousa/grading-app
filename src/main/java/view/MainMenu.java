package view;


import component.AddNewDialog;
import component.AddSemesterDialog;
import data.Student;
import model.CourseNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class MainMenu{

    private JTree tree;
    private static MainMenu MainMenuInstance;
    private JPanel currentPanel;
    private int nextCourseId = 1;
    private int getNextCourseId() {
    	return nextCourseId ++;
    }
    
    private MainMenu(JPanel cur){
        currentPanel = cur;
        //LOAD: Semesters, classes. create a CourseNode for each class and

        //each semester, then add CourseNodes for each class in that semester
        // Root is used to keep semesters together
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root", true);
        DefaultMutableTreeNode semester = new DefaultMutableTreeNode("2018 Fall", true);
        CourseNode node1 = new CourseNode("cs591 d1", semester.toString(), getNextCourseId());
        node1.addStudents(Student.loadStudentsFromFile(new File("./src/main/resources/stu.txt")));

        tree = new JTree(root);
        tree.setRootVisible(false);
        semester.add(node1);
        semester.add(newCourseNode());
        root.add(semester);
        root.add(newSemesterNode());
        
        setCourseView(node1, node1.getGradeCenter());

        ImageIcon listIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("004-list.png")).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH));
        ImageIcon studentIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("003-pass.png")).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH));


        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();

        renderer.setClosedIcon(listIcon);
        renderer.setOpenIcon(listIcon);
        renderer.setLeafIcon(studentIcon);

        tree.setCellRenderer(renderer);
        tree.setShowsRootHandles(true);

        expandToCourseLevel(semester);

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
        String classString = tp.getPathComponent(1).toString();
        System.out.println(classString);


        if (cur != null) {
            if (cur.toString().equals(newCourseNode().toString())) {
                // todo add new
                System.out.println("new course");
                AddNewDialog addNew = new AddNewDialog((JFrame) SwingUtilities.getWindowAncestor(currentPanel));
                addNew.setVisible(true);
                if (addNew.isSucceed()) {
                    if(!addNew.getClassName().isEmpty()) {
                        DefaultMutableTreeNode semester = (DefaultMutableTreeNode) cur.getParent();
                        addNewClassConfigNode(addNew, cur);
                        expandToCourseLevel(semester);
                    }
                }
            }
            if (cur.toString().equals(newSemesterNode().toString())) {
                // todo add new semester
                System.out.println("new semester");
                AddSemesterDialog addNew = new AddSemesterDialog((JFrame) SwingUtilities.getWindowAncestor(currentPanel));
                addNew.setVisible(true);
                if (addNew.isSucceed()) {
                    if(!addNew.getSemesterName().isEmpty()) {
                        addNewSemesterNode(addNew.getSemesterName(), cur);
                    }
                }
            }
            if(cur.toString().equals("grade center")) {
                System.out.println("grade");
                setCourseView((CourseNode) cur.getParent(), ((CourseNode) cur.getParent()).getGradeCenter());
            }
            if (cur.toString().equals("student info")){
            	setCourseView((CourseNode) cur.getParent(), ((CourseNode) cur.getParent()).getStudentInfo());
            }
            if (cur.toString().equals("class configuration")){
                ClassConfig config = new ClassConfig(cur.getParent().toString(),cur.getRoot().toString(),currentPanel);
                JScrollPane panel = new JScrollPane(config);
                currentPanel.removeAll();
                currentPanel.add(panel);
                currentPanel.revalidate();
                currentPanel.repaint();
            }
        }
        else
            System.out.println("null");
    }
    public void setCourseView(CourseNode course, JPanel view) {
    	String title = course.toString() + ", " + course.getParent().toString();
    	JLabel lbClassName = new JLabel(title,SwingConstants.CENTER);
        lbClassName.setFont(lbClassName.getFont().deriveFont (16.0f));
        JPanel coursePanel = new JPanel(new BorderLayout());
        coursePanel.add(lbClassName, BorderLayout.NORTH);
        coursePanel.add(view, BorderLayout.CENTER);
        
    	currentPanel.removeAll();
        currentPanel.add(coursePanel);
        currentPanel.revalidate();
        currentPanel.repaint();
    }
    public static MainMenu getMainMenuInstance(JPanel cur){
        if(MainMenuInstance == null){
            MainMenuInstance = new MainMenu(cur);
        }
        return MainMenuInstance;
    }
    public void setPanel(JPanel panel){
        JScrollPane treePanel = new JScrollPane((JTree) tree);
        panel.add(treePanel);
    }
    private void expandToCourseLevel(DefaultMutableTreeNode cur) {
        if(cur == null) return;
        do {
            if (cur.getLevel() < 2)
                tree.expandPath(new TreePath(cur.getPath()));
            cur = cur.getNextNode();
        }
        while (cur != null);
    }
    private void addNewClassConfigNode(AddNewDialog addNew, DefaultMutableTreeNode cur) {

        DefaultMutableTreeNode semester = (DefaultMutableTreeNode) cur.getParent();
        CourseNode newCourse = new CourseNode(addNew.getClassName(), semester.toString(), getNextCourseId());

        if (addNew.getStudentFile() != null) {
        	newCourse.addStudents(addNew.getStudentFile());
        }
        semester.add(newCourse);
        semester.add(newCourseNode());
        removeNode(cur);
    }
    private void addNewSemesterNode(String semesterName, DefaultMutableTreeNode cur) {
    	DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
    	DefaultMutableTreeNode newSemester = new DefaultMutableTreeNode(semesterName, true);
    	newSemester.add(newCourseNode());
    	
    	root.add(newSemester);
    	root.add(newSemesterNode());
    	removeNode(cur);
    }
    private void removeNode(DefaultMutableTreeNode cur){
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.removeNodeFromParent(cur);
        model.reload();
    }
    private DefaultMutableTreeNode newCourseNode() {
    	return new DefaultMutableTreeNode("add new course", false);
    }
    private DefaultMutableTreeNode newSemesterNode() {
    	return new DefaultMutableTreeNode("add new semester", false);
    }

}
