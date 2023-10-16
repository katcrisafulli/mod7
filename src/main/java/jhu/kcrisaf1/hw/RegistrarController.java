package jhu.kcrisaf1.hw;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Date;
import java.util.HashMap;

@RestController
public class RegistrarController {

    private List<Student> students = new ArrayList();
    private final AtomicInteger studentCounter = new AtomicInteger();

    private List<Course> courses = new ArrayList();

    private HashMap<Integer, Registrar> registrars = new HashMap<Integer, Registrar>();
    private int maxStudentListSize = 15;

    public RegistrarController() {
        Student student1 = new Student(studentCounter.incrementAndGet(), "Kathryn", "Crisafulli", "07/08/1996",
                "katcrisafulli@gmail.com");
        Student student2 = new Student(studentCounter.incrementAndGet(), "Test", "Student1", "01/01/2023",
                "test@gmail.com");
        Course course1 = new Course(123, "Test Course1");

        students.add(student1);
        students.add(student2);
        courses.add(course1);

        List<Integer> list = new ArrayList();
        list.add(student1.getStudentId());
        list.add(student2.getStudentId());
        Registrar registrar1 = new Registrar(course1.getCourseNumber(), list);

        registrars.put(course1.getCourseNumber(), registrar1);
    }

    @GetMapping(value = "/")
    public ResponseEntity getAll() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("students", students);
        map.put("courses", courses);
        map.put("registrar", registrars);
        return ResponseEntity.ok(map);
    }

    // Students
    @GetMapping(value = "/students")
    public ResponseEntity getStudents() {
        return ResponseEntity.ok(students);
    }

    @GetMapping(value = "/students", params = "studentId")
    public ResponseEntity getStudent(@RequestParam(value = "studentId") Integer studentId) {

        Student returnValue = null;
        for (Student temp : students) {
            if (temp.getStudentId() == studentId) {
                returnValue = temp;
            }
        }
        return ResponseEntity.ok(returnValue);
    }

    @PostMapping(value = "/students")
    public ResponseEntity addStudent(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName") String lastName,
            @RequestParam(value = "dateOfBirth") String dateOfBirth,
            @RequestParam(value = "email") String email) {
        students.add(new Student(studentCounter.incrementAndGet(), firstName, lastName, dateOfBirth, email));
        return ResponseEntity.ok(students);
    }

    @PutMapping(value = "/students")
    public ResponseEntity updateStudent(
            @RequestParam(value = "studentId") Integer studentId,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName") String lastName,
            @RequestParam(value = "dateOfBirth") String dateOfBirth,
            @RequestParam(value = "email") String email) {
        ResponseEntity response = new ResponseEntity(HttpStatus.NOT_MODIFIED);
        for (Student temp : students) {
            if (temp.getStudentId() == studentId) {
                if (firstName != null) {
                    temp.setFirstName(firstName);
                }
                temp.setLastName(lastName);
                temp.setDateOfBirth(dateOfBirth);
                temp.setEmail(email);
                response = ResponseEntity.ok(students);
            }
        }
        return response;
    }

    @DeleteMapping(value = "/students")
    public ResponseEntity removeStudent(@RequestParam(value = "studentId") Integer studentId) {
        ResponseEntity response = new ResponseEntity(HttpStatus.NOT_MODIFIED);
        for (Student temp : students) {
            if (temp.getStudentId() == studentId) {
                students.remove(temp);
                response = ResponseEntity.ok(students);
            }
        }
        return response;
    }

    // Courses
    @GetMapping(value = "/courses")
    public ResponseEntity getCourses() {
        return ResponseEntity.ok(courses);
    }

    @GetMapping(value = "/courses", params = "courseNumber")
    public ResponseEntity getCourse(@RequestParam(value = "courseNumber") Integer courseNumber) {
        Course returnValue = null;
        for (Course temp : courses) {
            if (temp.getCourseNumber() == courseNumber) {
                returnValue = temp;
            }
        }
        return ResponseEntity.ok(returnValue);
    }

    @PostMapping(value = "/courses")
    public ResponseEntity addCourse(@RequestParam(value = "courseNumber") Integer courseNumber,
            @RequestParam(value = "courseTitle") String courseTitle) {
        courses.add(new Course(courseNumber, courseTitle));
        return ResponseEntity.ok(courses);
    }

    @PutMapping(value = "/courses")
    public ResponseEntity updateCourse(@RequestParam(value = "courseNumber") Integer courseNumber,
            @RequestParam(value = "courseTitle") String courseTitle) {
        ResponseEntity response = new ResponseEntity(HttpStatus.NOT_MODIFIED);
        for (Course temp : courses) {
           if (temp.getCourseNumber() == courseNumber) {
                temp.setCourseTitle(courseTitle);
                response = ResponseEntity.ok(courses);
            }   
        }
        return response;
    }

    @DeleteMapping(value = "/courses")
    public ResponseEntity removeCourse(@RequestParam(value = "courseNumber") Integer courseNumber) {
        ResponseEntity response = new ResponseEntity(HttpStatus.NOT_MODIFIED);
        for (Course temp : courses) {
            if (temp.getCourseNumber() == courseNumber) {
                courses.remove(temp);
                response = ResponseEntity.ok(courses);
            }   
        }
        return response;
    }

    // Registrar
    @GetMapping(value = "/registrars")
    public ResponseEntity getRegistrars() {
        return ResponseEntity.ok(registrars);
    }

    @GetMapping(value = "/registrars", params = "courseNumber")
    public ResponseEntity getRegistrar(@RequestParam(value = "courseNumber") Integer courseNumber) {
        
        ResponseEntity response;
        Registrar returnValue = registrars.get(courseNumber);

        if (returnValue == null) {
            response = new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            response = ResponseEntity.ok(returnValue);
        }

        return response;

    }

    @PostMapping(value = "/registrars")
    public ResponseEntity addRegistrar(@RequestParam(value = "courseNumber") Integer courseNumber,
            @RequestParam(value = "studentId") Integer studentId) {

        ResponseEntity response = new ResponseEntity<HashMap<Integer, Registrar>>(registrars, HttpStatus.NOT_FOUND);
        Boolean courseFound = false;
        Boolean studentFound = false;
        Boolean studentRegistered = false;

        // Make sure course and student exist
        for (Course temp : courses) {
            if (temp.getCourseNumber() == courseNumber)
                courseFound = true;
        }
        for (Student temp : students) {
            if (temp.getStudentId() == studentId)
                studentFound = true;
        }

        // Check for existing registrar
        if (courseFound && studentFound) {
            Registrar temp = registrars.get(courseNumber);
            if (temp != null) {
                List<Integer> tempList = temp.getStudentList();
                for (Integer id : tempList) {
                    if (id == studentId) {
                        studentRegistered = true;
                    }
                }
                // Check if student already registered
                if (studentRegistered) {
                    response = ResponseEntity.ok(registrars);
                } else {
                    if (studentRegistered == false) {
                        // Check student list size
                        if (tempList.size() < maxStudentListSize) {
                            tempList.add(studentId);
                            temp.setStudentList(tempList);
                            response = ResponseEntity.ok(registrars);
                        } else {
                            response = new ResponseEntity<HashMap<Integer, Registrar>>(registrars,
                                    HttpStatus.FORBIDDEN);
                        }
                    }
                }
            } else {
                // Create new registrar
                List<Integer> list = new ArrayList();
                list.add(studentId);
                temp = new Registrar(courseNumber, list);
                response = ResponseEntity.ok(registrars);
            }
        }

        return response;
    }

    @DeleteMapping(value = "/registrars")
    public ResponseEntity removeRegistrar(@RequestParam(value = "courseNumber") Integer courseNumber,
            @RequestParam(value = "studentId") Integer studentId) {

        ResponseEntity response = new ResponseEntity(HttpStatus.NOT_MODIFIED);
        
        Registrar temp = registrars.get(courseNumber);
        if (temp != null) {
            List tempList = temp.getStudentList();
            tempList.remove(studentId);
            temp.setStudentList(tempList);
            response = ResponseEntity.ok(registrars);
        }
        return response;
    }
}