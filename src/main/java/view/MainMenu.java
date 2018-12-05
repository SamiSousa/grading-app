package view;


import component.AddNewDialog;
import component.AddSemesterDialog;
import data.Student;
import database.GetClassesQuery;
import database.GetStudentsInClassQuery;
import model.ClassModel;
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
import java.util.HashMap;
import java.util.Map;

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

        java.util.List<ClassModel> classes = loadClasses();
        Map<String, DefaultMutableTreeNode> semesters = new HashMap<>(); //Maps semesters to their coursenodes
        for(ClassModel clazz: classes) {
            System.out.println(clazz.CourseNumber);
            DefaultMutableTreeNode semester;
            if(semesters.containsKey(clazz.Semester)) {
                semester = semesters.get(clazz.Semester);
            } else {
                semester = new DefaultMutableTreeNode(clazz.Semester, true);
                root.add(semester);
                semesters.put(clazz.Semester, semester);
            }
            CourseNode course = new CourseNode(clazz);
            semester.add(course);
        }
        for(DefaultMutableTreeNode semester: semesters.values()) {
            semester.add(newCourseNode());
        }

        root.add(newSemesterNode());
        tree = new JTree(root);
        tree.setRootVisible(false);
        
//        setCourseView(node1, node1.getGradeCenter());

//        System.out.println(MainMenu.class.getResource(""));

        ImageIcon listIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("004-list.png")).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH));
        ImageIcon studentIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("003-pass.png")).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH));

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

    private java.util.List<ClassModel> loadClasses() {
        GetClassesQuery query = new GetClassesQuery();
        return query.execute();
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
                        addNewClassConfigNode(addNew, cur);
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
            CourseNode clickedClassNode = (CourseNode)cur.getParent();
            ClassModel model = clickedClassNode.getClassModel();

            if(cur.toString().equals("grade center")) {
                System.out.println("grade");

                setCourseView(clickedClassNode, clickedClassNode.getGradeCenter());
            }
            if (cur.toString().equals("student info")){
                setCourseView(clickedClassNode, clickedClassNode.getStudentInfo());
            }
            if (cur.toString().equals("class configuration")){
                JPanel panel = new JPanel();
                JScrollPane scroll = new JScrollPane(clickedClassNode.getClassConfig());
                panel.add(scroll);
                setCourseView(clickedClassNode, panel);
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
        DefaultMutableTreeNode semester = (DefaultMutableTreeNode) cur.getParent();
        CourseNode newCourse = null;//new CourseNode(addNew.getClassName(), semester.toString(), getNextCourseId());
//        InsertNewClass.
        //todo new course saving
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
