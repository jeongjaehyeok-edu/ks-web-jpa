package kr.ac.ks.app.controller;

import kr.ac.ks.app.domain.Course;
import kr.ac.ks.app.domain.Lesson;
import kr.ac.ks.app.domain.Student;
import kr.ac.ks.app.repository.CourseRepository;
import kr.ac.ks.app.repository.LessonRepository;
import kr.ac.ks.app.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
public class CourseController {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public CourseController(StudentRepository studentRepository, CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/course")
    public String showCourseForm(Model model) {
        List<Student> students = studentRepository.findAll();
        List<Lesson> lessons = lessonRepository.findAll();
        model.addAttribute("students", students);
        model.addAttribute("lessons", lessons);
        return "courses/courseForm";
    }

    @PostMapping("/course")
    public String createCourse(@RequestParam("studentId") Long studentId,
                               @RequestParam("lessonId") Long lessonId
                               ) {
        Student student = studentRepository.findById(studentId).get();
        Lesson lesson = lessonRepository.findById(lessonId).get();
        lesson.setQuota(lesson.getQuota()-1);
        Course course = Course.createCourse(student,lesson);
        Course savedCourse = courseRepository.save(course);
        return "redirect:/courses";
    }

    @GetMapping("/courses")
    public String courseList(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "courses/courseList";
    }


    @GetMapping("/course/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        Course course = courseRepository.getOne(id);
        course.getLesson().setQuota(course.getLesson().getQuota()+1);
        courseRepository.delete(course);

        return "redirect:/courses";
    }

    @GetMapping("/course/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        List<Student> students = studentRepository.findAll();
        List<Lesson> lessons = lessonRepository.findAll();
        model.addAttribute("students", students);
        model.addAttribute("lessons", lessons);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));

        model.addAttribute("course",course);
        return "courses/update_course";
    }

    @PostMapping("/course/update/{id}")
    public String updateCourse(@RequestParam("studentId") Long studentId,
                               @RequestParam("lessonId") Long lessonId,
                               @Valid Lesson lesson,
                               @Valid Student student,
                               @Valid Course course,
                               @PathVariable("id") long id,
                               BindingResult result,
                               Model model){
        course.setStudent(studentRepository.findById(studentId).get());
        course.setLesson(lessonRepository.findById(lessonId).get());
        courseRepository.save(course);
        model.addAttribute("course",courseRepository.findAll());
        return "redirect:/courses";
    }

    @GetMapping("/courses/count")
    public String Count(Model model){
        List<Lesson> lessons = lessonRepository.findAll();
        List<Course> courses = courseRepository.findAll();
        Iterator<Lesson> iter_lessons = lessons.iterator();
        Iterator<Course> iter_courses = courses.iterator();


        Lesson lesson_temp = new Lesson();
        Course course_temp = new Course();
        int i = 0;
        while (iter_lessons.hasNext()) {
            lesson_temp=iter_lessons.next();
            iter_courses = courses.iterator();
            while(iter_courses.hasNext()){
                course_temp=iter_courses.next();
                if(lesson_temp.getId().longValue()==course_temp.getLesson().getId().longValue()){
                    lesson_temp.setQuota(lesson_temp.getQuota()-1);
                    lessons.set(i,lesson_temp);
                }
            }
            i++;
        }
        model.addAttribute("lessons",lessons);
        return "/courses/count";
    }
}