<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Ошибка</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<main class="page">
    <section class="hero">
        <div>
            <h1>Ошибка ${statusCode}</h1>
            <p>Запрос не удалось обработать. Вернитесь к списку студентов и повторите действие.</p>
        </div>
        <a href="${pageContext.request.contextPath}/students">К списку студентов</a>
    </section>
</main>
</body>
</html>
