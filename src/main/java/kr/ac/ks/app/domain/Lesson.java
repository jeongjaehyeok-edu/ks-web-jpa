package kr.ac.ks.app.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Lesson {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int quota;

    @OneToMany
    @JoinColumn(name = "lesson_id", updatable = false)
    private List<Course> courses = new ArrayList<>();

    public Lesson() {
    }

    public Lesson(long id,String name,int quota) {
        this.id=id;
        this.name=name;
        this.quota=quota;
    }
    public Lesson(String id,String name,String quota) {
        this.id=Long.parseLong(id);
        this.name=name;
        this.quota=Integer.parseInt(quota);
    }

    @Builder
    public Lesson(String name, int quota) {
        this.name = name;
        this.quota = quota;
    }
}
