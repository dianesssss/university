package ru.msu.cmc.webprac.dao.impl;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.dao.FacultyDAO;
import ru.msu.cmc.webprac.model.Faculty;

@Repository
public class FacultyDAOImpl extends AbstractCommonDAO<Faculty, Long> implements FacultyDAO {

    @Override
    public Faculty findByName(String name) {
        try {
            return entityManager.createQuery(
                            "SELECT f FROM Faculty f WHERE f.name = :name", Faculty.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}