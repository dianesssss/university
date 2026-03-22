package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.model.Faculty;

public interface FacultyDAO extends CommonDAO<Faculty, Long> {
    Faculty findByName(String name);
}