package ru.msu.cmc.webprac.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.msu.cmc.webprac.dao.ClassroomDAO;
import ru.msu.cmc.webprac.dao.CourseDAO;
import ru.msu.cmc.webprac.dao.FacultyDAO;
import ru.msu.cmc.webprac.dao.LessonDAO;
import ru.msu.cmc.webprac.dao.LessonGroupDAO;
import ru.msu.cmc.webprac.dao.StudentCourseDAO;
import ru.msu.cmc.webprac.dao.StudentDAO;
import ru.msu.cmc.webprac.dao.StudyGroupDAO;
import ru.msu.cmc.webprac.dao.TeacherDAO;
import ru.msu.cmc.webprac.model.Classroom;
import ru.msu.cmc.webprac.model.Course;
import ru.msu.cmc.webprac.model.CourseType;
import ru.msu.cmc.webprac.model.Lesson;
import ru.msu.cmc.webprac.model.StudentCourse;
import ru.msu.cmc.webprac.model.StudyGroup;
import ru.msu.cmc.webprac.service.StudentPortalService;
import ru.msu.cmc.webprac.service.StudentPortalService.OperationResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class PortalPageController {

    private final StudentPortalService studentPortalService;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final StudentCourseDAO studentCourseDAO;
    private final StudyGroupDAO studyGroupDAO;
    private final LessonDAO lessonDAO;
    private final LessonGroupDAO lessonGroupDAO;
    private final ClassroomDAO classroomDAO;
    private final TeacherDAO teacherDAO;
    private final FacultyDAO facultyDAO;

    public PortalPageController(
            StudentPortalService studentPortalService,
            StudentDAO studentDAO,
            CourseDAO courseDAO,
            StudentCourseDAO studentCourseDAO,
            StudyGroupDAO studyGroupDAO,
            LessonDAO lessonDAO,
            LessonGroupDAO lessonGroupDAO,
            ClassroomDAO classroomDAO,
            TeacherDAO teacherDAO,
            FacultyDAO facultyDAO
    ) {
        this.studentPortalService = studentPortalService;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
        this.studentCourseDAO = studentCourseDAO;
        this.studyGroupDAO = studyGroupDAO;
        this.lessonDAO = lessonDAO;
        this.lessonGroupDAO = lessonGroupDAO;
        this.classroomDAO = classroomDAO;
        this.teacherDAO = teacherDAO;
        this.facultyDAO = facultyDAO;
    }

    @GetMapping("/schedule")
    public String schedule(@RequestParam(required = false) Long groupId, Model model) {
        model.addAttribute("groups", studentPortalService.getGroups());
        model.addAttribute("courses", sortedCourses());
        model.addAttribute("classrooms", classroomDAO.getAll());
        model.addAttribute("teachers", teacherDAO.getAll());
        model.addAttribute("selectedGroupId", groupId);
        model.addAttribute("lessonGroups", groupId == null ? lessonGroupDAO.getAll() : lessonGroupDAO.findByGroupId(groupId));
        return "schedule";
    }

    @PostMapping("/schedule")
    public String createLesson(
            @RequestParam Long courseId,
            @RequestParam Long classroomId,
            @RequestParam Long teacherId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate lessonDate,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime endTime,
            @RequestParam(required = false) List<Long> groupIds,
            RedirectAttributes redirectAttributes
    ) {
        Lesson lesson = Lesson.builder()
                .course(courseDAO.getById(courseId))
                .classroom(classroomDAO.getById(classroomId))
                .teacher(teacherDAO.getById(teacherId))
                .lessonDate(lessonDate)
                .startTime(startTime)
                .endTime(endTime)
                .build();
        lessonDAO.save(lesson);
        if (groupIds != null) {
            groupIds.forEach(groupId -> lessonGroupDAO.assignLessonToGroup(lesson.getId(), groupId));
        }
        redirectAttributes.addFlashAttribute("successMessage", "Занятие добавлено в расписание.");
        return "redirect:/schedule";
    }

    @GetMapping("/courses")
    public String courses(@RequestParam(defaultValue = "1") Long studentId, Model model) {
        Set<Long> enrolledCourseIds = studentCourseDAO.findByStudentId(studentId).stream()
                .map(sc -> sc.getCourse().getId())
                .collect(Collectors.toSet());
        model.addAttribute("studentId", studentId);
        model.addAttribute("courses", sortedCourses());
        model.addAttribute("teachers", teacherDAO.getAll());
        model.addAttribute("enrolledCourseIds", enrolledCourseIds);
        model.addAttribute("courseTypes", CourseType.values());
        return "courses";
    }

    @PostMapping("/courses")
    public String createCourse(
            @RequestParam String name,
            @RequestParam CourseType courseType,
            @RequestParam Integer maxStudents,
            @RequestParam String description,
            @RequestParam Long teacherId,
            RedirectAttributes redirectAttributes
    ) {
        Course course = Course.builder()
                .name(name.trim())
                .courseType(courseType)
                .maxStudents(maxStudents)
                .freePlaces(maxStudents)
                .description(description.trim())
                .teacher(teacherDAO.getById(teacherId))
                .build();
        courseDAO.save(course);
        redirectAttributes.addFlashAttribute("successMessage", "Курс добавлен.");
        return "redirect:/courses";
    }

    @PostMapping("/courses/{courseId}")
    public String updateCourse(
            @PathVariable Long courseId,
            @RequestParam String name,
            @RequestParam CourseType courseType,
            @RequestParam Integer maxStudents,
            @RequestParam Integer freePlaces,
            @RequestParam String description,
            @RequestParam Long teacherId,
            RedirectAttributes redirectAttributes
    ) {
        Course course = courseDAO.getById(courseId);
        if (course != null) {
            course.setName(name.trim());
            course.setCourseType(courseType);
            course.setMaxStudents(maxStudents);
            course.setFreePlaces(freePlaces);
            course.setDescription(description.trim());
            course.setTeacher(teacherDAO.getById(teacherId));
            courseDAO.update(course);
            redirectAttributes.addFlashAttribute("successMessage", "Курс обновлен.");
        }
        return "redirect:/courses";
    }

    @PostMapping("/courses/{courseId}/delete")
    public String deleteCourse(@PathVariable Long courseId, RedirectAttributes redirectAttributes) {
        courseDAO.delete(courseId);
        redirectAttributes.addFlashAttribute("successMessage", "Курс удален.");
        return "redirect:/courses";
    }

    @PostMapping("/courses/{courseId}/enroll")
    public String enrollFromCourses(@PathVariable Long courseId, @RequestParam(defaultValue = "1") Long studentId, RedirectAttributes redirectAttributes) {
        OperationResult result = studentPortalService.enrollToSpecialCourse(studentId, courseId);
        redirectAttributes.addFlashAttribute(result.success() ? "successMessage" : "errorMessage", result.message());
        return "redirect:/courses?studentId=" + studentId;
    }

    @PostMapping("/courses/{courseId}/unenroll")
    public String unenrollFromCourses(@PathVariable Long courseId, @RequestParam(defaultValue = "1") Long studentId, RedirectAttributes redirectAttributes) {
        OperationResult result = studentPortalService.unenrollFromSpecialCourse(studentId, courseId);
        redirectAttributes.addFlashAttribute(result.success() ? "successMessage" : "errorMessage", result.message());
        return "redirect:/courses?studentId=" + studentId;
    }

    @GetMapping("/my-courses")
    public String myCourses(@RequestParam(defaultValue = "1") Long studentId, Model model) {
        model.addAttribute("student", studentDAO.getById(studentId));
        model.addAttribute("studentCourses", studentPortalService.getStudentCourses(studentId));
        return "my-courses";
    }

    @PostMapping("/my-courses/{courseId}/unenroll")
    public String unenrollFromMyCourses(@PathVariable Long courseId, @RequestParam(defaultValue = "1") Long studentId, RedirectAttributes redirectAttributes) {
        OperationResult result = studentPortalService.unenrollFromSpecialCourse(studentId, courseId);
        redirectAttributes.addFlashAttribute(result.success() ? "successMessage" : "errorMessage", result.message());
        return "redirect:/my-courses?studentId=" + studentId;
    }

    @GetMapping("/grades")
    public String grades(
            @RequestParam(defaultValue = "1") Long studentId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long groupId,
            Model model
    ) {
        model.addAttribute("student", studentDAO.getById(studentId));
        model.addAttribute("studentCourses", studentPortalService.getStudentCourses(studentId));
        model.addAttribute("courses", sortedCourses());
        model.addAttribute("groups", studentPortalService.getGroups());
        model.addAttribute("selectedCourseId", courseId);
        model.addAttribute("selectedGroupId", groupId);
        model.addAttribute("students", groupId == null ? List.of() : studentDAO.findByGroupId(groupId));
        return "grades";
    }

    @PostMapping("/grades")
    public String updateGrades(
            @RequestParam Long courseId,
            @RequestParam Long groupId,
            @RequestParam List<Long> studentIds,
            @RequestParam List<Integer> grades,
            RedirectAttributes redirectAttributes
    ) {
        for (int i = 0; i < studentIds.size(); i++) {
            studentCourseDAO.updateGrade(studentIds.get(i), courseId, grades.get(i));
        }
        redirectAttributes.addFlashAttribute("successMessage", "Оценки сохранены.");
        return "redirect:/grades?courseId=" + courseId + "&groupId=" + groupId;
    }

    @GetMapping("/groups")
    public String groups(Model model) {
        model.addAttribute("groups", studentPortalService.getGroups());
        model.addAttribute("faculties", facultyDAO.getAll());
        return "groups";
    }

    @PostMapping("/groups")
    public String createGroup(@RequestParam String name, @RequestParam Integer studyYear, @RequestParam Long facultyId) {
        studyGroupDAO.save(StudyGroup.builder()
                .name(name.trim())
                .studyYear(studyYear)
                .faculty(facultyDAO.getById(facultyId))
                .build());
        return "redirect:/groups";
    }

    @PostMapping("/groups/{groupId}")
    public String updateGroup(@PathVariable Long groupId, @RequestParam String name, @RequestParam Integer studyYear, @RequestParam Long facultyId) {
        StudyGroup group = studyGroupDAO.getById(groupId);
        if (group != null) {
            group.setName(name.trim());
            group.setStudyYear(studyYear);
            group.setFaculty(facultyDAO.getById(facultyId));
            studyGroupDAO.update(group);
        }
        return "redirect:/groups";
    }

    @PostMapping("/groups/{groupId}/delete")
    public String deleteGroup(@PathVariable Long groupId) {
        studyGroupDAO.delete(groupId);
        return "redirect:/groups";
    }

    @GetMapping("/classrooms")
    public String classrooms(Model model) {
        model.addAttribute("classrooms", classroomDAO.getAll());
        model.addAttribute("faculties", facultyDAO.getAll());
        return "classrooms";
    }

    @PostMapping("/classrooms")
    public String createClassroom(@RequestParam String roomNumber, @RequestParam Integer capacity, @RequestParam Long facultyId) {
        classroomDAO.save(Classroom.builder()
                .roomNumber(roomNumber.trim())
                .capacity(capacity)
                .faculty(facultyDAO.getById(facultyId))
                .build());
        return "redirect:/classrooms";
    }

    @PostMapping("/classrooms/{classroomId}")
    public String updateClassroom(@PathVariable Long classroomId, @RequestParam String roomNumber, @RequestParam Integer capacity, @RequestParam Long facultyId) {
        Classroom classroom = classroomDAO.getById(classroomId);
        if (classroom != null) {
            classroom.setRoomNumber(roomNumber.trim());
            classroom.setCapacity(capacity);
            classroom.setFaculty(facultyDAO.getById(facultyId));
            classroomDAO.update(classroom);
        }
        return "redirect:/classrooms";
    }

    @PostMapping("/classrooms/{classroomId}/delete")
    public String deleteClassroom(@PathVariable Long classroomId) {
        classroomDAO.delete(classroomId);
        return "redirect:/classrooms";
    }

    private List<Course> sortedCourses() {
        return courseDAO.getAll().stream()
                .sorted(Comparator.comparing(Course::getName))
                .toList();
    }
}
