package ru.msu.cmc.webprac.dao.impl;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.dao.CourseDAO;
import ru.msu.cmc.webprac.model.Course;
import ru.msu.cmc.webprac.model.CourseType;

import java.util.List;

@Repository
public class CourseDAOImpl extends AbstractCommonDAO<Course, Long> implements CourseDAO {

    @Override
    public List<Course> findMandatoryCourses() {
        return entityManager.createQuery(
                        "SELECT c FROM Course c WHERE c.courseType = :type", Course.class)
                .setParameter("type", CourseType.MANDATORY)
                .getResultList();
    }

    @Override
    public List<Course> findSpecialCourses() {
        return entityManager.createQuery(
                        "SELECT c FROM Course c WHERE c.courseType = :type", Course.class)
                .setParameter("type", CourseType.SPECIAL)
                .getResultList();
    }

    @Override
    public List<Course> findByTeacherId(Long teacherId) {
        return entityManager.createQuery(
                        "SELECT c FROM Course c WHERE c.teacher.id = :teacherId", Course.class)
                .setParameter("teacherId", teacherId)
                .getResultList();
    }

    @Override
    public boolean hasFreePlaces(Long courseId) {
        Course course = getById(courseId);
        return course != null && course.getFreePlaces() != null && course.getFreePlaces() > 0;
    }

    @Override
    public void decreaseFreePlaces(Long courseId) {
        Course course = getById(courseId);
        if (course != null && course.getFreePlaces() != null && course.getFreePlaces() > 0) {
            course.setFreePlaces(course.getFreePlaces() - 1);
            entityManager.merge(course);
        }
    }

    @Override
    public void increaseFreePlaces(Long courseId) {
        Course course = getById(courseId);
        if (course != null && course.getFreePlaces() != null) {
            course.setFreePlaces(course.getFreePlaces() + 1);
            entityManager.merge(course);
        }
    }
}