package view;

import component.NewAssignmentDialog;
import component.NewCategoryDialog;
import data.Assignment;
import model.AssignmentEntry;
import model.ClassConfigCard;
import model.CourseNode;
import model.SemesterNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClassConfig extends JPanel{
    private String courseName;
    private String semester;
    private List<ClassConfigCard> categories;
    private JButton btnAdd;
    private final JPanel thisComponent = this;

    public ClassConfig(CourseNode course, String semester){
        this.courseName = course.getClassModel().CourseNumber;
        this.semester = semester;
        this.categories = new ArrayList<>();

        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

        composeHeader(new JPanel());
        loadAssignmentCards();

        btnAdd = new JButton("add new category");
        btnAdd.addActionListener(e -> {
            NewCategoryDialog dialog = new NewCategoryDialog((JFrame) SwingUtilities.getWindowAncestor(thisComponent));
            dialog.setVisible(true);
            if (dialog.isSucceed()){

                String newCategory = dialog.getCategoryName();
                int weight = dialog.getWeight();
                // todo add new assignment to database
                List<AssignmentEntry> entries = new ArrayList<>();
                entries.add(new AssignmentEntry());
                ClassConfigCard card = new ClassConfigCard(newCategory,weight,entries,thisComponent);
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
        defaultCategories();

    }
    private void composeHeader(JPanel header) {
        String title = "Class configuration for "  + courseName + " in "+semester;
        JLabel lbClassName = new JLabel(title,SwingConstants.CENTER);
        lbClassName.setFont(lbClassName.getFont().deriveFont (16.0f));

        header.add(lbClassName);
        add(header);
    }
    private void defaultCategories() {
        // todo better if we can use hashmap


        Map<String, List<AssignmentEntry>> assignmentInfo = new HashMap<>();

        List<AssignmentEntry> assignment = new ArrayList<>();
        assignment.add(new AssignmentEntry("assignment1",100,50));
        assignment.add(new AssignmentEntry("assignment2",100,50));


        List<AssignmentEntry> project = new ArrayList<>();
        project.add(new AssignmentEntry("project1",100,100));

        List<AssignmentEntry> midterm = new ArrayList<>();
        midterm.add(new AssignmentEntry("midterm1",100,100));

        List<AssignmentEntry> finalExam = new ArrayList<>();
        finalExam.add(new AssignmentEntry("final",100,100));

        assignmentInfo.put("Assignment", assignment);
        assignmentInfo.put("Project", project);
        assignmentInfo.put("Midterm", midterm);
        assignmentInfo.put("Final", finalExam);

        Object[][] categoryList = {
                {"Assignment", new Integer(30)},
                {"Project", new Integer(40)},
                {"Midterm", new Integer(10)},
                {"Final", new Integer(20)}
        };

        for(Object[] categoryInfo : categoryList){
            ClassConfigCard card = new ClassConfigCard((String)categoryInfo[0],(Integer)categoryInfo[1],assignmentInfo.get((String)categoryInfo[0]),thisComponent);
            categories.add(card);
            add(card);
        }


    }

}
