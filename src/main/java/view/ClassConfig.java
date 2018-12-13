package view;

import component.NewAssignmentDialog;
import component.NewCategoryDialog;
import data.Assignment;
import data.ClassCategory;
import database.GetAssignmentFromCategory;
import database.GetClassCategoriesMap;
import database.InsertCategoryIntoClass;
import database.InsertNewAssignment;
import model.AssignmentEntry;
import model.ClassConfigCard;
import model.CourseNode;
import model.SemesterNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


public class ClassConfig extends JPanel{
    private String courseName;
    private String semester;
    private List<ClassConfigCard> categories;
    private JButton btnAdd;
    private final JPanel thisComponent = this;
    private int courseId;

    public ClassConfig(CourseNode course, String semester){
        this.courseName = course.getClassModel().CourseNumber;
        this.semester = semester;
        this.categories = new ArrayList<>();
        this.courseId = course.getClassModel().ClassID;

        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

        composeHeader(new JPanel());
        loadAssignmentCards();

        btnAdd = new JButton("add new category");
        btnAdd.addActionListener(e -> {
            NewCategoryDialog dialog = new NewCategoryDialog((JFrame) SwingUtilities.getWindowAncestor(thisComponent), courseId);
            dialog.setVisible(true);
            if (dialog.isSucceed()){

                String newCategory = dialog.getCategoryName();
                int weight = dialog.getWeight();
                // todo add new category to database
                ClassCategory c = InsertCategoryIntoClass.insert(newCategory,weight,courseId);
                List<Assignment> entries = new ArrayList<>();
                Assignment defautAssignment = InsertNewAssignment.insert(courseId,c.getCategoryID(),"default",100,100);
                entries.add(defautAssignment);
                ClassConfigCard card = new ClassConfigCard(newCategory,weight,c.getCategoryID(),courseId,entries,thisComponent);
                add(card);
                categories.add(card);

                System.out.println(newCategory+" "+weight);

                refreshLayout();
            }
        });
        add(btnAdd);

    }
    private void refreshLayout(){
        remove(btnAdd);
        add(btnAdd);
        revalidate();
        repaint();
    }
    private void loadAssignmentCards() {
        // todo get all categories from database, if null, use default
        GetClassCategoriesMap query = new GetClassCategoriesMap(courseId);
        List<ClassCategory> list = query.execute();
        if(list.size() == 0){
            defaultCategories();
        } else {
            loadClassConfig(list);
        }

    }
    private void composeHeader(JPanel header) {
        String title = "Class configuration";
        JLabel lbClassName = new JLabel(title,SwingConstants.CENTER);
        lbClassName.setFont(lbClassName.getFont().deriveFont (16.0f));

        header.add(lbClassName);
        add(header);
    }
    private void defaultCategories() {

        Map<String, List<Assignment>> assignmentInfo = new HashMap<>();

        Object[][] categoryList = {
                {"Assignment", new Integer(30), 0},
                {"Project", new Integer(40), 0},
                {"Midterm", new Integer(10), 0},
                {"Final", new Integer(20), 0}
        };

        for(int i = 0;i<categoryList.length;i++){
            ClassCategory c = InsertCategoryIntoClass.insert((String)categoryList[i][0],(Integer)categoryList[i][1],courseId);
            categoryList[i][2] = c.getCategoryID();
        }
        List<Assignment> assignment = new ArrayList<>();
        Assignment temp = InsertNewAssignment.insert(courseId, (Integer) categoryList[0][2],"assignment1",100,50);
        assignment.add(temp);
        temp = InsertNewAssignment.insert(courseId, (Integer) categoryList[0][2],"assignment2",100,50);
        assignment.add(temp);


        List<Assignment> project = new ArrayList<>();
        temp = InsertNewAssignment.insert(courseId, (Integer) categoryList[1][2],"project1",100,100);
        project.add(temp);

        List<Assignment> midterm = new ArrayList<>();
        temp = InsertNewAssignment.insert(courseId, (Integer) categoryList[2][2],"midterm",100,100);
        midterm.add(temp);

        List<Assignment> finalExam = new ArrayList<>();
        temp = InsertNewAssignment.insert(courseId, (Integer) categoryList[3][2],"final",100,100);
        finalExam.add(temp);

        assignmentInfo.put("Assignment", assignment);
        assignmentInfo.put("Project", project);
        assignmentInfo.put("Midterm", midterm);
        assignmentInfo.put("Final", finalExam);



        for(Object[] categoryInfo : categoryList){
            ClassConfigCard card = new ClassConfigCard((String)categoryInfo[0],(Integer)categoryInfo[1],(Integer) categoryInfo[2],courseId,assignmentInfo.get((String)categoryInfo[0]),thisComponent);
            categories.add(card);
            add(card);
        }
    }
    private void loadClassConfig (List<ClassCategory> list) {
        Map<Integer, List<Assignment>> assignmentInfo = new HashMap<>();
        Map<Integer, Integer> categorySet = new HashMap<>();
        for(int i=0;i<list.size();i++){
            ClassCategory c  = list.get(i);
            categorySet.put(c.getCategoryID(),i);
            int cId = c.getCategoryID();
            GetAssignmentFromCategory query = new GetAssignmentFromCategory(c.getAssigmentId());
            Assignment assignment = query.execute().get(0);

            if(assignmentInfo.containsKey(cId)){
                List<Assignment> temp = assignmentInfo.get(cId);
                temp.add(assignment);
                assignmentInfo.put(cId, temp);
            } else {
                List<Assignment> temp = new ArrayList<>();
                temp.add(assignment);
                assignmentInfo.put(cId, temp);
            }
        }
        for(int i:categorySet.keySet()){
            ClassCategory c  = list.get(categorySet.get(i));
            ClassConfigCard card = new ClassConfigCard(c.getName(), c.getWeight(), c.getCategoryID(), courseId, assignmentInfo.get(c.getCategoryID()), thisComponent);
            categories.add(card);
            add(card);

        }


    }



}
