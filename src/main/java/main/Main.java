package main;

import component.LoginDialog;
import view.MainMenu;

import java.awt.*;
import javax.swing.*;


public class Main {
    private static JFrame curWindow;
    private static JPanel topPanel;
    private static JPanel menuPanel;
    private static JPanel contentPanel;
    public static void main(String[] args) {
        curWindow = new JFrame("Grading system");

        curWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        curWindow.setSize(800, 600);
        curWindow.setLayout(new BorderLayout());

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        curWindow.setLocation(dim.width/2-curWindow.getSize().width/2, dim.height/2-curWindow.getSize().height/2);


        LoginDialog loginDlg = new LoginDialog(curWindow);

        loginDlg.setVisible(true);

        // if login successfully
        if(loginDlg.isSucceeded()) {
            loginDlg.dispose();

            configTopPanel();

            curWindow.getContentPane().add(topPanel);
//            topPanel.setLayout(null);

            constructMainView();

            curWindow.setVisible(true);
        }
        if(loginDlg.isClosed()) {
            loginDlg.dispose();
            curWindow.dispose();
        }
    }
    private static void constructMainView() {

        MainMenu menu = MainMenu.getMainMenuInstance(contentPanel);
        menu.setPanel(menuPanel);
//        EditableTableDisplay tableDisplay = new EditableTableDisplay(contentPanel);
//        EditableTableModel model = tableDisplay.getModel();
//
//        model.addEditableCol(4);
//        model.addEditableCol(5);
//
//        tableDisplay.setTableModel(model);
//        tableDisplay.setPanel(contentPanel);
        
    }
    private static void configTopPanel() {
        topPanel = new JPanel();
        menuPanel = new JPanel();
        contentPanel = new JPanel();

        menuPanel.setLayout(new GridLayout(0, 1));
        menuPanel.setMaximumSize(new Dimension(180,600));
        menuPanel.setPreferredSize(new Dimension(180,600));
        contentPanel.setLayout(new GridLayout(0, 1));

        topPanel.setLayout(new BorderLayout());
        topPanel.add(menuPanel,BorderLayout.WEST);
        topPanel.add(contentPanel,BorderLayout.CENTER);
    }
}

