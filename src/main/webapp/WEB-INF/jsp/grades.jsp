<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru"><head><meta charset="UTF-8"><title>Оценки</title><link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"><link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css"></head>
<body><main class="page">
    <section class="hero"><div><h1>Оценки</h1><p>Просмотр оценок студента и выставление оценок преподавателем.</p></div><nav class="top-nav"><a href="${pageContext.request.contextPath}/students">Студенты</a><a href="${pageContext.request.contextPath}/courses">Курсы</a></nav></section>
    <c:if test="${not empty successMessage}"><div class="message success">${successMessage}</div></c:if>
    <section class="grid">
        <div class="panel"><h2>Мои оценки</h2><p class="muted">${student.surname} ${student.firstName}</p><table id="student-grades-table"><thead><tr><th>Курс</th><th>Оценка</th></tr></thead><tbody><c:forEach items="${studentCourses}" var="studentCourse"><tr><td>${studentCourse.course.name}</td><td>${studentCourse.grade}</td></tr></c:forEach></tbody></table></div>
        <div class="panel"><h2>Выставить оценки</h2>
            <form method="get" action="${pageContext.request.contextPath}/grades" class="stack">
                <div><label>Курс</label><select name="courseId"><c:forEach items="${courses}" var="course"><option value="${course.id}" <c:if test="${selectedCourseId == course.id}">selected</c:if>>${course.name}</option></c:forEach></select></div>
                <div><label>Группа</label><select name="groupId"><c:forEach items="${groups}" var="group"><option value="${group.id}" <c:if test="${selectedGroupId == group.id}">selected</c:if>>${group.name}</option></c:forEach></select></div>
                <button type="submit">Показать студентов</button>
            </form>
            <c:if test="${not empty students}">
                <form id="grades-form" method="post" action="${pageContext.request.contextPath}/grades" class="stack">
                    <input type="hidden" name="courseId" value="${selectedCourseId}"><input type="hidden" name="groupId" value="${selectedGroupId}">
                    <table><thead><tr><th>Студент</th><th>Оценка</th></tr></thead><tbody>
                    <c:forEach items="${students}" var="row"><tr><td>${row.surname} ${row.firstName}<input type="hidden" name="studentIds" value="${row.id}"></td><td><input type="number" name="grades" min="2" max="5" value="5"></td></tr></c:forEach>
                    </tbody></table><button type="submit">Сохранить</button>
                </form>
            </c:if>
        </div>
    </section>
</main></body></html>
