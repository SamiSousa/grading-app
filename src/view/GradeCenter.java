package view;

import component.EditableTableDisplay;
import component.collector.TableDataCollector;
import model.EditableTableModel;

import javax.swing.*;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GradeCenter extends JPanel{

    private String courseName;
    private String semester;
    private String title;
    private EditableTableDisplay tableDisplay;
    public GradeCenter(String courseName, String semester) {
        setLayout(new BorderLayout());

        this.courseName = courseName;
        this.semester = semester;
        title = "Grade center for "  + courseName + " in "+semester;

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

        Set<String> filterList = (new TableDataCollector()).getColData(2);
        filterList.add("All");
        JComboBox dropMenu = new JComboBox(filterList.toArray());
        dropMenu.addActionListener(new JComboBoxListener(2,tableDisplay));
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(dropMenu, cs);

        header.add(lbClassName, BorderLayout.NORTH);
        header.add(panel, BorderLayout.CENTER);
    }
    private void composeCenter() {

        tableDisplay = new EditableTableDisplay();
        EditableTableModel model = tableDisplay.getModel();

        model.addEditableCol(4);
        model.addEditableCol(5);

        tableDisplay.setTableModel(model);
        tableDisplay.setPanel(this);
        setCellColorRender();
    }
    private void setCellColorRender() {
        JTable table = tableDisplay.getAdapter().getTableModel();

        for (int i =0; i<table.getColumnCount();i++) {
            if(table.getColumnName(i).equals("Lost points")){
                table.getColumnModel().getColumn(i).setCellRenderer(new TableCellRender());
                break;
            }
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
    private Object[][] data;
    private int filterIndex;
    private EditableTableDisplay tableDisplay;
    public JComboBoxListener(int filterIndex, EditableTableDisplay tableDisplay){
        data = (new TableDataCollector()).getData();
        this.filterIndex = filterIndex;
        this.tableDisplay = tableDisplay;
    }
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        String petName = (String)cb.getSelectedItem();
        updateLabel(petName);
    }
    private void updateLabel(String filter){

        if(filter.equals("All")){
            EditableTableModel model = new EditableTableModel((new TableDataCollector()).getCols(),data);
            model.addEditableCol(4);
            model.addEditableCol(5);
            tableDisplay.setTableModel(model);
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
        EditableTableModel model = new EditableTableModel((new TableDataCollector()).getCols(),res);
        model.addEditableCol(4);
        model.addEditableCol(5);
        tableDisplay.setTableModel(model);
    }
}