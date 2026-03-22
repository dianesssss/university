package ru.msu.cmc.webprac.dao.impl;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.dao.StudentCourseDAO;
import ru.msu.cmc.webprac.model.Course;
import ru.msu.cmc.webprac.model.CourseType;
import ru.msu.cmc.webprac.model.Student;
import ru.msu.cmc.webprac.model.StudentCourse;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class StudentCourseDAOImpl extends AbstractCommonDAO<StudentCourse, Long> implements StudentCourseDAO {

    @Override
    public List<StudentCourse> findByStudentId(Long studentId) {
        return entityManager.createQuery(
                        "SELECT sc FROM StudentCourse sc WHERE sc.student.id = :studentId", StudentCourse.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }

    @Override
    public List<StudentCourse> findByCourseId(Long courseId) {
        return entityManager.createQuery(
                        "SELECT sc FROM StudentCourse sc WHERE sc.course.id = :courseId", StudentCourse.class)
                .setParameter("courseId", courseId)
                .getResultList();
    }

    @Override
    public StudentCourse findByStudentIdAndCourseId(Long studentId, Long courseId) {
        try {
            return entityManager.createQuery(
                            "SELECT sc FROM StudentCourse sc WHERE sc.student.id = :studentId AND sc.course.id = :courseId",
                            StudentCourse.class)
                    .setParameter("studentId", studentId)
                    .setParameter("courseId", courseId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean enrollToSpecialCourse(Long studentId, Long courseId) {
        StudentCourse existing = findByStudentIdAndCourseId(studentId, courseId);
        if (existing != null) {
            return false;
        }

        Student student = entityManager.find(Student.class, studentId);
        Course course = entityManager.find(Course.class, courseId);

        if (student == null || course == null) {
            return false;
        }

        if (course.getCourseType() != CourseType.SPECIAL) {
            return false;
        }

        if (course.getFreePlaces() == null || course.getFreePlaces() <= 0) {
            return false;
        }

        StudentCourse studentCourse = StudentCourse.builder()
                .student(student)
                .course(course)
                .grade(null)
                .enrolledAt(LocalDateTime.now())
                .build();

        entityManager.persist(studentCourse);

        course.setFreePlaces(course.getFreePlaces() - 1);
        entityManager.merge(course);

        return true;
    }

    @Override
    public boolean unenrollFromSpecialCourse(Long studentId, Long courseId) {
        StudentCourse studentCourse = findByStudentIdAndCourseId(studentId, courseId);
        if (studentCourse == null) {
            return false;
        }

        Course course = studentCourse.getCourse();
        entityManager.remove(studentCourse);

        if (course != null && course.getFreePlaces() != null) {
            course.setFreePlaces(course.getFreePlaces() + 1);
            entityManager.merge(course);
        }

        return true;
    }

    @Override
    public void updateGrade(Long studentId, Long courseId, Integer grade) {
        StudentCourse studentCourse = findByStudentIdAndCourseId(studentId, courseId);
        if (studentCourse != null) {
            studentCourse.setGrade(grade);
            entityManager.merge(studentCourse);
        }
    }

    @Override
    public List<StudentCourse> findGradesByStudent(Long studentId) {
        return entityManager.createQuery(
                        "SELECT sc FROM StudentCourse sc WHERE sc.student.id = :studentId", StudentCourse.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }
}