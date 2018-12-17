package view;

import component.EditableTableDisplay;
import data.Assignment;
import data.Grade;
import data.Student;
import database.*;
import model.CourseNode;
import model.EditableTableModel;
import model.ExcelViewModel;
import model.GradeModel;

import javax.swing.*;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        JComboBoxListener jcbl = new JComboBoxListener(gradeList,tableDisplay,1, this.getCourse().getClassModel().ClassID);
        GradeListener.setJComboBoxListener(jcbl);
        dropMenu.addActionListener(jcbl);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(dropMenu, cs);

        JButton export = new JButton("Export to excel");
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File scores = new File("scores.xls");
                toExcel(tableDisplay.getAdapter().getTableModel(),scores);
            }
        });
        cs.gridx = 3;
        cs.gridy = 1;
        panel.add(export,cs);

        header.add(lbClassName, BorderLayout.NORTH);
        header.add(panel, BorderLayout.CENTER);
    }
    private void toExcel(JTable table, File file){
        try{
            TableModel model = table.getModel();
            FileWriter excel = new FileWriter(file);

            for(int i = 0; i < model.getColumnCount(); i++){
                excel.write(model.getColumnName(i) + "\t");
            }

            excel.write("\n");

            for(int i=0; i< model.getRowCount(); i++) {
                for(int j=0; j < model.getColumnCount(); j++) {
                    excel.write(model.getValueAt(i,j).toString()+"\t");
                }
                excel.write("\n");
            }

            excel.close();

        }catch(IOException e){ System.out.println(e); }
    }
    private void composeCenter() {
        tableDisplay = new EditableTableDisplay(this);
        EditableTableModel model = loadData();
        tableDisplay.setTableModel(model);
        tableDisplay.setPanel(this);
        tableDisplay.getAdapter().getTableModel().getModel().addTableModelListener(GradeListener.getGradeListenerInstance(gradeList,"All",course.getClassModel().ClassID));
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

            //TODO connor set final grades
            Map<Integer, Double> finalGrades = calculateFinalGrades(gradeList, this.course.getClassModel().ClassID);

            ExcelViewModel excelModel = constructExcelView(gradeList, colSet, finalGrades);
            EditableTableModel model = new EditableTableModel(excelModel.getCols(),excelModel.getData());
            for(int j=1;j<excelModel.getColCounts();j++){
                model.addEditableCol(j);
            }
            return model;
        }

    }

    public static Map<Integer, Double> calculateFinalGrades(Map<Integer, GradeModel> grades, int classID) {
        GetCategoriesInClass categories = new GetCategoriesInClass(classID);
        List<Map.Entry<Integer, Integer>> categoryWeightsList = categories.execute();
        Map<Integer, Integer> categoryWeights = new HashMap<>();
        for(Map.Entry<Integer, Integer> entry: categoryWeightsList) {
            categoryWeights.put(entry.getKey(), entry.getValue());
        }

        Map<Integer, Double> finalGrades = new HashMap<Integer, Double>();
        for(Integer studentID: grades.keySet()) {
            List<Grade> gradeList = grades.get(studentID).getGrades();
            double finalGrade = 0;
            for(Grade g: gradeList) {
                double gradeDouble = (g.getAssignment().getMaxPoints() - g.getLostPoints()) * 1.0 /g.getAssignment().getMaxPoints() * g.getAssignment().getWeight();
                finalGrade += gradeDouble * categoryWeights.get(g.getAssignment().getCategoryId())* 1.0 / 100;
            }
            finalGrades.put(studentID, finalGrade);
        }
        return finalGrades;
    }
    public static ExcelViewModel constructExcelView(Map<Integer, GradeModel> gradeList, Set<String> colSet, Map<Integer, Double> finalGrades){
        List<String> colNames = new ArrayList<>();
        colNames.add("Name");
        for(int stuId :gradeList.keySet()){
            GradeModel grade = gradeList.get(stuId);
            List<Grade> grades = grade.getGrades();
            for(int j = 0;j<grades.size();j++){
                Assignment a = grades.get(j).getAssignment();
                String col = a.getName();
//                    col += " (W: "+a.getWeight();
                col += " (MP: "+a.getMaxPoints()+")";
                colNames.add(col);
                colSet.add(a.getName());
            }
            break;
        }
        colNames.add("Final Grade");
        Object[][] data = new Object[gradeList.size()][colNames.size()];
        int i = 0;
        for(int stuId :gradeList.keySet()) {
            GradeModel grade = gradeList.get(stuId);
            List<Grade> grades = grade.getGrades();
            data[i][0] = grade.getStudent().getFirstName()+" "+grade.getStudent().getLastName();
            for(int j = 1;j<colNames.size()-1;j++){
                data[i][j] = grades.get(j-1).getLostPoints();
            }

            data[i][colNames.size()-1] = finalGrades.get(stuId);
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
    private int classId;
    private JComboBoxListener jcblistener;

    private GradeListener(Map<Integer,GradeModel> map,String filter, int classId){
        this.filter = filter;
        this.map = map;
        this.gradeIds = new ArrayList<>();
        this.classId = classId;
    }
    public static GradeListener getGradeListenerInstance(Map<Integer,GradeModel> map,String filter, int classId){
        if(gradeListenerInstance == null){
            gradeListenerInstance = new GradeListener(map,filter,classId);
        } else{
            gradeListenerInstance.setFilter(filter);
            gradeListenerInstance.setMap(map);
            gradeListenerInstance.setClassId(classId);
        }
        return gradeListenerInstance;
    }

    public static void setJComboBoxListener(JComboBoxListener jcblistener) {
        if(gradeListenerInstance != null){
            gradeListenerInstance.jcblistener = jcblistener;
        }
    }

    public void setClassId(int classId) {
        this.classId = classId;
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
        GetStudentByName queryStudent = new GetStudentByName(name,classId);
        Student temp = queryStudent.execute().get(0);
        GradeModel changedModel = map.get(temp.getStudentID());
        Grade changedGrade;
        if(changedModel == null){
            System.out.println(map.entrySet());
        }
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
        if(filter.equals("All")) {
            jcblistener.updateLabel("All");
        }
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
    private int classID;
    public JComboBoxListener(Map<Integer,GradeModel> map, EditableTableDisplay tableDisplay, int filterindex, int classID){
        this.map = map;
        this.tableDisplay = tableDisplay;
        this.filterIndex=filterindex;
        this.classID = classID;
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
    public void updateLabel(String filter){

        if(filter.equals("All")){
            //TODO connor set final grades
            Map<Integer, Double> finalGrades = GradeCenter.calculateFinalGrades(map, this.classID);
            ExcelViewModel excelModel = GradeCenter.constructExcelView(map, new HashSet<>(), finalGrades);
            EditableTableModel model = new EditableTableModel(excelModel.getCols(),excelModel.getData());
            for(int j=1;j<excelModel.getColCounts();j++){
                model.addEditableCol(j);
            }
            tableDisplay.setTableModel(model);
//            tableDisplay.getAdapter().setCellColorRender(new TableCellRender());
            GradeListener currentGradeListener = GradeListener.getGradeListenerInstance(map,"All",classID);
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

        GradeListener currentGradeListener = GradeListener.getGradeListenerInstance(map,filter,classID);
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