package model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NewCategoryForm extends JPanel {
    private JTextField categoryName;
    private JTextField categoryWeight;
    public NewCategoryForm(){
        super();
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        setBorder(new EmptyBorder(2,8,0,8));
        categoryName = new JTextField("",10);
        categoryWeight = new JTextField("",5);
        JPanel panel = constructPair("Category name:",categoryName);
        add(panel);
        panel = constructPair("Weight:",categoryWeight);
        add(panel);
    }

    public String getCategoryName() {
        return categoryName.getText();
    }

    public int getCategoryWeight() {

        try{
            return Integer.parseInt(categoryWeight.getText());
        }catch (NumberFormatException e){
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
