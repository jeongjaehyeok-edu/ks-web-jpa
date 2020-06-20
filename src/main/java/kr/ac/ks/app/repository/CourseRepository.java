package kr.ac.ks.app.repository;

import kr.ac.ks.app.domain.Course;
import kr.ac.ks.app.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "select " +
            "            to_char(Lesson.id), " +
            "            to_char(Lesson.name), " +
            "           to_char(Lesson.quota-count(Lesson.id))  " +
            "   from " +
            "       Course, " +
            "       Lesson " +
            "where 1=1 " +
            "   and Course.lesson_id = Lesson.id " +
            " group by Lesson.id,Lesson.name,Lesson.quota " +
            " order by Lesson.id asc ",nativeQuery = true)
    List<Lesson>  findByGroupCount();
}