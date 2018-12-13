package view;

import component.EditableTableDisplay;
import component.collector.TableDataCollector;
import data.Assignment;
import data.Grade;
import database.GetAllAssignmentForStudentInClass;
import model.CourseNode;
import model.EditableTableModel;
import model.ExcelViewModel;
import model.GradeModel;

import javax.swing.*;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class GradeCenter extends JPanel{

	private final CourseNode course;
    private String title;
    private EditableTableDisplay tableDisplay;
    private Map<Integer, GradeModel> gradeList;
    private Set<String> colSet;

    public GradeCenter(CourseNode course) {
    	this.course = course;
    	gradeList = new HashMap<>();
    	colSet = new HashSet<>();
        setLayout(new BorderLayout());

        title = "Grade center";

        composeCenter();

        JPanel header = new JPanel(new BorderLayout());
        composeHeader(header);
        add(header, BorderLayout.NORTH);
    }
    private void composeHeader(JPanel header) {

        JLabel lbClassName = new JLabel(title,SwingConstants.CENTER);
        lbClassName.setFont(lbClassName.getFont().deriveFont (16.0f));

        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel lbMenu = new JLabel("Assignment:");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbMenu,cs);

        Set<String> filterList = new HashSet<>(colSet);
        filterList.add("All");
        JComboBox dropMenu = new JComboBox(filterList.toArray());
        dropMenu.setSelectedItem("All");
        dropMenu.addActionListener(new JComboBoxListener(gradeList,tableDisplay,1));
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(dropMenu, cs);

        header.add(lbClassName, BorderLayout.NORTH);
        header.add(panel, BorderLayout.CENTER);
    }
    private void composeCenter() {

        tableDisplay = new EditableTableDisplay(this);
//        EditableTableModel model = tableDisplay.getModel();
//
//        model.addEditableCol(4);
//        model.addEditableCol(5);
//
//        tableDisplay.setTableModel(model);
        EditableTableModel model = loadData();
        tableDisplay.setTableModel(model);
        tableDisplay.setPanel(this);
//        tableDisplay.getAdapter().setCellColorRender(new TableCellRender());

    }
    private EditableTableModel loadData() {
        GetAllAssignmentForStudentInClass query = new GetAllAssignmentForStudentInClass(course.getClassModel().ClassID);
        List<Grade> res = query.execute();
        if (res.size() == 0) {
            String[] cols = new String[]{"Name","Assignment","Max Points","Lost Points"};
            Object[][] data = new Object[][]{{" "," "," "," "}};
            return new EditableTableModel(cols,data);
        } else {
            for (Grade grade : res) {
                int stuId = grade.getStudent().getStudentID();
                GradeModel newGrade;
                if (gradeList.containsKey(stuId)) {
                    newGrade = gradeList.get(stuId);
                } else {
                    newGrade = new GradeModel(grade.getStudent(), null);
                }
                newGrade.addGrade(grade);
                gradeList.put(stuId, newGrade);
            }
            ExcelViewModel excelModel = constructExcelView(gradeList, colSet);
            EditableTableModel model = new EditableTableModel(excelModel.getCols(),excelModel.getData());
            for(int j=1;j<excelModel.getColCounts();j++){
                model.addEditableCol(j);
            }
            return model;
        }

    }
    public static ExcelViewModel constructExcelView(Map<Integer, GradeModel> gradeList, Set<String> colSet){
        List<String> colNames = new ArrayList<>();
        colNames.add("Name");
        for(int stuId :gradeList.keySet()){
            GradeModel grade = gradeList.get(stuId);
            List<Grade> grades = grade.getGrades();
            for(int j = 0;j<grades.size();j++){
                Assignment a = grades.get(j).getAssignment();
                String col = a.getName()+" lostPoints";
//                    col += " (W: "+a.getWeight();
                col += " (MP: "+a.getMaxPoints()+")";
                colNames.add(col);
                colSet.add(a.getName());
            }
            break;
        }
        Object[][] data = new Object[gradeList.size()][colNames.size()];
        int i = 0;
        for(int stuId :gradeList.keySet()) {
            GradeModel grade = gradeList.get(stuId);
            List<Grade> grades = grade.getGrades();
            data[i][0] = grade.getStudent().getFirstName()+" "+grade.getStudent().getLastName();
            for(int j = 1;j<colNames.size();j++){
                data[i][j] = grades.get(j-1).getLostPoints();
            }
            i++;
        }

        String[] cols = new String[colNames.size()];
        for(int j = 0;j<cols.length;j++){
            cols[j] = colNames.get(j);
        }
        return new ExcelViewModel(cols,data,colSet);

    }
    public static ExcelViewModel constructGradingView(Map<Integer, GradeModel> gradeList){
        String[] cols = new String[]{"Name","Assignment","Max Points","Lost Points"};
        int totalLength = 0;
        for(int stuId :gradeList.keySet()){
            totalLength += gradeList.get(stuId).getGradesCount();
        }
        Object[][] data = new Object[totalLength][cols.length];
        int i = 0;
        for (int stuId :gradeList.keySet()){
            GradeModel grade = gradeList.get(stuId);
            List<Grade> info = grade.getGrades();
            for (int j=0;j<info.size();j++){
                Assignment assignment = info.get(j).getAssignment();
                data[i][0] = grade.getStudent().getFirstName()+" "+grade.getStudent().getLastName();
                data[i][1] = assignment.getName();
                data[i][2] = assignment.getMaxPoints();
                data[i][3] = info.get(j).getLostPoints();
                i++;
            }
        }
        for(int k=0;k<data.length;k++){
            for(int j=0;j<data[k].length;j++){
                if(data[k][j] == null){
                    System.out.print(k+" "+j+" null");
                } else {
                    System.out.print(data[k][j]+" ");
                }
            }
            System.out.println();
        }
        return new ExcelViewModel(cols,data,new HashSet<>());
    }
    public CourseNode getCourse() {
    	return this.course;
    }

}
class TableCellRender extends DefaultTableCellRenderer {
    private Color background = getBackground();
    private Color  foreground = getForeground();
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column)
    {
        Component render = super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);

        if(value!=null && ((Integer) value < 0 || (Integer) value > (Integer) table.getModel().getValueAt(row, column-1)))
            setBackground(new Color(255, 153, 153));
        else {
            setBackground(background);
            setForeground(foreground);
        }
        setHorizontalAlignment(JLabel.RIGHT);
        return render;
    }
}
class JComboBoxListener implements ActionListener{

