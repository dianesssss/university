package ru.msu.cmc.webprac.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.msu.cmc.webprac.model.Teacher;

import java.util.List;

import static org.testng.Assert.*;

public class TeacherDAOTest extends BaseDaoTest {

    @Autowired
    private TeacherDAO teacherDAO;

    @Test
    public void testGetAll() {
        List<Teacher> teachers = teacherDAO.getAll();
        assertNotNull(teachers);
        assertFalse(teachers.isEmpty());
    }

    @Test
    public void testFindByEmailFound() {
        Teacher teacher = teacherDAO.findByEmail("ivanov@msu.ru");
        assertNotNull(teacher);
        assertEquals(teacher.getEmail(), "ivanov@msu.ru");
    }

    @Test
    public void testFindByEmailNotFound() {
        Teacher teacher = teacherDAO.findByEmail("no_teacher@mail.ru");
        assertNull(teacher);
    }

    @Test
    public void testSaveUpdateDelete() {
        Teacher teacher = Teacher.builder()
                .surname("Тестов")
                .firstName("Препод")
                .patronymic("Тестович")
                .email("teacher_test@mail.ru")
                .phone("+70000000000")
                .build();

        Teacher saved = teacherDAO.save(teacher);
        assertNotNull(saved.getId());

        saved.setPhone("+79999999999");
        Teacher updated = teacherDAO.update(saved);
        assertEquals(updated.getPhone(), "+79999999999");

        Long id = updated.getId();
        teacherDAO.delete(id);
        assertNull(teacherDAO.getById(id));
    }
}