package view;

import component.EditableTableDisplay;
import model.EditableTableModel;

import javax.swing.*;
import java.awt.*;

public class GradeCenter extends JPanel{

    private String courseName;
    private String semester;
    private String title;
    public GradeCenter(String courseName, String semester) {
        setLayout(new BorderLayout());

        this.courseName = courseName;
        this.semester = semester;
        title = "Grade center for "  + courseName + " in "+semester;

        composeCenter();
    }
    private void composeCenter() {
        JLabel lbClassName = new JLabel(title,SwingConstants.CENTER);

        lbClassName.setFont(lbClassName.getFont().deriveFont (16.0f));

        add(lbClassName, BorderLayout.NORTH);

        EditableTableDisplay tableDisplay = new EditableTableDisplay();
        EditableTableModel model = tableDisplay.getModel();

        model.addEditableCol(3);
        model.addEditableCol(4);

        tableDisplay.setTableModel(model);
        tableDisplay.setPanel(this);
    }
}
