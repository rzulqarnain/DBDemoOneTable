package models;

import exceptions.CourseException;

public class Course {

    // must match field names exactly!!!
    public final static String ID = "id";
    public final static String NAME = "name";
    public final static String CREDITS = "credits";

    private String id;
    private String name;
    private int credits;

    public Course(String id, String name, int credits) throws CourseException {
        setId(id);
        setName(name);
        setCredits(credits);
    }

    public Course(String id, String name, String credits) throws CourseException {
        setId(id);
        setName(name);
        setCredits(credits);
    }

    public final void setId(String id) throws CourseException {
        if (id.isEmpty()) {
            throw new CourseException("ID cannot be empty");
        }
        this.id = id;
    }

    public final void setName(String name) throws CourseException {
        if (id.isEmpty()) {
            throw new CourseException("Name cannot be empty");
        }
        this.name = name;
    }

    public final void setCredits(int credits) throws CourseException {
        if (credits < 0 || credits > 10) {
            throw new CourseException("Credits out of range");
        }
        this.credits = credits;
    }

    public final void setCredits(String credits) throws CourseException {
        try {
            setCredits(Integer.parseInt(credits));
        } catch (NumberFormatException e) {
            throw new CourseException("Credits must be a number");
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        return name + " (" + id + "), credits = " + credits;
    }

}
