package ru.msu.cmc.webprac.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.msu.cmc.webprac.model.Course;
import ru.msu.cmc.webprac.model.StudentCourse;

import java.util.List;

import static org.testng.Assert.*;

public class StudentCourseDAOTest extends BaseDaoTest {

    @Autowired
    private StudentCourseDAO studentCourseDAO;

    @Autowired
    private CourseDAO courseDAO;

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
    public void testFindByCourseId() {
        List<StudentCourse> studentCourses = studentCourseDAO.findByCourseId(1L);
        assertNotNull(studentCourses);
        assertFalse(studentCourses.isEmpty());

        for (StudentCourse sc : studentCourses) {
            assertEquals(sc.getCourse().getId(), Long.valueOf(1));
        }
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
        course.setFreePlaces(0);
        courseDAO.update(course);

        boolean result = studentCourseDAO.enrollToSpecialCourse(4L, 3L);
        assertFalse(result);
    }

    @Test
    public void testUnenrollFromSpecialCourseSuccess() {
        studentCourseDAO.enrollToSpecialCourse(3L, 4L);

        Course courseBefore = courseDAO.getById(4L);
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
    public void testUpdateGrade() {
        studentCourseDAO.updateGrade(4L, 2L, 5);

        StudentCourse sc = studentCourseDAO.findByStudentIdAndCourseId(4L, 2L);
        assertNotNull(sc);
        assertEquals(sc.getGrade(), Integer.valueOf(5));
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
}