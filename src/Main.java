import component.EditableTableDisplay;
import component.LoginDialog;
import model.EditableTableModel;
import view.MainMenu;

import java.awt.*;
import javax.swing.*;


public class Main {
    private static JFrame curWindow;
    private static JPanel topPanel;
    public static void main(String[] args) {
        curWindow = new JFrame("Grading system");
        topPanel = new JPanel();

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

            curWindow.getContentPane().add(topPanel);
            topPanel.setLayout(null);

            constructMainView();

            curWindow.setVisible(true);
        }
        if(loginDlg.isClosed()) {
            loginDlg.dispose();
            curWindow.dispose();
        }
    }
    private static void constructMainView() {

        MainMenu menu = MainMenu.getMainMenuInstance(curWindow);
        menu.setPanel(topPanel);
        EditableTableDisplay tableDisplay = new EditableTableDisplay();
        EditableTableModel model = tableDisplay.getModel();

        model.addEditableCol(3);
        model.addEditableCol(4);

        tableDisplay.setTableModel(model);
        tableDisplay.setPanel(topPanel);
    }
}

