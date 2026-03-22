package ru.msu.cmc.webprac.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.msu.cmc.webprac.model.Faculty;

import java.util.List;

import static org.testng.Assert.*;

public class FacultyDAOTest extends BaseDaoTest {

    @Autowired
    private FacultyDAO facultyDAO;

    @Test
    public void testGetAll() {
        List<Faculty> faculties = facultyDAO.getAll();
        assertNotNull(faculties);
        assertFalse(faculties.isEmpty());
    }

    @Test
    public void testFindByNameFound() {
        Faculty faculty = facultyDAO.findByName("ВМК");
        assertNotNull(faculty);
        assertEquals(faculty.getName(), "ВМК");
    }

    @Test
    public void testFindByNameNotFound() {
        Faculty faculty = facultyDAO.findByName("NO_FACULTY");
        assertNull(faculty);
    }

    @Test
    public void testSaveUpdateDelete() {
        Faculty faculty = Faculty.builder()
                .name("Тестовый факультет")
                .description("Описание")
                .address("Адрес")
                .build();

        Faculty saved = facultyDAO.save(faculty);
        assertNotNull(saved.getId());

        saved.setAddress("Новый адрес");
        Faculty updated = facultyDAO.update(saved);
        assertEquals(updated.getAddress(), "Новый адрес");

        Long id = updated.getId();
        facultyDAO.delete(id);
        assertNull(facultyDAO.getById(id));
    }
}