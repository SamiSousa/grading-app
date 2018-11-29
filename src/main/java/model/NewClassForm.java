package model;

import component.EditableTableDisplay;
import component.Login;
import component.LoginDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewClassForm extends JPanel{
    private JTextField txClassName;
    public NewClassForm() {
        super();
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbClassName = new JLabel("Class Name: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbClassName, cs);

        txClassName = new JTextField("",25);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(txClassName,cs);

        lbClassName.setLabelFor(txClassName);

        add(panel,BorderLayout.NORTH);

        String[] configName = {"Assignment Category",
                "Assignment",
                "Weight",
                "Max points"
                };
        Object[][] configValue = {
                {"Midterm", "Midterm",
                        new Integer(20), new Integer(100)},
                {"Project", "p1",
                        new Integer(10), new Integer(100)},
                {"Project", "p2",
                        new Integer(15), new Integer(100)},
                {"Final", "Final",
                        new Integer(50), new Integer(100)},
                {"Attendance", "attendance",
                        new Integer(5), new Integer(40)},
        };
        JPanel tablePanel = new JPanel();
        tablePanel.setPreferredSize(new Dimension(200,120));
        tablePanel.setMaximumSize(new Dimension(200,120));
        EditableTableModel tableModel = new EditableTableModel(configName,configValue);
        tableModel.addEditableCol(0);
        tableModel.addEditableCol(1);
        tableModel.addEditableCol(2);
        tableModel.addEditableCol(3);
        EditableTableDisplay display = new EditableTableDisplay(this);
        display.setTableModel(tableModel);
        display.setPanel(tablePanel);

        add(tablePanel,BorderLayout.CENTER);
        setPreferredSize(new Dimension(500,200));
        setMaximumSize(new Dimension(500,200));
    }
    public String getClassName() {
        return txClassName.getText();
    }
}
