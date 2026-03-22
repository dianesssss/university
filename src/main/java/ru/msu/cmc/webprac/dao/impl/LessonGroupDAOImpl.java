package ru.msu.cmc.webprac.dao.impl;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.dao.LessonGroupDAO;
import ru.msu.cmc.webprac.model.Lesson;
import ru.msu.cmc.webprac.model.LessonGroup;
import ru.msu.cmc.webprac.model.StudyGroup;

import java.util.List;

@Repository
public class LessonGroupDAOImpl extends AbstractCommonDAO<LessonGroup, Long> implements LessonGroupDAO {

    @Override
    public List<LessonGroup> findByGroupId(Long groupId) {
        return entityManager.createQuery(
                        "SELECT lg FROM LessonGroup lg WHERE lg.group.id = :groupId", LessonGroup.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }

    @Override
    public void assignLessonToGroup(Long lessonId, Long groupId) {
        Lesson lesson = entityManager.find(Lesson.class, lessonId);
        StudyGroup group = entityManager.find(StudyGroup.class, groupId);

        if (lesson != null && group != null) {
            LessonGroup lessonGroup = LessonGroup.builder()
                    .lesson(lesson)
                    .group(group)
                    .build();
            entityManager.persist(lessonGroup);
        }
    }
}