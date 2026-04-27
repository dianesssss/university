package ru.msu.cmc.webprac.dao;

import jakarta.persistence.EntityManager;
import org.testng.annotations.Test;
import ru.msu.cmc.webprac.dao.impl.StudentCourseDAOImpl;
import ru.msu.cmc.webprac.model.Course;
import ru.msu.cmc.webprac.model.CourseType;
import ru.msu.cmc.webprac.model.StudentCourse;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class StudentCourseDAOImplUnitTest {

    @Test
    public void testEnrollToSpecialCourseReturnsFalseForMandatoryCourse() throws Exception {
        StudentCourseDAOImpl dao = spy(new StudentCourseDAOImpl());
        EntityManager entityManager = mock(EntityManager.class);
        setEntityManager(dao, entityManager);

        doReturn(null).when(dao).findByStudentIdAndCourseId(10L, 20L);
        when(entityManager.find(ru.msu.cmc.webprac.model.Student.class, 10L))
                .thenReturn(ru.msu.cmc.webprac.model.Student.builder().id(10L).build());
        when(entityManager.find(Course.class, 20L))
                .thenReturn(Course.builder().id(20L).courseType(CourseType.MANDATORY).freePlaces(5).build());

        assertFalse(dao.enrollToSpecialCourse(10L, 20L));
        verify(entityManager, never()).persist(any());
    }

    @Test
    public void testUnenrollFromSpecialCourseReturnsTrueWhenCourseIsNull() throws Exception {
        StudentCourseDAOImpl dao = spy(new StudentCourseDAOImpl());
        EntityManager entityManager = mock(EntityManager.class);
        setEntityManager(dao, entityManager);

        StudentCourse studentCourse = StudentCourse.builder().id(1L).course(null).build();
        doReturn(studentCourse).when(dao).findByStudentIdAndCourseId(10L, 20L);

        assertTrue(dao.unenrollFromSpecialCourse(10L, 20L));
        verify(entityManager).remove(studentCourse);
        verify(entityManager, never()).merge(any());
    }

    private static void setEntityManager(StudentCourseDAOImpl dao, EntityManager entityManager) throws Exception {
        Field field = dao.getClass().getSuperclass().getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(dao, entityManager);
    }
}
