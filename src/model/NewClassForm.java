package model;

import component.Login;
import component.LoginDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewClassForm extends JPanel{
    public NewClassForm() {
        super();
        setLayout(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbClassName = new JLabel("Class Name: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        add(lbClassName, cs);

        JTextField txClassName = new JTextField("",25);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        add(txClassName,cs);

        lbClassName.setLabelFor(txClassName);

    }
}
