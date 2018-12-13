package model;


import component.EditableTableDisplay;
import component.NewAssignmentDialog;
import data.Assignment;
import database.InsertNewAssignment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

public class ClassConfigCard extends JPanel {
    private String categoryName;
    private int weight;
    private List<Assignment> form;
    private JPanel curPanel;
    private int categoryId;
    private int classId;

    public ClassConfigCard(String name,int weight, int categoryId, int classId, List<Assignment> fm, JPanel curPanel){
        this.categoryName = name;
        this.weight = weight;
        this.form = fm;
        this.curPanel = curPanel;
        this.categoryId = categoryId;
        this.classId = classId;

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5,15,0,15));
        refreshLayout();
        JPanel control = constructControl();
        add(control,BorderLayout.SOUTH);

        setMaximumSize(new Dimension(600,140));
        setPreferredSize(new Dimension(600,140));

    }
    private JPanel constructControl(){
        JPanel control = new JPanel();
        JButton btnAdd = new JButton("add an assignment");

        control.add(btnAdd);

        btnAdd.addActionListener(e -> {
            NewAssignmentDialog dialog = new NewAssignmentDialog((JFrame) SwingUtilities.getWindowAncestor(curPanel));
            dialog.setVisible(true);
            if (dialog.isSucceed()){
                String newAssignmentName = dialog.getAssignmentName();
                int maxPoints = dialog.getMaxPoints();
                // todo add new assignment to database
                Assignment newAssignment = InsertNewAssignment.insert(classId,categoryId,newAssignmentName,maxPoints,0);
                form.add(newAssignment);
//                int weight1 = 100 / form.size();
//                for(Assignment entry:form){
//                    entry.setWeight(weight1);
//                }
//                System.out.println(newAssignment+" "+maxPoints+" "+ weight1);

                refreshLayout();
            }
        });
        return control;
    }
    private void refreshLayout(){
        removeAll();
        JPanel categoryInfo = new JPanel();
        JPanel lbCategoryName = constructPair("Category: ",8,categoryName);

        JPanel totalWeight = constructPair("Weight: ",5,String.valueOf(weight));
        categoryInfo.add(lbCategoryName);
        categoryInfo.add(totalWeight);
        add(categoryInfo,BorderLayout.NORTH);

        Object[][] data = new Object[form.size()][AssignmentEntry.getFieldsCount()];

        for(int i = 0;i<data.length;i++){
            data[i][0] = form.get(i).getName();
            data[i][1] = form.get(i).getMaxPoints();
            data[i][2] = form.get(i).getWeight();
        }
        EditableTableDisplay display = new EditableTableDisplay(this);
        EditableTableModel model = new EditableTableModel(AssignmentEntry.getColName(),data);
        for(int i=0;i<AssignmentEntry.getFieldsCount();i++){
            model.addEditableCol(i);
        }
        display.setTableModel(model);
        display.setPanel(this);

        JPanel control = constructControl();
        add(control,BorderLayout.SOUTH);

        revalidate();
        repaint();
    }
    private JPanel constructPair(String item, int width, String init){
        JPanel panel = new JPanel(new GridLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;
        JLabel lbClassName = new JLabel(item);
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbClassName, cs);

        JTextField field = new JTextField(init, width);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(field,cs);

        lbClassName.setLabelFor(field);
        return panel;
    }
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<Assignment> getForm() {
        return form;
    }

    public void setForm(List<Assignment> form) {
        this.form = form;
    }
}
