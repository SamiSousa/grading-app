package model;


import adapter.TableAdapter;
import component.EditableTableDisplay;
import component.NewAssignmentDialog;
import data.Assignment;
import database.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;


public class ClassConfigCard extends JPanel {
    private String categoryName;
    private int weight;
    private List<Assignment> form;
    private JPanel curPanel;
    private int categoryId;
    private int classId;
    private EditableTableDisplay display;

    public ClassConfigCard(String name,int weight, int categoryId, int classId, List<Assignment> fm, JPanel curPanel){
        this.categoryName = name;
        this.weight = weight;
        this.form = fm;
        this.curPanel = curPanel;
        this.categoryId = categoryId;
        this.classId = classId;
        this.display = new EditableTableDisplay(this);

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
        JButton btnDelete = new JButton("delete selected assignment");

        control.add(btnAdd);
        control.add(btnDelete);

        btnAdd.addActionListener(e -> {
            NewAssignmentDialog dialog = new NewAssignmentDialog((JFrame) SwingUtilities.getWindowAncestor(curPanel));
            dialog.setVisible(true);
            if (dialog.isSucceed()){
                String newAssignmentName = dialog.getAssignmentName();
                int maxPoints = dialog.getMaxPoints();

                Assignment newAssignment = InsertNewAssignment.insert(classId,categoryId,newAssignmentName,maxPoints,0);
                form.add(newAssignment);
                refreshLayout();
            }
        });
        btnDelete.addActionListener(e -> {
            int selectedRow = display.getAdapter().getTableModel().getSelectedRow();
            int rowCount = display.getAdapter().getTableModel().getRowCount();
            if(selectedRow != -1 && rowCount > 1){
                DeleteGrade gradeDelete = new DeleteGrade(form.get(selectedRow).getAssignmentId());
                DeleteAssignment query = new DeleteAssignment(form.get(selectedRow).getAssignmentId());
                gradeDelete.execute();
                query.execute();
                form.remove(selectedRow);
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
        JButton btnDelete = new JButton("delete category");

        btnDelete.addActionListener(e -> {
            DeleteGrade gradeDelete = new DeleteGrade(categoryId,classId);
            DeleteAssignment assignmentDelete = new DeleteAssignment(categoryId,classId);
            DeleteCategory query = new DeleteCategory(categoryId,classId);

            gradeDelete.execute();
            assignmentDelete.execute();
            query.execute();
            setVisible(false);
        });

        categoryInfo.add(lbCategoryName);
        categoryInfo.add(totalWeight);
        categoryInfo.add(btnDelete);
        add(categoryInfo,BorderLayout.NORTH);

        Object[][] data = new Object[form.size()][AssignmentEntry.getFieldsCount()];

        for(int i = 0;i<data.length;i++){
            data[i][0] = form.get(i).getName();
            data[i][1] = form.get(i).getMaxPoints();
            data[i][2] = form.get(i).getWeight();
        }

        EditableTableModel model = new EditableTableModel(AssignmentEntry.getColName(),data);
        for(int i=0;i<AssignmentEntry.getFieldsCount();i++){
            model.addEditableCol(i);
        }
        display.setTableModel(model);
        display.setPanel(this);

        TableAdapter adapter = display.getAdapter();
        adapter.getTableModel().getModel().addTableModelListener(new FormListener(form));

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
        cs.gridwidth = 1;
        panel.add(field,cs);
        field.addFocusListener(new CategoryListener(lbClassName,categoryId,field));


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
class CategoryListener implements FocusListener {
    String label;
    int categoryId;
    JTextField curField;
    CategoryListener(JLabel label, int categoryId,JTextField curField){
        this.label = label.getText();
        this.categoryId = categoryId;
        this.curField = curField;
    }
    @Override public void focusLost(final FocusEvent e) {
        JTextField txt = (JTextField)e.getSource();
        String changedText = txt.getText();
        String query = "SET ";
        switch (label){
            case "Category: ":
                query += "Name = '"+changedText+"'";
                break;
            case "Weight: ":
                query += "Weight = "+changedText;
                break;
        }
        UpdateCategory updateQuery = new UpdateCategory(query,categoryId);
        updateQuery.execute();
    }
    @Override public void focusGained(final FocusEvent pE) {
        curField.selectAll();
    }

}
class FormListener implements TableModelListener{
    private List<Assignment> form;
    FormListener(List<Assignment> form){
        this.form = form;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int col = e.getColumn();
        TableModel model = (TableModel)e.getSource();

        Object data = model.getValueAt(row, col);
        switch (col){
            case 0:
                form.get(row).setName((String) data);
                break;
            case 1:
                form.get(row).setMaxPoints((Integer) data);
                break;
            case 2:
                form.get(row).setWeight((Integer) data);
                break;
        }

        UpdateAssignment query = new UpdateAssignment(form.get(row));
        query.execute();
    }
}
