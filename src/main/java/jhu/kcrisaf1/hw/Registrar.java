package jhu.kcrisaf1.hw;

import java.util.HashMap;
import java.util.List;

public class Registrar {

    private int courseNumber;
    private List<Integer> studentList;
    
    public Registrar(int courseNumber, List<Integer> studentList) {
        this.courseNumber = courseNumber;
        this.studentList = studentList;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public List<Integer> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Integer> studentList) {
        this.studentList = studentList;
    }
}
