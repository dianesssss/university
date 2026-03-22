package ru.msu.cmc.webprac.dao.impl;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.dao.StudyGroupDAO;
import ru.msu.cmc.webprac.model.StudyGroup;

@Repository
public class StudyGroupDAOImpl extends AbstractCommonDAO<StudyGroup, Long> implements StudyGroupDAO {

    @Override
    public StudyGroup findByName(String name) {
        try {
            return entityManager.createQuery(
                            "SELECT g FROM StudyGroup g WHERE g.name = :name", StudyGroup.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}