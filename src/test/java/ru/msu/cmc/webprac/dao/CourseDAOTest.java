package ru.msu.cmc.webprac.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.msu.cmc.webprac.model.Course;
import ru.msu.cmc.webprac.model.CourseType;
import ru.msu.cmc.webprac.model.Teacher;

import java.util.List;

import static org.testng.Assert.*;

public class CourseDAOTest extends BaseDaoTest {

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Test
    public void testGetAll() {
        List<Course> courses = courseDAO.getAll();
        assertNotNull(courses);
        assertFalse(courses.isEmpty());
    }

    @Test
    public void testFindMandatoryCourses() {
        List<Course> courses = courseDAO.findMandatoryCourses();
        assertNotNull(courses);
        assertFalse(courses.isEmpty());

        for (Course course : courses) {
            assertEquals(course.getCourseType(), CourseType.MANDATORY);
        }
    }

    @Test
    public void testFindSpecialCourses() {
        List<Course> courses = courseDAO.findSpecialCourses();
        assertNotNull(courses);
        assertFalse(courses.isEmpty());

        for (Course course : courses) {
            assertEquals(course.getCourseType(), CourseType.SPECIAL);
        }
    }

    @Test
    public void testFindByTeacherId() {
        List<Course> courses = courseDAO.findByTeacherId(2L);
        assertNotNull(courses);
        assertFalse(courses.isEmpty());

        for (Course course : courses) {
            assertEquals(course.getTeacher().getId(), Long.valueOf(2));
        }
    }

    @Test
    public void testHasFreePlacesTrue() {
        assertTrue(courseDAO.hasFreePlaces(3L));
    }

    @Test
    public void testHasFreePlacesFalse() {
        Teacher teacher = teacherDAO.getById(1L);

        Course course = Course.builder()
                .name("Курс без мест test")
                .courseType(CourseType.SPECIAL)
                .maxStudents(10)
                .freePlaces(0)
                .description("Тест")
                .teacher(teacher)
                .build();

        Course saved = courseDAO.save(course);
        assertFalse(courseDAO.hasFreePlaces(saved.getId()));
    }

    @Test
    public void testHasFreePlacesCourseNotFound() {
        assertFalse(courseDAO.hasFreePlaces(999999L));
    }

    @Test
    public void testHasFreePlacesWhenNull() {
        Teacher teacher = teacherDAO.getById(1L);

        Course course = Course.builder()
                .name("Null free places test")
                .courseType(CourseType.SPECIAL)
                .maxStudents(10)
                .freePlaces(null)
                .description("РўРµСЃС‚")
                .teacher(teacher)
                .build();

        Course saved = courseDAO.save(course);
        assertFalse(courseDAO.hasFreePlaces(saved.getId()));
    }

    @Test
    public void testDecreaseAndIncreaseFreePlaces() {
        Course course = courseDAO.getById(3L);
        assertNotNull(course);

        Integer oldValue = course.getFreePlaces();

        courseDAO.decreaseFreePlaces(course.getId());
        Course afterDecrease = courseDAO.getById(course.getId());
        assertEquals(afterDecrease.getFreePlaces(), Integer.valueOf(oldValue - 1));

        courseDAO.increaseFreePlaces(course.getId());
        Course afterIncrease = courseDAO.getById(course.getId());
        assertEquals(afterIncrease.getFreePlaces(), oldValue);
    }

    @Test
    public void testDecreaseFreePlacesWhenZero() {
        Teacher teacher = teacherDAO.getById(1L);

        Course course = Course.builder()
                .name("Decrease zero test")
                .courseType(CourseType.SPECIAL)
                .maxStudents(5)
                .freePlaces(0)
                .description("Тест")
                .teacher(teacher)
                .build();

        Course saved = courseDAO.save(course);

        courseDAO.decreaseFreePlaces(saved.getId());

        Course after = courseDAO.getById(saved.getId());
        assertEquals(after.getFreePlaces(), Integer.valueOf(0));
    }

    @Test
    public void testDecreaseFreePlacesCourseNotFound() {
        courseDAO.decreaseFreePlaces(999999L);
        assertNull(courseDAO.getById(999999L));
    }

    @Test
    public void testDecreaseFreePlacesWhenNull() {
        Teacher teacher = teacherDAO.getById(1L);

        Course course = Course.builder()
                .name("Decrease null test")
                .courseType(CourseType.SPECIAL)
                .maxStudents(5)
                .freePlaces(null)
                .description("РўРµСЃС‚")
                .teacher(teacher)
                .build();

        Course saved = courseDAO.save(course);

        courseDAO.decreaseFreePlaces(saved.getId());

        Course after = courseDAO.getById(saved.getId());
        assertNull(after.getFreePlaces());
    }

    @Test
    public void testIncreaseFreePlacesCourseNotFound() {
        courseDAO.increaseFreePlaces(999999L);
        assertNull(courseDAO.getById(999999L));
    }

    @Test
    public void testIncreaseFreePlacesWhenNull() {
        Teacher teacher = teacherDAO.getById(1L);

        Course course = Course.builder()
                .name("Increase null test")
                .courseType(CourseType.SPECIAL)
                .maxStudents(5)
                .freePlaces(null)
                .description("РўРµСЃС‚")
                .teacher(teacher)
                .build();

        Course saved = courseDAO.save(course);

        courseDAO.increaseFreePlaces(saved.getId());

        Course after = courseDAO.getById(saved.getId());
        assertNull(after.getFreePlaces());
    }
}
