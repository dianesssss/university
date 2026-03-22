package ru.msu.cmc.webprac.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.msu.cmc.webprac.model.Classroom;
import ru.msu.cmc.webprac.model.Course;
import ru.msu.cmc.webprac.model.Lesson;
import ru.msu.cmc.webprac.model.Teacher;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.testng.Assert.*;

public class LessonDAOTest extends BaseDaoTest {

    @Autowired
    private LessonDAO lessonDAO;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private ClassroomDAO classroomDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Test
    public void testGetAll() {
        List<Lesson> lessons = lessonDAO.getAll();
        assertNotNull(lessons);
        assertFalse(lessons.isEmpty());
    }

    @Test
    public void testFindByDate() {
        List<Lesson> lessons = lessonDAO.findByDate(LocalDate.of(2026, 3, 23));
        assertNotNull(lessons);
        assertFalse(lessons.isEmpty());

        for (Lesson lesson : lessons) {
            assertEquals(lesson.getLessonDate(), LocalDate.of(2026, 3, 23));
        }
    }

    @Test
    public void testFindByTeacherId() {
        List<Lesson> lessons = lessonDAO.findByTeacherId(1L);
        assertNotNull(lessons);

        for (Lesson lesson : lessons) {
            assertEquals(lesson.getTeacher().getId(), Long.valueOf(1));
        }
    }

    @Test
    public void testSaveUpdateDelete() {
        Course course = courseDAO.getById(1L);
        Classroom classroom = classroomDAO.getById(1L);
        Teacher teacher = teacherDAO.getById(1L);

        Lesson lesson = Lesson.builder()
                .course(course)
                .classroom(classroom)
                .teacher(teacher)
                .lessonDate(LocalDate.of(2026, 4, 1))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 30))
                .build();

        Lesson saved = lessonDAO.save(lesson);
        assertNotNull(saved.getId());

        saved.setEndTime(LocalTime.of(11, 0));
        Lesson updated = lessonDAO.update(saved);
        assertEquals(updated.getEndTime(), LocalTime.of(11, 0));

        Long id = updated.getId();
        lessonDAO.delete(id);
        assertNull(lessonDAO.getById(id));
    }
}