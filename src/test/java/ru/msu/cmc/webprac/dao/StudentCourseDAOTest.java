package ru.msu.cmc.webprac.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.msu.cmc.webprac.model.Course;
import ru.msu.cmc.webprac.model.CourseType;
import ru.msu.cmc.webprac.model.Student;
import ru.msu.cmc.webprac.model.StudentCourse;
import ru.msu.cmc.webprac.model.Teacher;

import java.time.LocalDateTime;
import java.util.List;

import static org.testng.Assert.*;

public class StudentCourseDAOTest extends BaseDaoTest {

    @Autowired
    private StudentCourseDAO studentCourseDAO;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Autowired
    private StudentDAO studentDAO;

    @Test
    public void testFindByStudentId() {
        List<StudentCourse> studentCourses = studentCourseDAO.findByStudentId(1L);
        assertNotNull(studentCourses);
        assertFalse(studentCourses.isEmpty());

        for (StudentCourse sc : studentCourses) {
            assertEquals(sc.getStudent().getId(), Long.valueOf(1));
        }
    }

    @Test
    public void testFindByStudentIdNotFound() {
        List<StudentCourse> studentCourses = studentCourseDAO.findByStudentId(999999L);
        assertNotNull(studentCourses);
        assertTrue(studentCourses.isEmpty());
    }

    @Test
    public void testFindByCourseId() {
        List<StudentCourse> studentCourses = studentCourseDAO.findByCourseId(1L);
        assertNotNull(studentCourses);
        assertFalse(studentCourses.isEmpty());

        for (StudentCourse sc : studentCourses) {
            assertEquals(sc.getCourse().getId(), Long.valueOf(1));
        }
    }

    @Test
    public void testFindByCourseIdNotFound() {
        List<StudentCourse> studentCourses = studentCourseDAO.findByCourseId(999999L);
        assertNotNull(studentCourses);
        assertTrue(studentCourses.isEmpty());
    }

    @Test
    public void testFindByStudentIdAndCourseIdFound() {
        StudentCourse sc = studentCourseDAO.findByStudentIdAndCourseId(1L, 1L);
        assertNotNull(sc);
        assertEquals(sc.getStudent().getId(), Long.valueOf(1));
        assertEquals(sc.getCourse().getId(), Long.valueOf(1));
    }

    @Test
    public void testFindByStudentIdAndCourseIdNotFound() {
        StudentCourse sc = studentCourseDAO.findByStudentIdAndCourseId(999L, 999L);
        assertNull(sc);
    }

    @Test
    public void testEnrollToSpecialCourseSuccess() {
        Course courseBefore = courseDAO.getById(4L);
        assertNotNull(courseBefore);
        Integer oldPlaces = courseBefore.getFreePlaces();

        boolean result = studentCourseDAO.enrollToSpecialCourse(3L, 4L);

        assertTrue(result);

        StudentCourse sc = studentCourseDAO.findByStudentIdAndCourseId(3L, 4L);
        assertNotNull(sc);

        Course courseAfter = courseDAO.getById(4L);
        assertEquals(courseAfter.getFreePlaces(), Integer.valueOf(oldPlaces - 1));
    }

    @Test
    public void testEnrollToSpecialCourseAlreadyEnrolled() {
        boolean result = studentCourseDAO.enrollToSpecialCourse(1L, 3L);
        assertFalse(result);
    }

    @Test
    public void testEnrollToSpecialCourseNoPlaces() {
        Course course = courseDAO.getById(3L);
        assertNotNull(course);

        Integer oldPlaces = course.getFreePlaces();
        course.setFreePlaces(0);
        courseDAO.update(course);

        boolean result = studentCourseDAO.enrollToSpecialCourse(4L, 3L);
        assertFalse(result);

        Course updatedCourse = courseDAO.getById(3L);
        assertNotNull(updatedCourse);
        assertEquals(updatedCourse.getFreePlaces(), Integer.valueOf(0));

        course.setFreePlaces(oldPlaces);
        courseDAO.update(course);
    }

    @Test
    public void testEnrollToSpecialCourseNegativeFreePlaces() {
        Course course = courseDAO.getById(4L);
        assertNotNull(course);

        Integer oldPlaces = course.getFreePlaces();
        course.setFreePlaces(-1);
        courseDAO.update(course);

        boolean result = studentCourseDAO.enrollToSpecialCourse(3L, 4L);
        assertFalse(result);

        Course updatedCourse = courseDAO.getById(4L);
        assertNotNull(updatedCourse);
        assertEquals(updatedCourse.getFreePlaces(), Integer.valueOf(-1));

        course.setFreePlaces(oldPlaces);
        courseDAO.update(course);
    }

    @Test
    public void testEnrollToSpecialCourseFreePlacesNull() {
        Teacher teacher = teacherDAO.getById(1L);
        assertNotNull(teacher);

        Course course = Course.builder()
                .name("Special course with null free places")
                .courseType(CourseType.SPECIAL)
                .maxStudents(10)
                .freePlaces(null)
                .description("РўРµСЃС‚")
                .teacher(teacher)
                .build();

        Course savedCourse = courseDAO.save(course);
        assertNotNull(savedCourse);
        assertNotNull(savedCourse.getId());

        boolean result = studentCourseDAO.enrollToSpecialCourse(3L, savedCourse.getId());

        assertFalse(result);

        StudentCourse sc = studentCourseDAO.findByStudentIdAndCourseId(3L, savedCourse.getId());
        assertNull(sc);
    }

