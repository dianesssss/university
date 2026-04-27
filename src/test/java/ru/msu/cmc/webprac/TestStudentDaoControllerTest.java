package ru.msu.cmc.webprac;

import org.testng.annotations.Test;
import ru.msu.cmc.webprac.controller.TestStudentDaoController;
import ru.msu.cmc.webprac.dao.StudentDAO;
import ru.msu.cmc.webprac.model.Student;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertSame;

public class TestStudentDaoControllerTest {

    @Test
    public void testGetAllStudentsDelegatesToDao() {
        StudentDAO studentDAO = mock(StudentDAO.class);
        List<Student> students = List.of(Student.builder().id(1L).build());
        when(studentDAO.getAll()).thenReturn(students);

        TestStudentDaoController controller = new TestStudentDaoController(studentDAO);

        assertSame(controller.getAllStudents(), students);
    }
}
