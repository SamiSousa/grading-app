package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Student implements TableReady {
	
	private String firstName;
	private String lastName;
	private String buID;
	private String email; // Primary key
	private String year;
	
	public Student(String firstName, String lastName, String BUID, String email, String year) {
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
			students[i] = new Student(studentInfo[0], studentInfo[1], studentInfo[2], studentInfo[3], studentInfo[4]);
			i++;
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
		String[] cols = {"First Name", "Last Name", "BUID", "Email", "Year"};
		return cols;
	}
	
	public String toString() {
		return String.format("Name: %s, BUID: %s, Email: %s, Year: %s\n", this.firstName + " " + this.lastName, this.buID, this.email, this.year);
	}
}
