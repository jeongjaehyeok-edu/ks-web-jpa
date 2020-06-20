package kr.ac.ks.app.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Student {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String email;

    @OneToMany
    @JoinColumn(name = "student_id", updatable = false)
    private List<Course> courses = new ArrayList<>();

    public Student() {
    }

    @Builder
    public Student(String name, String email) {
        this.name = name;
        this.email = email;
    }
    @Builder
    public Student(Long id,String name, String email) {
        this.id=id;
        this.name = name;
        this.email = email;
    }
}
