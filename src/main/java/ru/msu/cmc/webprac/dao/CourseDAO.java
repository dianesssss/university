package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.model.Course;

import java.util.List;

public interface CourseDAO extends CommonDAO<Course, Long> {
    List<Course> findMandatoryCourses();
    List<Course> findSpecialCourses();
    List<Course> findByTeacherId(Long teacherId);
    boolean hasFreePlaces(Long courseId);
    void decreaseFreePlaces(Long courseId);
    void increaseFreePlaces(Long courseId);
}