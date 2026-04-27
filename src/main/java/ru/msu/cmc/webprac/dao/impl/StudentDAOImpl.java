package ru.msu.cmc.webprac.dao.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.dao.StudentDAO;
import ru.msu.cmc.webprac.model.Course;
import ru.msu.cmc.webprac.model.CourseType;
import ru.msu.cmc.webprac.model.Student;
import ru.msu.cmc.webprac.model.StudentCourse;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class StudentDAOImpl extends AbstractCommonDAO<Student, Long> implements StudentDAO {

    @Override
    public List<Student> findByGroupId(Long groupId) {
        return entityManager.createQuery(
                        "SELECT s FROM Student s WHERE s.group.id = :groupId", Student.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }

    @Override
    public List<Student> findByFullNameLike(String text) {
        String pattern = "%" + text.toLowerCase() + "%";
        return entityManager.createQuery("""
                        SELECT s FROM Student s
                        WHERE LOWER(s.surname) LIKE :pattern
                           OR LOWER(s.firstName) LIKE :pattern
                           OR LOWER(COALESCE(s.patronymic, '')) LIKE :pattern
                        """, Student.class)
                .setParameter("pattern", pattern)
                .getResultList();
    }

    @Override
    public Student findByEmail(String email) {
        List<Student> students = entityManager.createQuery(
                        "SELECT s FROM Student s WHERE LOWER(s.email) = :email", Student.class)
                .setParameter("email", email.toLowerCase())
                .setMaxResults(1)
                .getResultList();
        return students.isEmpty() ? null : students.get(0);
    }

    @Override
    public Student addStudentWithMandatoryCourses(Student student) {
        entityManager.persist(student);
        entityManager.flush();

        List<Course> mandatoryCourses = entityManager.createQuery("""
                        SELECT DISTINCT c
                        FROM Course c
                        JOIN Lesson l ON l.course.id = c.id
                        JOIN LessonGroup lg ON lg.lesson.id = l.id
                        WHERE lg.group.id = :groupId
                          AND c.courseType = :courseType
                        """, Course.class)
                .setParameter("groupId", student.getGroup().getId())
                .setParameter("courseType", CourseType.MANDATORY)
                .getResultList();

        for (Course course : mandatoryCourses) {
            StudentCourse studentCourse = StudentCourse.builder()
                    .student(student)
                    .course(course)
                    .grade(null)
                    .enrolledAt(LocalDateTime.now())
                    .build();
            entityManager.persist(studentCourse);
        }

        return student;
    }
}
