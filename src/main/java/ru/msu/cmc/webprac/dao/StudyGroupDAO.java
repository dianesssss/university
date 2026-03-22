package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.model.StudyGroup;

public interface StudyGroupDAO extends CommonDAO<StudyGroup, Long> {
    StudyGroup findByName(String name);
}