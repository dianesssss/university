package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.model.Lesson;

import java.time.LocalDate;
import java.util.List;

public interface LessonDAO extends CommonDAO<Lesson, Long> {
    List<Lesson> findByDate(LocalDate date);
    List<Lesson> findByTeacherId(Long teacherId);
}