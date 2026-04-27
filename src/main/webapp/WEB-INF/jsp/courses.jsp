<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Курсы</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<main class="page">
    <section class="hero">
        <div><h1>Курсы</h1><p>Спецкурсы для студентов и управление курсами для преподавателя.</p></div>
        <nav class="top-nav"><a href="${pageContext.request.contextPath}/schedule">Расписание</a><a href="${pageContext.request.contextPath}/my-courses?studentId=${studentId}">Мои курсы</a><a href="${pageContext.request.contextPath}/students">Студенты</a></nav>
    </section>
    <c:if test="${not empty successMessage}"><div class="message success">${successMessage}</div></c:if>
    <c:if test="${not empty errorMessage}"><div class="message error">${errorMessage}</div></c:if>
    <section class="grid">
        <div class="panel">
            <h2>Список курсов</h2>
            <table id="courses-table">
                <thead><tr><th>Курс</th><th>Тип</th><th>Места</th><th>Преподаватель</th><th>Студент</th><th>Редактирование</th></tr></thead>
                <tbody>
                <c:forEach items="${courses}" var="course">
                    <tr>
                        <td><strong>${course.name}</strong><br><span class="muted">${course.description}</span></td>
                        <td>${course.courseType}</td>
                        <td>${course.freePlaces} / ${course.maxStudents}</td>
                        <td>${course.teacher.surname} ${course.teacher.firstName}</td>
                        <td>
                            <c:choose>
                                <c:when test="${course.courseType eq 'SPECIAL' && enrolledCourseIds.contains(course.id)}">
                                    <form method="post" action="${pageContext.request.contextPath}/courses/${course.id}/unenroll">
                                        <input type="hidden" name="studentId" value="${studentId}">
                                        <button type="submit" class="button-danger">Отписаться</button>
                                    </form>
                                </c:when>
                                <c:when test="${course.courseType eq 'SPECIAL'}">
                                    <form method="post" action="${pageContext.request.contextPath}/courses/${course.id}/enroll">
                                        <input type="hidden" name="studentId" value="${studentId}">
                                        <button type="submit">Записаться</button>
                                    </form>
                                </c:when>
                            </c:choose>
                        </td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/courses/${course.id}" class="stack compact-form">
                                <input name="name" value="${course.name}">
                                <select name="courseType"><c:forEach items="${courseTypes}" var="type"><option value="${type}" <c:if test="${course.courseType == type}">selected</c:if>>${type}</option></c:forEach></select>
                                <input name="maxStudents" type="number" value="${course.maxStudents}">
                                <input name="freePlaces" type="number" value="${course.freePlaces}">
                                <input name="description" value="${course.description}">
                                <select name="teacherId"><c:forEach items="${teachers}" var="teacher"><option value="${teacher.id}" <c:if test="${course.teacher.id == teacher.id}">selected</c:if>>${teacher.surname}</option></c:forEach></select>
                                <button type="submit">Редактировать</button>
                            </form>
                            <form method="post" action="${pageContext.request.contextPath}/courses/${course.id}/delete"><button type="submit" class="button-danger">Удалить</button></form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="panel">
            <h2>Добавить курс</h2>
            <form id="course-form" method="post" action="${pageContext.request.contextPath}/courses" class="stack">
                <div><label>Название курса</label><input name="name" required></div>
                <div><label>Тип курса</label><select name="courseType"><c:forEach items="${courseTypes}" var="type"><option value="${type}">${type}</option></c:forEach></select></div>
                <div><label>Максимум студентов</label><input name="maxStudents" type="number" min="1" required></div>
                <div><label>Описание</label><textarea name="description"></textarea></div>
                <div><label>Преподаватель</label><select name="teacherId"><c:forEach items="${teachers}" var="teacher"><option value="${teacher.id}">${teacher.surname} ${teacher.firstName}</option></c:forEach></select></div>
                <button type="submit">Сохранить</button>
            </form>
        </div>
    </section>
</main>
</body>
</html>
