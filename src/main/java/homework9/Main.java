package homework9;

import com.github.javafaker.Faker;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    static List<Course> allCourses = new ArrayList<>();

    private static List<Student> getListOfStudents(){
        List<Student> students = new ArrayList<>();

        for (Program el: Program.values()) {
            allCourses.add(new Course(el));
        }
        Faker faker = new Faker();
        Random randomCourses = new Random();
        for(int i =0; i<100;i++){
            Set<Course> studentCourses = new HashSet<>();
            for(int j=0;j<randomCourses.nextInt(10); j++){
                studentCourses.add(allCourses.get(randomCourses.nextInt(25)));
            }
            Student student = new Student(faker.name().fullName(), studentCourses);
            students.add(student);
        } return students;
    }




    public static void main(String[] args) {
        List<Student> studentList = getListOfStudents();
        System.out.println(getListOfStudents());

        //1 задание
        Set<Course> distinctCourses = studentList.stream().flatMap(student -> student.courses.stream()).distinct().collect(Collectors.toSet());
        System.out.println(distinctCourses);
        System.out.println(distinctCourses.size());


        //2 задание
        studentList.stream()
                .filter(student -> student.courses.size() != 0)
                .sorted((student1,student2) -> student2.courses.size() - student1.courses.size())
                .limit(3)
                .forEach(System.out::println);


        //3 задание
        findStudentsByCourse(studentList, Program.ART);
    }

    public static void findStudentsByCourse(List<Student> students, Program nameCourse){
        for (Course course : allCourses) {
            if(course.getCourseName() == nameCourse) {
                Set<Student> names = students.stream().filter(student -> student.courses.contains(course)).collect(Collectors.toSet());
                System.out.println(names);
            }
        }
    }
}
