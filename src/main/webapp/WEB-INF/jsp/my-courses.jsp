<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru"><head><meta charset="UTF-8"><title>Мои курсы</title><link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"><link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css"></head>
<body><main class="page">
    <section class="hero"><div><h1>Мои курсы</h1><p>${student.surname} ${student.firstName}: обязательные курсы и спецкурсы.</p></div><nav class="top-nav"><a href="${pageContext.request.contextPath}/courses?studentId=${student.id}">Курсы</a><a href="${pageContext.request.contextPath}/grades?studentId=${student.id}">Мои оценки</a></nav></section>
    <c:if test="${not empty successMessage}"><div class="message success">${successMessage}</div></c:if><c:if test="${not empty errorMessage}"><div class="message error">${errorMessage}</div></c:if>
    <section class="panel"><table id="my-courses-table"><thead><tr><th>Курс</th><th>Тип</th><th>Оценка</th><th></th></tr></thead><tbody>
    <c:forEach items="${studentCourses}" var="studentCourse"><tr><td>${studentCourse.course.name}</td><td>${studentCourse.course.courseType}</td><td>${studentCourse.grade}</td><td><c:if test="${studentCourse.course.courseType eq 'SPECIAL'}"><form method="post" action="${pageContext.request.contextPath}/my-courses/${studentCourse.course.id}/unenroll"><input type="hidden" name="studentId" value="${student.id}"><button type="submit" class="button-danger">Отписаться</button></form></c:if></td></tr></c:forEach>
    </tbody></table></section>
</main></body></html>
