package homework9;

public class Course {

    private Program courseName;


    public Course(Program courseName){
        this.courseName=courseName;

    }

    @Override
    public String toString() {
        return "Course: " + courseName;
    }

    public Program getCourseName(){
      return courseName;
}
}
