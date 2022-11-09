package homework9;

import java.util.List;
import java.util.Set;

public class Student {

    private String name;
    Set<Course> courses;

    public Student(String name, Set<Course> courses){
        this.courses = courses;
        this.name = name;
    }

    String getName() {
        return name;
    }

    Set<Course> getAllCourses() {
        return courses;
    }

    @Override
    public String toString() {
        return "Name: " + name + " has courses: " + courses;
    }
}
