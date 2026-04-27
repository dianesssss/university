<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Расписание</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<main class="page">
    <section class="hero">
        <div>
            <h1>Расписание</h1>
            <p>Занятия по группам: курс, аудитория, дата, время и преподаватель.</p>
        </div>
        <nav class="top-nav">
            <a href="${pageContext.request.contextPath}/students">Студенты</a>
            <a href="${pageContext.request.contextPath}/courses">Курсы</a>
            <a href="${pageContext.request.contextPath}/grades">Оценки</a>
        </nav>
    </section>

    <c:if test="${not empty successMessage}"><div class="message success">${successMessage}</div></c:if>

    <section class="grid">
        <div class="panel">
            <h2>Занятия</h2>
            <form method="get" action="${pageContext.request.contextPath}/schedule" class="filters">
                <div style="flex: 1 1 220px;">
                    <label for="groupId">Группа</label>
                    <select id="groupId" name="groupId">
                        <option value="">Все группы</option>
                        <c:forEach items="${groups}" var="group">
                            <option value="${group.id}" <c:if test="${selectedGroupId == group.id}">selected</c:if>>${group.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div style="align-self:end;"><button type="submit">Показать</button></div>
            </form>
            <table id="schedule-table">
                <thead>
                <tr><th>Группа</th><th>Курс</th><th>Аудитория</th><th>Дата</th><th>Время</th><th>Преподаватель</th></tr>
                </thead>
                <tbody>
                <c:forEach items="${lessonGroups}" var="row">
                    <tr>
                        <td>${row.group.name}</td>
                        <td>${row.lesson.course.name}</td>
                        <td>${row.lesson.classroom.roomNumber}</td>
                        <td>${row.lesson.lessonDate}</td>
                        <td>${row.lesson.startTime} - ${row.lesson.endTime}</td>
                        <td>${row.lesson.teacher.surname} ${row.lesson.teacher.firstName}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="panel">
            <h2>Добавить занятие</h2>
            <form id="lesson-form" method="post" action="${pageContext.request.contextPath}/schedule" class="stack">
                <div><label>Курс</label><select name="courseId"><c:forEach items="${courses}" var="course"><option value="${course.id}">${course.name}</option></c:forEach></select></div>
                <div><label>Аудитория</label><select name="classroomId"><c:forEach items="${classrooms}" var="classroom"><option value="${classroom.id}">${classroom.roomNumber}</option></c:forEach></select></div>
                <div><label>Преподаватель</label><select name="teacherId"><c:forEach items="${teachers}" var="teacher"><option value="${teacher.id}">${teacher.surname} ${teacher.firstName}</option></c:forEach></select></div>
                <div><label>Дата</label><input type="date" name="lessonDate" required></div>
                <div><label>Начало</label><input type="time" name="startTime" required></div>
                <div><label>Конец</label><input type="time" name="endTime" required></div>
                <div>
                    <label>Группы</label>
                    <select name="groupIds" multiple size="4">
                        <c:forEach items="${groups}" var="group"><option value="${group.id}">${group.name}</option></c:forEach>
                    </select>
                </div>
                <button type="submit">Сохранить</button>
            </form>
        </div>
    </section>
</main>
</body>
</html>
