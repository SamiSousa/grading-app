package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Student implements TableReady {

	private int studentID;
	private String firstName;
	private String lastName;
	private String buID;
	private String email; // Primary key
	private String year;
	
	public Student(int studentID, String firstName, String lastName, String BUID, String email, String year) {
		this.studentID = studentID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.buID = BUID;
		this.email = email;
		this.year = year;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}

	public String getBuID() {
		return buID;
	}

	public String getEmail() {
		return email;
	}

	public String getYear() {
		return year;
	}
	
	public static Student[] loadStudentsFromFile(File f) {
		// Load Student objects from formatted file
		// Return null on fail
		Student[] students = null;
		ArrayList<String[]> data = new ArrayList<String[]>();
		try {
			Scanner input = new Scanner(f);
			while(input.hasNextLine()) {
				String line = input.nextLine();
				Scanner commas = new Scanner(line);
				commas.useDelimiter(",");
				String[] lineData = new String[5];
				int i = 0;
				while(commas.hasNext()) {
					if (i > 4) {
						// Too many values
						break;
					}
					lineData[i] = commas.next();
					i++;
				}
				data.add(lineData);
				commas.close();
			}
			input.close();
		} catch (FileNotFoundException e) {
			return null;
		}
		
		students = new Student[data.size()];
		
		Iterator<String[]> it = data.iterator();
		int i = 0;
		while(it.hasNext()) {
			String[] studentInfo = it.next();
			students[i] = new Student(-1, studentInfo[0], studentInfo[1], studentInfo[2], studentInfo[3], studentInfo[4]);
			i++;
			System.out.println(students[i]);
		}
		
		return students;
	}
	
	public Object[] getDataRow() {
		Object[] row = new Object[5];
		
		/* TODO: fill in row with data fields */
		row[0] = this.firstName;
		row[1] = this.lastName;
		row[2] = this.buID;
		row[3] = this.email;
		row[4] = this.year;
		
		return row;
	}
	
	public String[] getDataColumns() {
		return Student.getStudentDataColumns();
	}
	
	public static String[] getStudentDataColumns() {
		String[] cols = {"First Name", "Last Name", "BUID", "Email", "Year"};
		return cols;
	}
	
	public static Student getDefaultStudent() {
		return new Student(-1, "", "", "", "", "");
	}
	
	public String toString() {
		return String.format("Name: %s, BUID: %s, Email: %s, Year: %s\n", this.firstName + " " + this.lastName, this.buID, this.email, this.year);
	}

	public int getStudentID() {
		return studentID;
	}
}
