package ru.msu.cmc.webprac.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.msu.cmc.webprac.model.Student;
import ru.msu.cmc.webprac.model.StudyGroup;

import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.*;

public class StudentDAOTest extends BaseDaoTest {

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private StudyGroupDAO studyGroupDAO;

    @Test
    public void testGetAll() {
        List<Student> students = studentDAO.getAll();
        assertNotNull(students);
        assertFalse(students.isEmpty());
    }

    @Test
    public void testGetByIdFound() {
        Student student = studentDAO.getById(1L);
        assertNotNull(student);
        assertEquals(student.getId(), Long.valueOf(1));
    }

    @Test
    public void testGetByIdNotFound() {
        Student student = studentDAO.getById(999999L);
        assertNull(student);
    }

    @Test
    public void testSave() {
        StudyGroup group = studyGroupDAO.getById(1L);
        assertNotNull(group);

        Student student = Student.builder()
                .surname("Тестов")
                .firstName("Тест")
                .patronymic("Тестович")
                .birthDate(LocalDate.of(2005, 1, 1))
                .email("save_student_test@mail.ru")
                .group(group)
                .build();

        Student saved = studentDAO.save(student);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(saved.getSurname(), "Тестов");
        assertEquals(saved.getGroup().getId(), Long.valueOf(1));
    }

    @Test
    public void testUpdate() {
        Student student = studentDAO.getById(1L);
        assertNotNull(student);

        String oldEmail = student.getEmail();
        student.setEmail("updated_student@mail.ru");

        Student updated = studentDAO.update(student);

        assertNotNull(updated);
        assertEquals(updated.getEmail(), "updated_student@mail.ru");
        assertNotEquals(updated.getEmail(), oldEmail);
    }

    @Test
    public void testDelete() {
        StudyGroup group = studyGroupDAO.getById(1L);

        Student student = Student.builder()
                .surname("Удаляемый")
                .firstName("Студент")
                .patronymic("Тестовый")
                .birthDate(LocalDate.of(2004, 5, 10))
                .email("delete_student_test@mail.ru")
                .group(group)
                .build();

        Student saved = studentDAO.save(student);
        Long id = saved.getId();

        assertNotNull(studentDAO.getById(id));

        studentDAO.delete(id);

        assertNull(studentDAO.getById(id));
    }

    @Test
    public void testFindByGroupIdFound() {
        List<Student> students = studentDAO.findByGroupId(1L);
        assertNotNull(students);
        assertFalse(students.isEmpty());

        for (Student student : students) {
            assertEquals(student.getGroup().getId(), Long.valueOf(1));
        }
    }

    @Test
    public void testFindByGroupIdEmpty() {
        List<Student> students = studentDAO.findByGroupId(999999L);
        assertNotNull(students);
        assertTrue(students.isEmpty());
    }

    @Test
    public void testFindByFullNameLikeFound() {
        List<Student> students = studentDAO.findByFullNameLike("смир");
        assertNotNull(students);
        assertFalse(students.isEmpty());
    }

    @Test
    public void testFindByFullNameLikeNotFound() {
        List<Student> students = studentDAO.findByFullNameLike("zzzzzzzz");
        assertNotNull(students);
        assertTrue(students.isEmpty());
    }
}