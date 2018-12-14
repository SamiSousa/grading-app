package view;

import component.EditableTableDisplay;
import component.collector.TableDataCollector;
import data.Assignment;
import data.Grade;
import data.Student;
import database.*;
import model.CourseNode;
import model.EditableTableModel;
import model.ExcelViewModel;
import model.GradeModel;
import org.omg.CORBA.PRIVATE_MEMBER;

import javax.swing.*;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
        EditableTableModel model = loadData();
        tableDisplay.setTableModel(model);
        tableDisplay.setPanel(this);
        tableDisplay.getAdapter().getTableModel().getModel().addTableModelListener(GradeListener.getGradeListenerInstance(gradeList,"All"));
    }
    private EditableTableModel loadData() {
        GetAllAssignmentForStudentInClass query = new GetAllAssignmentForStudentInClass(course.getClassModel().ClassID);
        List<Grade> res = query.execute();
        for(int i=0;i<res.size();i++){
            Grade g = res.get(i);
            GetGrade que = new GetGrade(g.getAssignment().getAssignmentId(),g.getStudent().getStudentID());
            List<Integer> ress = que.execute();
            if(ress.size() == 0){
                InsertGrade iGrade = new InsertGrade(g.getAssignment().getAssignmentId(), g.getStudent().getStudentID());
                iGrade.execute();
            } else {
                g.setLostPoints(ress.get(0));
                res.set(i,g);
            }
        }
        if (res.size() == 0) {
            // there is no student
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

    public CourseNode getCourse() {
    	return this.course;
    }

}
class GradeListener implements TableModelListener {
    private Map<Integer,GradeModel> map;
    private String filter;
    private List<Integer> gradeIds;
    private static GradeListener gradeListenerInstance;

    private GradeListener(Map<Integer,GradeModel> map,String filter){
        this.filter = filter;
        this.map = map;
        this.gradeIds = new ArrayList<>();
    }
    public static GradeListener getGradeListenerInstance(Map<Integer,GradeModel> map,String filter){
        if(gradeListenerInstance == null){
            gradeListenerInstance = new GradeListener(map,filter);
        } else{
            gradeListenerInstance.setFilter(filter);
            gradeListenerInstance.setMap(map);
        }
        return gradeListenerInstance;
    }

    public void setMap(Map<Integer, GradeModel> map) {
        this.map = map;
    }

    public void setFilter(String filter){
        this.filter = filter;
    }
    public void setGradeIds(List<Integer> gradeIdList){
        this.gradeIds = gradeIdList;
    }
    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int col = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        Object data = model.getValueAt(row, col);
        String name = (String) model.getValueAt(row, 0);
        GetStudentByName queryStudent = new GetStudentByName(name);
        Student temp = queryStudent.execute().get(0);
        GradeModel changedModel = map.get(temp.getStudentID());
        Grade changedGrade;

        if(filter.equals("All")){
            changedGrade = changedModel.getGrades().get(col-1);
            changedModel.setGrade(col-1,changedGrade);
        } else {
            changedGrade = changedModel.getGrades().get(gradeIds.get(row));
            changedModel.setGrade(gradeIds.get(row),changedGrade);
        }

        int assignmentId = changedGrade.getAssignment().getAssignmentId();
        int studentId = changedGrade.getStudent().getStudentID();
        changedGrade.setLostPoints((int) data);
        map.put(temp.getStudentID(),changedModel);

        GetGrade query = new GetGrade(assignmentId, studentId);
        List<Integer> res = query.execute();
        if(res.size() == 0){
            InsertGrade iGrade = new InsertGrade(assignmentId, studentId);
            iGrade.execute();
        }
        UpdateGrade uGrade = new UpdateGrade((int) data, assignmentId, studentId);
        uGrade.execute();

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
    private List<Integer> stuIds;
    public JComboBoxListener(Map<Integer,GradeModel> map, EditableTableDisplay tableDisplay, int filterindex){
        this.map = map;
        this.tableDisplay = tableDisplay;
        this.filterIndex=filterindex;
        this.stuIds = new ArrayList<>();
        ExcelViewModel viewModel = constructGradingView(map);
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
            for(int j=1;j<excelModel.getColCounts();j++){
                model.addEditableCol(j);
            }
            tableDisplay.setTableModel(model);
//            tableDisplay.getAdapter().setCellColorRender(new TableCellRender());
            GradeListener currentGradeListener = GradeListener.getGradeListenerInstance(map,"All");
            tableDisplay.getAdapter().getTableModel().getModel().removeTableModelListener(currentGradeListener);
            tableDisplay.getAdapter().getTableModel().getModel().addTableModelListener(currentGradeListener);
            return;
        }
        ExcelViewModel excelModel = constructGradingView(map);
        data = excelModel.getData();
        int leng = data[0].length;
        List<Integer> list = new ArrayList<>();
        List<Integer> indexList = new ArrayList<>();
        for(int i=0;i<data.length;i++){
            if(data[i][filterIndex].equals(filter)){
                indexList.add(i);
                list.add(stuIds.get(i));
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

        GradeListener currentGradeListener = GradeListener.getGradeListenerInstance(map,filter);
        tableDisplay.getAdapter().getTableModel().getModel().removeTableModelListener(currentGradeListener);
        currentGradeListener.setGradeIds(list);
        tableDisplay.getAdapter().getTableModel().getModel().addTableModelListener(currentGradeListener);
    }
    private ExcelViewModel constructGradingView(Map<Integer, GradeModel> gradeList){
        String[] cols = new String[]{"Name","Assignment","Max Points","Lost Points"};
        stuIds = new ArrayList<>();
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
                stuIds.add(j);
                i++;
            }
        }
        return new ExcelViewModel(cols,data,new HashSet<>());

    }

}