    private EditableTableDisplay tableDisplay;
    private Map<Integer,GradeModel> map;
    private String[] cols;
    private Object[][] data;
    private int filterIndex;
    public JComboBoxListener(Map<Integer,GradeModel> map, EditableTableDisplay tableDisplay, int filterindex){
        this.map = map;
        this.tableDisplay = tableDisplay;
        this.filterIndex=filterindex;
        ExcelViewModel viewModel = GradeCenter.constructGradingView(map);
        cols = viewModel.getCols();
        data = viewModel.getData();
    }
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        String petName = (String)cb.getSelectedItem();
        updateLabel(petName);
    }
    private void updateLabel(String filter){

        if(filter.equals("All")){
            ExcelViewModel excelModel = GradeCenter.constructExcelView(map, new HashSet<>());
            EditableTableModel model = new EditableTableModel(excelModel.getCols(),excelModel.getData());
            tableDisplay.setTableModel(model);
//            tableDisplay.getAdapter().setCellColorRender(new TableCellRender());
            return;
        }

        int leng = data[0].length;
        List<Integer> indexList = new ArrayList<>();
        for(int i=0;i<data.length;i++){
            if(data[i][filterIndex].equals(filter)){
                indexList.add(i);
            }
        }
        Object[][] res = new Object[indexList.size()][leng];
        for(int i=0;i<res.length;i++){
            for(int j=0;j<res[i].length;j++){
                res[i][j] = data[indexList.get(i)][j];
            }
        }
        EditableTableModel model = new EditableTableModel(cols,res);
        model.addEditableCol(3);
        tableDisplay.setTableModel(model);
        tableDisplay.getAdapter().setCellColorRender(new TableCellRender());
    }

}