package view;

import component.EditableTableDisplay;
import data.Student;
import model.EditableTableModel;

import javax.swing.*;
import java.io.File;

public class StudentInfo extends JPanel{
    public StudentInfo(){
        Student[] stuList = Student.loadStudentsFromFile(new File("/Users/richard/Desktop/fall18/cs591 d1/grade/grading-app/src/view/stu.txt"));
        EditableTableDisplay display = new EditableTableDisplay(this);
        Object[][] data = new Object[stuList.length][stuList[0].getDataRow().length];
        for(int i=0;i<stuList.length;i++){
            data[i] = stuList[i].getDataRow();
            System.out.println(data);
        }
        EditableTableModel model = new EditableTableModel(stuList[0].getDataColumns(),data);
        display.setTableModel(model);
        display.setPanel(this);

    }
}
