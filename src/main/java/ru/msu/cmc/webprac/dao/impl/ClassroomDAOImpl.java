package ru.msu.cmc.webprac.dao.impl;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.dao.ClassroomDAO;
import ru.msu.cmc.webprac.model.Classroom;

@Repository
public class ClassroomDAOImpl extends AbstractCommonDAO<Classroom, Long> implements ClassroomDAO {

    @Override
    public Classroom findByRoomNumber(String roomNumber) {
        try {
            return entityManager.createQuery(
                            "SELECT c FROM Classroom c WHERE c.roomNumber = :roomNumber", Classroom.class)
                    .setParameter("roomNumber", roomNumber)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}