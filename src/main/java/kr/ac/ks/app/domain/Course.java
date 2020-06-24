package kr.ac.ks.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name ="student_id")
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    private Student student;


    @JoinColumn(name ="lesson_id")
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    private Lesson lesson;

    public Course(){

    }

    public void setStudent(Student student) {
        if(this.student != null){
            this.student.getCourses().remove(this);
        }
        this.student = student;
        if(student != null){
            this.student.getCourses().add(this);
        }
    }

    public void setLesson(Lesson lesson) {
        if(this.lesson != null){
            this.lesson.getCourses().remove(this);
        }
        this.lesson = lesson;
        if(lesson != null){
            this.lesson.getCourses().add(this);
        }
    }

    public static Course createCourse(Student student, Lesson... lessons) {
        Course course = new Course();
        course.setStudent(student);
        Arrays.stream(lessons).forEach(course::setLesson);
        return course;
    }
}
