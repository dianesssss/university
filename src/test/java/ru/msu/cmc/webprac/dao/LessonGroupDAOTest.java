package ru.msu.cmc.webprac.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.msu.cmc.webprac.model.LessonGroup;

import java.util.List;

import static org.testng.Assert.*;

public class LessonGroupDAOTest extends BaseDaoTest {

    @Autowired
    private LessonGroupDAO lessonGroupDAO;

    @Autowired
    private LessonDAO lessonDAO;

    @Autowired
    private StudyGroupDAO studyGroupDAO;

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
}