package kr.ac.ks.app.repository;


import kr.ac.ks.app.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query(value = "select " +
            "            Lesson.id, " +
            "            Lesson.name, " +
            "           Lesson.quota-count(Lesson.id) as \"quota\" " +
            "   from " +
            "       Course, " +
            "       Lesson " +
            "where 1=1 " +
            "   and Course.lesson_id = Lesson.id " +
            " group by Lesson.id,Lesson.name,Lesson.quota " +
            " order by Lesson.id asc ",nativeQuery = true)
    List<Lesson> findByGroupCount();
}
