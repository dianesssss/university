<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Карточка студента</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<main class="page">
    <section class="hero">
        <div>
            <h1>${student.surname} ${student.firstName}</h1>
            <p>Карточка студента, список назначенных курсов, запись на спецкурс, отмена записи и выставление оценок.</p>
        </div>
        <a href="${pageContext.request.contextPath}/students">К списку студентов</a>
    </section>

    <c:if test="${not empty successMessage}">
        <div id="success-message" class="message success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div id="error-message" class="message error">${errorMessage}</div>
    </c:if>

    <section class="meta">
        <div class="meta-card">
            <strong>${student.group.name}</strong>
            <span class="muted">Группа, ${student.group.studyYear} курс</span>
        </div>
        <div class="meta-card">
            <strong>${student.birthDate}</strong>
            <span class="muted">Дата рождения</span>
        </div>
        <div class="meta-card">
            <strong>${student.email}</strong>
            <span class="muted">Контактный email</span>
        </div>
    </section>

    <section class="grid">
        <div class="panel">
            <h2>Назначенные курсы</h2>
            <table id="student-courses-table">
                <thead>
                <tr>
                    <th>Курс</th>
                    <th>Тип</th>
                    <th>Преподаватель</th>
                    <th>Свободно</th>
                    <th>Оценка</th>
                    <th>Действия</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${studentCourses}" var="studentCourse">
                    <tr id="course-row-${studentCourse.course.id}">
                        <td>
                            <strong>${studentCourse.course.name}</strong><br>
                            <span class="muted">${studentCourse.course.description}</span>
                        </td>
                        <td>
                            <span class="chip ${studentCourse.course.courseType eq 'SPECIAL' ? 'special' : 'mandatory'}">
                                ${studentCourse.course.courseType}
                            </span>
                        </td>
                        <td>
                            ${studentCourse.course.teacher.surname}
                            ${studentCourse.course.teacher.firstName}
                        </td>
                        <td>${studentCourse.course.freePlaces}</td>
                        <td>${studentCourse.grade}</td>
                        <td>
                            <div class="actions">
                                <form method="post" action="${pageContext.request.contextPath}/students/${student.id}/grades/${studentCourse.course.id}" class="inline-form">
                                    <label for="grade-${studentCourse.course.id}" class="muted">Оценка</label>
                                    <input id="grade-${studentCourse.course.id}" type="number" name="grade" min="2" max="5" value="${studentCourse.grade}" style="width: 88px;">
                                    <button type="submit">Сохранить</button>
                                </form>
                                <c:if test="${studentCourse.course.courseType eq 'SPECIAL'}">
                                    <form method="post" action="${pageContext.request.contextPath}/students/${student.id}/enrollments/${studentCourse.course.id}/delete">
                                        <button type="submit" class="button-danger">Отчислить</button>
                                    </form>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="panel">
            <h2>Записать на спецкурс</h2>
            <c:choose>
                <c:when test="${empty availableCourses}">
                    <p class="muted">Свободных спецкурсов для записи нет.</p>
                </c:when>
                <c:otherwise>
                    <form id="enrollment-form" method="post" action="${pageContext.request.contextPath}/students/${student.id}/enrollments" class="stack">
                        <div>
                            <label for="courseId">Спецкурс</label>
                            <select id="courseId" name="courseId">
                                <c:forEach items="${availableCourses}" var="course">
                                    <option value="${course.id}">
                                        ${course.name} (${course.freePlaces} мест)
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <button type="submit">Записать</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
</main>
</body>
</html>
