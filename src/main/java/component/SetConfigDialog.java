package component;


import database.SQLConnection;
import main.Config;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SetConfigDialog extends JDialog {

    private JLabel lblPassword;
    private JPasswordField txfPassword;
    private JLabel lblMySQLUser;
    private JTextField txfMySQLUser;
    private JLabel lblMySQLPassword;
    private JPasswordField txfMySQLPassword;
    private JButton save;
    private JButton exit;
    public boolean isDone = false;

    public SetConfigDialog(Frame parent) {
        super(parent, "Configure", true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;

        lblPassword = new JLabel("Set Password: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lblPassword, cs);

        txfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(txfPassword, cs);


        lblMySQLUser = new JLabel("MySQL User: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lblMySQLUser, cs);

        txfMySQLUser = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(txfMySQLUser, cs);


        lblMySQLPassword = new JLabel("MySQL Password: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lblMySQLPassword, cs);

        txfMySQLPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(txfMySQLPassword, cs);


        save = new JButton("Save Configuration");

        save.addActionListener(e -> {
            //todo connor make this save the values to properties. then make sql connection read from properties.
            Config.setMySQLPassword(getMySQLPassword());
            Config.setMySQLUser(getMySQLUser());
            Config.setPassword(getDesiredPassword());
            Config.save();
            SQLConnection.initialize(Config.getMySQLUser(), Config.getMySQLPassword());
            isDone = true;
            dispose();
        });

        exit = new JButton("Exit");
        exit.addActionListener(e -> {
            dispose();
        });
        JPanel bp = new JPanel();
        bp.add(save);
        bp.add(exit);


        panel.setBorder(new LineBorder(Color.GRAY));
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private String getDesiredPassword() {
        return new String(txfPassword.getPassword());
    }

    private String getMySQLUser() {
        return txfMySQLUser.getText().trim();
    }

    private String getMySQLPassword() {
        return new String(txfMySQLPassword.getPassword());
    }
}
