package ru.msu.cmc.webprac.dao.impl;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.dao.LessonDAO;
import ru.msu.cmc.webprac.model.Lesson;

import java.time.LocalDate;
import java.util.List;

@Repository
public class LessonDAOImpl extends AbstractCommonDAO<Lesson, Long> implements LessonDAO {

    @Override
    public List<Lesson> findByDate(LocalDate date) {
        return entityManager.createQuery(
                        "SELECT l FROM Lesson l WHERE l.lessonDate = :date", Lesson.class)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public List<Lesson> findByTeacherId(Long teacherId) {
        return entityManager.createQuery(
                        "SELECT l FROM Lesson l WHERE l.teacher.id = :teacherId", Lesson.class)
                .setParameter("teacherId", teacherId)
                .getResultList();
    }
}