    @Test
    public void testEnrollToSpecialCourseStudentNotFound() {
        boolean result = studentCourseDAO.enrollToSpecialCourse(999999L, 4L);
        assertFalse(result);
    }

    @Test
    public void testEnrollToSpecialCourseCourseNotFound() {
        boolean result = studentCourseDAO.enrollToSpecialCourse(1L, 999999L);
        assertFalse(result);
    }

    @Test
    public void testEnrollToSpecialCourseMandatoryCourse() {
        boolean result = studentCourseDAO.enrollToSpecialCourse(1L, 1L);
        assertFalse(result);
    }

    @Test
    public void testUnenrollFromSpecialCourseSuccess() {
        studentCourseDAO.enrollToSpecialCourse(3L, 4L);

        Course courseBefore = courseDAO.getById(4L);
        assertNotNull(courseBefore);
        Integer placesBefore = courseBefore.getFreePlaces();

        boolean result = studentCourseDAO.unenrollFromSpecialCourse(3L, 4L);

        assertTrue(result);

        StudentCourse sc = studentCourseDAO.findByStudentIdAndCourseId(3L, 4L);
        assertNull(sc);

        Course courseAfter = courseDAO.getById(4L);
        assertEquals(courseAfter.getFreePlaces(), Integer.valueOf(placesBefore + 1));
    }

    @Test
    public void testUnenrollFromSpecialCourseNotFound() {
        boolean result = studentCourseDAO.unenrollFromSpecialCourse(999L, 999L);
        assertFalse(result);
    }

    @Test
    public void testUnenrollFromSpecialCourseCourseWithNullFreePlaces() {
        Teacher teacher = teacherDAO.getById(1L);
        assertNotNull(teacher);

        Course course = Course.builder()
                .name("Null free places course")
                .courseType(CourseType.SPECIAL)
                .maxStudents(10)
                .freePlaces(null)
                .description("РўРµСЃС‚")
                .teacher(teacher)
                .build();

        Course savedCourse = courseDAO.save(course);
        assertNotNull(savedCourse);
        assertNotNull(savedCourse.getId());

        Student student = studentDAO.getById(3L);
        assertNotNull(student);

        StudentCourse studentCourse = StudentCourse.builder()
                .student(student)
                .course(savedCourse)
                .grade(null)
                .enrolledAt(LocalDateTime.now())
                .build();

        studentCourse = studentCourseDAO.save(studentCourse);
        assertNotNull(studentCourse);
        assertNotNull(studentCourse.getId());

        StudentCourse existing = studentCourseDAO.findByStudentIdAndCourseId(3L, savedCourse.getId());
        assertNotNull(existing);

        boolean result = studentCourseDAO.unenrollFromSpecialCourse(3L, savedCourse.getId());
        assertTrue(result);

        StudentCourse afterDelete = studentCourseDAO.findByStudentIdAndCourseId(3L, savedCourse.getId());
        assertNull(afterDelete);

        Course courseAfter = courseDAO.getById(savedCourse.getId());
        assertNotNull(courseAfter);
        assertNull(courseAfter.getFreePlaces());
    }

    @Test
    public void testUnenrollFromSpecialCourseCourseWithNonNullFreePlaces() {
        Teacher teacher = teacherDAO.getById(1L);
        assertNotNull(teacher);

        Course course = Course.builder()
                .name("Non-null free places course")
                .courseType(CourseType.SPECIAL)
                .maxStudents(10)
                .freePlaces(2)
                .description("РўРµСЃС‚")
                .teacher(teacher)
                .build();

        Course savedCourse = courseDAO.save(course);
        assertNotNull(savedCourse);

        Student student = studentDAO.getById(3L);
        assertNotNull(student);

        StudentCourse studentCourse = StudentCourse.builder()
                .student(student)
                .course(savedCourse)
                .grade(null)
                .enrolledAt(LocalDateTime.now())
                .build();

        studentCourse = studentCourseDAO.save(studentCourse);
        assertNotNull(studentCourse);

        boolean result = studentCourseDAO.unenrollFromSpecialCourse(3L, savedCourse.getId());
        assertTrue(result);

        StudentCourse afterDelete = studentCourseDAO.findByStudentIdAndCourseId(3L, savedCourse.getId());
        assertNull(afterDelete);

        Course courseAfter = courseDAO.getById(savedCourse.getId());
        assertNotNull(courseAfter);
        assertEquals(courseAfter.getFreePlaces(), Integer.valueOf(3));
    }

    @Test
    public void testUpdateGrade() {
        studentCourseDAO.updateGrade(4L, 2L, 5);

        StudentCourse sc = studentCourseDAO.findByStudentIdAndCourseId(4L, 2L);
        assertNotNull(sc);
        assertEquals(sc.getGrade(), Integer.valueOf(5));
    }

    @Test
    public void testUpdateGradeRecordNotFound() {
        studentCourseDAO.updateGrade(999999L, 999999L, 5);
        StudentCourse sc = studentCourseDAO.findByStudentIdAndCourseId(999999L, 999999L);
        assertNull(sc);
    }

    @Test
    public void testFindGradesByStudent() {
        List<StudentCourse> grades = studentCourseDAO.findGradesByStudent(1L);
        assertNotNull(grades);
        assertFalse(grades.isEmpty());

        for (StudentCourse sc : grades) {
            assertEquals(sc.getStudent().getId(), Long.valueOf(1));
        }
    }

    @Test
    public void testFindGradesByStudentNotFound() {
        List<StudentCourse> grades = studentCourseDAO.findGradesByStudent(999999L);
        assertNotNull(grades);
        assertTrue(grades.isEmpty());
    }
}
