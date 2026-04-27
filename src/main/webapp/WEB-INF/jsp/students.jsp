<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Студенты и курсы</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<main class="page">
    <section class="hero">
        <div>
            <h1>Учебный портал<br>кафедры</h1>
            <p>Список студентов, фильтрация по группе, создание карточки студента и переход к управлению курсами.</p>
        </div>
        <nav class="top-nav">
            <a href="${pageContext.request.contextPath}/schedule">Расписание</a>
            <a href="${pageContext.request.contextPath}/courses">Курсы</a>
            <a href="${pageContext.request.contextPath}/groups">Группы</a>
            <a href="${pageContext.request.contextPath}/classrooms">Аудитории</a>
            <a href="${pageContext.request.contextPath}/students">Сбросить фильтры</a>
        </nav>
    </section>

    <c:if test="${not empty successMessage}">
        <div id="success-message" class="message success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div id="error-message" class="message error">${errorMessage}</div>
    </c:if>

    <section class="grid">
        <div class="panel">
            <h2>Студенты</h2>
            <form method="get" action="${pageContext.request.contextPath}/students" class="filters" id="student-filter-form">
                <div style="flex: 1 1 220px;">
                    <label for="search">Поиск по ФИО</label>
                    <input id="search" type="text" name="search" value="${search}" placeholder="Например, Smirnov">
                </div>
                <div style="flex: 1 1 220px;">
                    <label for="groupId">Группа</label>
                    <select id="groupId" name="groupId">
                        <option value="">Все группы</option>
                        <c:forEach items="${groups}" var="group">
                            <option value="${group.id}" <c:if test="${selectedGroupId == group.id}">selected</c:if>>
                                ${group.name} курс ${group.studyYear}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div style="flex: 0 0 180px; align-self: end;">
                    <button type="submit">Найти</button>
                </div>
            </form>

            <table id="students-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>ФИО</th>
                    <th>Группа</th>
                    <th>Email</th>
                    <th>Действия</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${students}" var="student">
                    <tr id="student-row-${student.id}">
                        <td>${student.id}</td>
                        <td>${student.surname} ${student.firstName} ${student.patronymic}</td>
                        <td>${student.group.name}</td>
                        <td>${student.email}</td>
                        <td>
                            <div class="actions">
                                <a class="button-link" href="${pageContext.request.contextPath}/students/${student.id}">Открыть</a>
                                <form method="post" action="${pageContext.request.contextPath}/students/${student.id}/edit" class="inline-form compact-form">
                                    <input name="surname" value="${student.surname}">
                                    <input name="firstName" value="${student.firstName}">
                                    <input name="patronymic" value="${student.patronymic}">
                                    <input name="birthDate" type="date" value="${student.birthDate}">
                                    <input name="email" value="${student.email}">
                                    <select name="groupId">
                                        <c:forEach items="${groups}" var="group">
                                            <option value="${group.id}" <c:if test="${student.group.id == group.id}">selected</c:if>>${group.name}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit">Редактировать</button>
                                </form>
                                <form method="post" action="${pageContext.request.contextPath}/students/${student.id}/delete">
                                    <button type="submit" class="button-danger">Удалить</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty students}">
                    <tr>
                        <td colspan="5" class="muted">По вашему запросу студенты не найдены.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>

        <div class="panel">
            <h2>Новый студент</h2>
            <p class="muted">После создания студент автоматически записывается на обязательные курсы своей группы.</p>
            <form:form id="student-form" modelAttribute="studentForm" method="post" action="${pageContext.request.contextPath}/students" class="stack">
                <div>
                    <label for="surname">Фамилия</label>
                    <form:input path="surname" id="surname"/>
                    <form:errors path="surname" cssClass="field-error"/>
                </div>
                <div>
                    <label for="firstName">Имя</label>
                    <form:input path="firstName" id="firstName"/>
                    <form:errors path="firstName" cssClass="field-error"/>
                </div>
                <div>
                    <label for="patronymic">Отчество</label>
                    <form:input path="patronymic" id="patronymic"/>
                </div>
                <div>
                    <label for="birthDate">Дата рождения</label>
                    <form:input path="birthDate" id="birthDate" type="date"/>
                    <form:errors path="birthDate" cssClass="field-error"/>
                </div>
                <div>
                    <label for="email">Email</label>
                    <form:input path="email" id="email" type="email"/>
                    <form:errors path="email" cssClass="field-error"/>
                </div>
                <div>
                    <label for="groupSelect">Группа</label>
                    <form:select path="groupId" id="groupSelect">
                        <form:option value="" label="Выберите группу"/>
                        <form:options items="${groups}" itemValue="id" itemLabel="name"/>
                    </form:select>
                    <form:errors path="groupId" cssClass="field-error"/>
                </div>
                <button type="submit">Создать студента</button>
            </form:form>
        </div>
    </section>
</main>
</body>
</html>
