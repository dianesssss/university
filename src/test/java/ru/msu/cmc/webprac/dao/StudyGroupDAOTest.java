package ru.msu.cmc.webprac.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.msu.cmc.webprac.model.Faculty;
import ru.msu.cmc.webprac.model.StudyGroup;

import java.util.List;

import static org.testng.Assert.*;

public class StudyGroupDAOTest extends BaseDaoTest {

    @Autowired
    private StudyGroupDAO studyGroupDAO;

    @Autowired
    private FacultyDAO facultyDAO;

    @Test
    public void testGetAll() {
        List<StudyGroup> groups = studyGroupDAO.getAll();
        assertNotNull(groups);
        assertFalse(groups.isEmpty());
    }

    @Test
    public void testFindByNameFound() {
        StudyGroup group = studyGroupDAO.findByName("103");
        assertNotNull(group);
        assertEquals(group.getName(), "103");
    }

    @Test
    public void testFindByNameNotFound() {
        StudyGroup group = studyGroupDAO.findByName("NO_GROUP");
        assertNull(group);
    }

    @Test
    public void testSaveUpdateDelete() {
        Faculty faculty = facultyDAO.getById(1L);

        StudyGroup group = StudyGroup.builder()
                .name("999_test_group")
                .studyYear(3)
                .faculty(faculty)
                .build();

        StudyGroup saved = studyGroupDAO.save(group);
        assertNotNull(saved.getId());

        saved.setStudyYear(4);
        StudyGroup updated = studyGroupDAO.update(saved);
        assertEquals(updated.getStudyYear(), Integer.valueOf(4));

        Long id = updated.getId();
        studyGroupDAO.delete(id);
        assertNull(studyGroupDAO.getById(id));
    }
}