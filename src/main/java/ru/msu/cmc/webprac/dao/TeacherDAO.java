package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.model.Teacher;

public interface TeacherDAO extends CommonDAO<Teacher, Long> {
    Teacher findByEmail(String email);
}