package view;


import component.AddNewDialog;
import component.AddSemesterDialog;
import database.GetClassesQuery;
import database.GetSemestersQuery;
import database.InsertNewClass;
import database.InsertNewSemester;
import model.ClassModel;
import model.CourseNode;
import model.Semester;
import model.SemesterNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        java.util.List<Semester> semesters = loadSemesters();
        Map<Integer, SemesterNode> semesterMap = new HashMap<>(); //Maps semesters to their SemesterNode

        for(Semester sem: semesters) {
            SemesterNode semester = new SemesterNode(sem.getName(), true, sem.getId());
            System.out.println(sem.getName());
            root.add(semester);
            semesterMap.put(sem.getId(), semester);
        }
        System.out.println(semesterMap);
        for(ClassModel clazz: classes) {
            if(semesterMap.containsKey(clazz.SemesterID)) {
                System.out.println("Adding to semester node: "+clazz.CourseNumber);
                SemesterNode semester = semesterMap.get(clazz.SemesterID);
                CourseNode course = new CourseNode(clazz, semester.getSemesterName());
                semester.add(course);
            } else {
                System.out.println("Adding to root node: "+clazz.CourseNumber);
                CourseNode course = new CourseNode(clazz, "");
                root.add(course);
            }

        }
        for(DefaultMutableTreeNode semester: semesterMap.values()) {
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

        expandToCourseLevel(semester);

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

    private java.util.List<Semester> loadSemesters() {
        GetSemestersQuery query = new GetSemestersQuery();
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
                        DefaultMutableTreeNode semester = (DefaultMutableTreeNode) cur.getParent();
                        addNewClassConfigNode(addNew, cur);
                        expandToCourseLevel(semester);
                    }
                }
            }
            else if (cur.toString().equals(newSemesterNode().toString())) {
                // todo add new semester
                System.out.println("new semester");
                AddSemesterDialog addNew = new AddSemesterDialog((JFrame) SwingUtilities.getWindowAncestor(currentPanel));
                addNew.setVisible(true);
                if (addNew.isSucceed()) {
                    if(!addNew.getSemesterName().isEmpty()) {
                        addNewSemesterNode(addNew.getSemesterName(), cur);
                    }
                }
            }else {
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
        }
        else
            System.out.println("null");
    }
    public void setCourseView(CourseNode course, Component view) {
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
    private void expandToCourseLevel(DefaultMutableTreeNode currentNode) {
        if(currentNode == null) return;
        do {
            if (currentNode.getLevel() < 2)
                tree.expandPath(new TreePath(currentNode.getPath()));
            currentNode = currentNode.getNextNode();
        }
        while (currentNode != null);
    }
    private void addNewClassConfigNode(AddNewDialog addNew, DefaultMutableTreeNode cur) {
        SemesterNode semester = (SemesterNode) cur.getParent();
        System.out.println("Adding class to semester: "+semester.getSemesterID());
        ClassModel newClassModel = InsertNewClass.insertNewClass(addNew.getClassName(), semester.getSemesterID());
        CourseNode newCourse = new CourseNode(newClassModel, semester.getSemesterName());

        if (addNew.getStudentFile() != null) {
        	newCourse.addStudents(addNew.getStudentFile());
        }
        semester.add(newCourse);
        semester.add(newCourseNode());
        removeNode(cur);
    }
    private void addNewSemesterNode(String semesterName, DefaultMutableTreeNode cur) {
    	DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        Semester newSemester = InsertNewSemester.insertNewSemester(semesterName);
    	DefaultMutableTreeNode newSemesterNode = new SemesterNode(newSemester.getName(), true, newSemester.getId());
    	newSemesterNode.add(newCourseNode());
    	
    	root.add(newSemesterNode);
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
