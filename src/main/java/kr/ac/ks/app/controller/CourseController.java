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
import java.util.List;

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
        System.out.println(id+"i~~~~~~~d~~~~~~~");
        Course course = courseRepository.getOne(id);
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

    @GetMapping("/course_count")
    public String courseCount(Model model){
        List<Lesson> counts = courseRepository.findByGroupCount();
        model.addAttribute("counts",counts);
        return "courses/course_count";
    }
}