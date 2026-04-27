package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.model.Student;

import java.util.List;

public interface StudentDAO extends CommonDAO<Student, Long> {
    List<Student> findByGroupId(Long groupId);
    List<Student> findByFullNameLike(String text);
    Student findByEmail(String email);
    Student addStudentWithMandatoryCourses(Student student);
}
