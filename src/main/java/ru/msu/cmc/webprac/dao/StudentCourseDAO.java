package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.model.StudentCourse;

import java.util.List;

public interface StudentCourseDAO extends CommonDAO<StudentCourse, Long> {
    List<StudentCourse> findByStudentId(Long studentId);
    List<StudentCourse> findByCourseId(Long courseId);
    StudentCourse findByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean enrollToSpecialCourse(Long studentId, Long courseId);
    boolean unenrollFromSpecialCourse(Long studentId, Long courseId);

    void updateGrade(Long studentId, Long courseId, Integer grade);
    List<StudentCourse> findGradesByStudent(Long studentId);
}