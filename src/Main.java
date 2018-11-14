import java.awt.*;
import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Grading system");


        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new FlowLayout());

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);

        LoginDialog loginDlg = new LoginDialog(frame);

        loginDlg.setVisible(true);

        // if login successfully
        if(loginDlg.isSucceeded()) {
            loginDlg.dispose();
            frame.setVisible(true);
        }
        if(loginDlg.isClosed()) {
            loginDlg.dispose();
            frame.dispose();
        }
    }
}

