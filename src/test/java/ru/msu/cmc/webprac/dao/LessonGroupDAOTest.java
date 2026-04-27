package ru.msu.cmc.webprac.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.msu.cmc.webprac.model.LessonGroup;

import java.util.List;

import static org.testng.Assert.*;

public class LessonGroupDAOTest extends BaseDaoTest {

    @Autowired
    private LessonGroupDAO lessonGroupDAO;

    @Test
    public void testFindByGroupId() {
        List<LessonGroup> lessonGroups = lessonGroupDAO.findByGroupId(1L);
        assertNotNull(lessonGroups);
        assertFalse(lessonGroups.isEmpty());

        for (LessonGroup lg : lessonGroups) {
            assertEquals(lg.getGroup().getId(), Long.valueOf(1));
        }
    }

    @Test
    public void testAssignLessonToGroup() {
        lessonGroupDAO.assignLessonToGroup(3L, 3L);

        List<LessonGroup> lessonGroups = lessonGroupDAO.findByGroupId(3L);
        assertNotNull(lessonGroups);

        boolean found = lessonGroups.stream()
                .anyMatch(lg -> lg.getLesson().getId().equals(3L));

        assertTrue(found);
    }

    @Test
    public void testAssignLessonToGroupLessonNotFound() {
        lessonGroupDAO.assignLessonToGroup(999999L, 1L);

        List<LessonGroup> lessonGroups = lessonGroupDAO.findByGroupId(1L);
        boolean found = lessonGroups.stream()
                .anyMatch(lg -> lg.getLesson().getId().equals(999999L));

        assertFalse(found);
    }

    @Test
    public void testAssignLessonToGroupGroupNotFound() {
        lessonGroupDAO.assignLessonToGroup(1L, 999999L);

        List<LessonGroup> lessonGroups = lessonGroupDAO.getAll();
        boolean found = lessonGroups.stream()
                .anyMatch(lg -> lg.getGroup().getId().equals(999999L));

        assertFalse(found);
    }
}