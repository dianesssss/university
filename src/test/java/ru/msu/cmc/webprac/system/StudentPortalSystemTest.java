package ru.msu.cmc.webprac.system;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.msu.cmc.webprac.dao.CourseDAO;
import ru.msu.cmc.webprac.dao.StudentCourseDAO;
import ru.msu.cmc.webprac.dao.StudentDAO;
import ru.msu.cmc.webprac.model.Course;
import ru.msu.cmc.webprac.model.Student;
import ru.msu.cmc.webprac.model.StudentCourse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StudentPortalSystemTest extends AbstractTestNGSpringContextTests {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private StudentCourseDAO studentCourseDAO;

    @Autowired
    private CourseDAO courseDAO;

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = new HtmlUnitDriver(true);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void shouldRenderStudentsPageAndFilterByGroup() {
        open("/students?search=Smirnov&groupId=1");

        String body = bodyText();
        assertTrue(body.contains("Smirnov Aleksey Dmitrievich"));
        assertFalse(body.contains("Fedorova Elena Pavlovna"));
    }

    @Test
    public void shouldShowEmptySearchResult() {
        open("/students?search=NoSuchStudent");

        assertTrue(bodyText().contains("По вашему запросу студенты не найдены."));
    }

    @Test
    public void shouldCreateStudentAndAssignMandatoryCourses() {
        open("/students");

        typeInForm("student-form", "surname", "Sokolov");
        typeInForm("student-form", "firstName", "Artem");
        typeInForm("student-form", "patronymic", "Olegovich");
        typeInForm("student-form", "birthDate", "2005-09-18");
        typeInForm("student-form", "email", "sokolov@test.msu.ru");
        selectInForm("student-form", "groupId", "1");
        submit("student-form");

        assertContains("Студент добавлен, обязательные курсы назначены автоматически.");
        Student savedStudent = studentDAO.findByEmail("sokolov@test.msu.ru");
        assertNotNull(savedStudent);
        assertEquals(studentCourseDAO.findByStudentId(savedStudent.getId()).size(), 2);
    }

    @Test
    public void shouldRejectStudentCreationWithDuplicateEmail() {
        open("/students");

        typeInForm("student-form", "surname", "Sokolov");
        typeInForm("student-form", "firstName", "Artem");
        typeInForm("student-form", "patronymic", "Olegovich");
        typeInForm("student-form", "birthDate", "2005-09-18");
        typeInForm("student-form", "email", "smirnov1@student.msu.ru");
        selectInForm("student-form", "groupId", "1");
        submit("student-form");

        assertContains("Не удалось добавить студента: email уже используется.");
    }

    @Test
    public void shouldRejectStudentCreationWithValidationErrors() throws IOException {
        post("/students", Map.of("email", "wrong-email"));

        String body = bodyText();
        assertTrue(body.contains("Фамилия обязательна."), body);
        assertTrue(body.contains("Имя обязательно."), body);
        assertTrue(body.contains("Дата рождения обязательна."), body);
        assertTrue(body.contains("Некорректный email."), body);
        assertTrue(body.contains("Выберите учебную группу."), body);
    }

    @Test
    public void shouldRedirectUnknownStudentToList() {
        open("/students/999");

        assertTrue(bodyText().contains("Студент не найден."));
    }

    @Test
    public void shouldEnrollStudentToSpecialCourse() {
        open("/students/3");
        select("courseId", "4");
        submit("enrollment-form");

        assertTrue(bodyText().contains("Запись на спецкурс выполнена."));
        assertNotNull(studentCourseDAO.findByStudentIdAndCourseId(3L, 4L));
    }

    @Test
    public void shouldRejectEnrollmentWhenCourseIsMissing() throws IOException {
        post("/students/3/enrollments", Map.of("courseId", "999"));

        assertTrue(bodyText().contains("Курс не найден."));
    }

    @Test
    public void shouldRejectEnrollmentWhenAlreadyEnrolled() throws IOException {
        post("/students/1/enrollments", Map.of("courseId", "3"));

        assertTrue(bodyText().contains("Студент уже записан на этот курс."));
    }

    @Test
    public void shouldRejectEnrollmentWhenNoFreePlaces() throws IOException {
        Course course = courseDAO.getById(4L);
        course.setFreePlaces(0);
        courseDAO.update(course);

        post("/students/3/enrollments", Map.of("courseId", "4"));

        assertTrue(bodyText().contains("На курсе больше нет свободных мест."));
        assertNull(studentCourseDAO.findByStudentIdAndCourseId(3L, 4L));
    }

    @Test
    public void shouldRejectEnrollmentToMandatoryCourse() throws IOException {
        post("/students/3/enrollments", Map.of("courseId", "1"));

        assertTrue(bodyText().contains("Запись доступна только на спецкурсы."));
    }

    @Test
    public void shouldRejectEnrollmentWithoutCourse() throws IOException {
        post("/students/3/enrollments", Map.of());

        assertTrue(bodyText().contains("Выберите спецкурс."));
    }

    @Test
    public void shouldUnenrollStudentFromSpecialCourse() throws IOException {
        post("/students/2/enrollments/4/delete", Map.of());

        assertTrue(bodyText().contains("Запись на спецкурс отменена."));
        assertNull(studentCourseDAO.findByStudentIdAndCourseId(2L, 4L));
    }

    @Test
    public void shouldRejectUnenrollWhenEnrollmentDoesNotExist() throws IOException {
        post("/students/3/enrollments/4/delete", Map.of());

        assertTrue(bodyText().contains("Запись на курс не найдена."));
    }

    @Test
    public void shouldRejectUnenrollFromMandatoryCourse() throws IOException {
        post("/students/1/enrollments/1/delete", Map.of());

        assertTrue(bodyText().contains("Отчисление доступно только для спецкурсов."));
    }

    @Test
    public void shouldUpdateGrade() throws IOException {
        post("/students/1/grades/1", Map.of("grade", "5"));

        assertTrue(bodyText().contains("Оценка обновлена."));
        StudentCourse studentCourse = studentCourseDAO.findByStudentIdAndCourseId(1L, 1L);
        assertNotNull(studentCourse);
        assertEquals(studentCourse.getGrade(), Integer.valueOf(5));
    }

    @Test
    public void shouldRejectGradeOutsideAllowedRange() throws IOException {
        post("/students/1/grades/1", Map.of("grade", "6"));

        assertTrue(bodyText().contains("Оценка должна быть в диапазоне от 2 до 5."));
    }

    @Test
    public void shouldRejectGradeWithoutValue() throws IOException {
        post("/students/1/grades/1", Map.of());

        assertTrue(bodyText().contains("Укажите оценку."));
    }

    @Test
    public void shouldRejectGradeForMissingEnrollment() throws IOException {
        post("/students/3/grades/4", Map.of("grade", "5"));

        assertTrue(bodyText().contains("Нельзя выставить оценку: запись на курс не найдена."));
    }

    @Test
    public void shouldRenderErrorPageForUnexpectedUrl() {
        open("/unknown-page");

        assertTrue(bodyText().contains("Ошибка 404"));
    }

    @Test
    public void shouldRenderAllPagesDeclaredInFirstReport() {
        open("/schedule");
        assertContains("Расписание");

        open("/courses");
        assertContains("Курсы");

        open("/my-courses?studentId=1");
        assertContains("Мои курсы");

        open("/grades?studentId=1");
        assertContains("Оценки");

        open("/groups");
        assertContains("Группы");

        open("/classrooms");
        assertContains("Аудитории");
    }

    @Test
    public void shouldCreateTeacherCourseFromCoursesPage() {
        open("/courses");

        typeInForm("course-form", "name", "System Testing");
        selectInForm("course-form", "courseType", "SPECIAL");
        typeInForm("course-form", "maxStudents", "12");
        typeInForm("course-form", "description", "Course from system test");
        selectInForm("course-form", "teacherId", "1");
        submit("course-form");

        assertContains("Курс добавлен.");
        assertContains("System Testing");
    }

    @Test
    public void shouldCreateGroupFromGroupsPage() {
        open("/groups");

        typeInForm("group-form", "name", "909");
        typeInForm("group-form", "studyYear", "3");
        selectInForm("group-form", "facultyId", "1");
        submit("group-form");

        assertContains("909");
    }

    @Test
    public void shouldCreateClassroomFromClassroomsPage() {
        open("/classrooms");

        typeInForm("classroom-form", "roomNumber", "SYS-1");
        typeInForm("classroom-form", "capacity", "32");
        selectInForm("classroom-form", "facultyId", "1");
        submit("classroom-form");

        assertContains("SYS-1");
    }

    @Test
    public void shouldCreateLessonFromSchedulePage() throws IOException {
        post("/schedule", Map.of(
                "courseId", "1",
                "classroomId", "1",
                "teacherId", "1",
                "lessonDate", "2026-04-28",
                "startTime", "12:00",
                "endTime", "13:30",
                "groupIds", "1"
        ));

        assertContains("Занятие добавлено в расписание.");
    }

    private void open(String path) {
        driver.get(url(path));
    }

    private void post(String path, Map<String, String> params) throws IOException {
        Path formPage = Files.createTempFile("webprac-post-", ".html");
        Files.writeString(formPage, postFormHtml(path, params), StandardCharsets.UTF_8);
        driver.get(formPage.toUri().toString());
        driver.findElement(By.id("submit")).click();
        Files.deleteIfExists(formPage);
    }

    private String postFormHtml(String path, Map<String, String> params) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><body>");
        html.append("<form id=\"post-form\" method=\"post\" action=\"").append(url(path)).append("\">");
        params.forEach((name, value) -> html.append("<input type=\"hidden\" name=\"")
                .append(escape(name)).append("\" value=\"").append(escape(value)).append("\">"));
        html.append("<button id=\"submit\" type=\"submit\">Submit</button>");
        html.append("</form></body></html>");
        return html.toString();
    }

    private void type(String name, String value) {
        WebElement element = driver.findElement(By.name(name));
        element.clear();
        element.sendKeys(value);
    }

    private void typeInForm(String formId, String name, String value) {
        WebElement element = driver.findElement(By.id(formId)).findElement(By.name(name));
        element.clear();
        element.sendKeys(value);
    }

    private void select(String name, String value) {
        new Select(driver.findElement(By.name(name))).selectByValue(value);
    }

    private void selectInForm(String formId, String name, String value) {
        WebElement form = driver.findElement(By.id(formId));
        new Select(form.findElement(By.name(name))).selectByValue(value);
    }

    private void submit(String formId) {
        driver.findElement(By.cssSelector("#" + formId + " button[type='submit']")).click();
    }

    private void assertContains(String expected) {
        String body = bodyText();
        assertTrue(body.contains(expected), body);
    }

    private String bodyText() {
        return driver.findElement(By.tagName("body")).getText();
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    private String escape(String value) {
        return value.replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
