package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.model.Classroom;

public interface ClassroomDAO extends CommonDAO<Classroom, Long> {
    Classroom findByRoomNumber(String roomNumber);
}