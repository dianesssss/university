package ru.msu.cmc.webprac.dao.impl;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.dao.TeacherDAO;
import ru.msu.cmc.webprac.model.Teacher;

@Repository
public class TeacherDAOImpl extends AbstractCommonDAO<Teacher, Long> implements TeacherDAO {

    @Override
    public Teacher findByEmail(String email) {
        try {
            return entityManager.createQuery(
                            "SELECT t FROM Teacher t WHERE t.email = :email", Teacher.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}