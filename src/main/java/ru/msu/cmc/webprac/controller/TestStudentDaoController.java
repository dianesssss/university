package ru.msu.cmc.webprac.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.msu.cmc.webprac.dao.StudentDAO;
import ru.msu.cmc.webprac.model.Student;

import java.util.List;

@RestController
@RequestMapping("/test/students")
public class TestStudentDaoController {

    private final StudentDAO studentDAO;

    public TestStudentDaoController(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentDAO.getAll();
    }
}