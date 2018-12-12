package model;


public class Semester {

    private String name;
    private int id;

    public Semester(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
