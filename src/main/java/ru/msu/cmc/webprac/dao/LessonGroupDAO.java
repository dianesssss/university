package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.model.LessonGroup;

import java.util.List;

public interface LessonGroupDAO extends CommonDAO<LessonGroup, Long> {
    List<LessonGroup> findByGroupId(Long groupId);
    void assignLessonToGroup(Long lessonId, Long groupId);
}