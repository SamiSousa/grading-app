package model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NewAssignmentForm extends JPanel{
    private JTextField txAssignmentName;
    private JTextField txMaxPoint;

    public NewAssignmentForm() {
        super();
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        setBorder(new EmptyBorder(2,8,0,8));
        txAssignmentName = new JTextField("",10);
        txMaxPoint = new JTextField("",10);
        JPanel panel = constructPair("Assignment name:",txAssignmentName);
        add(panel);
        panel = constructPair("Max Points:",txMaxPoint);
        add(panel);
    }
    public String getTxAssignmentName() {
        return txAssignmentName.getText();
    }

    public int getTxMaxPoint() {
        try{
            return Integer.parseInt(txMaxPoint.getText());
        } catch (NumberFormatException e){
            return 0;
        }
    }
    private JPanel constructPair(String item, JTextField field){
        JPanel panel = new JPanel(new GridLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;
        JLabel lbClassName = new JLabel(item);
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbClassName, cs);


        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(field,cs);

        lbClassName.setLabelFor(field);
        return panel;
    }

}

