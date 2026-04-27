package ru.msu.cmc.webprac.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.msu.cmc.webprac.model.Classroom;
import ru.msu.cmc.webprac.model.Faculty;

import java.util.List;

import static org.testng.Assert.*;

public class ClassroomDAOTest extends BaseDaoTest {

    @Autowired
    private ClassroomDAO classroomDAO;

    @Autowired
    private FacultyDAO facultyDAO;

    @Test
    public void testGetAll() {
        List<Classroom> classrooms = classroomDAO.getAll();
        assertNotNull(classrooms);
        assertFalse(classrooms.isEmpty());
    }

    @Test
    public void testFindByRoomNumberFound() {
        Classroom classroom = classroomDAO.findByRoomNumber("P-8");
        assertNotNull(classroom);
        assertEquals(classroom.getRoomNumber(), "P-8");
    }

    @Test
    public void testFindByRoomNumberNotFound() {
        Classroom classroom = classroomDAO.findByRoomNumber("NO_ROOM");
        assertNull(classroom);
    }

    @Test
    public void testSaveUpdateDelete() {
        Faculty faculty = facultyDAO.getById(1L);

        Classroom classroom = Classroom.builder()
                .roomNumber("ТЕСТ-100")
                .capacity(55)
                .faculty(faculty)
                .build();

        Classroom saved = classroomDAO.save(classroom);
        assertNotNull(saved.getId());

        saved.setCapacity(60);
        Classroom updated = classroomDAO.update(saved);
        assertEquals(updated.getCapacity(), Integer.valueOf(60));

        Long id = updated.getId();
        classroomDAO.delete(id);
        assertNull(classroomDAO.getById(id));
    }
}
