package kr.ac.ks.app.repository;

import kr.ac.ks.app.domain.Course;
import kr.ac.ks.app.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

}