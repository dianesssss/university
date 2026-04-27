package ru.msu.cmc.webprac.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.msu.cmc.webprac.dao.CourseDAO;
import ru.msu.cmc.webprac.dao.StudentCourseDAO;
import ru.msu.cmc.webprac.dao.StudentDAO;
import ru.msu.cmc.webprac.dao.StudyGroupDAO;
import ru.msu.cmc.webprac.dto.StudentForm;
import ru.msu.cmc.webprac.model.Course;
import ru.msu.cmc.webprac.model.CourseType;
import ru.msu.cmc.webprac.model.Student;
import ru.msu.cmc.webprac.model.StudentCourse;
import ru.msu.cmc.webprac.model.StudyGroup;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentPortalService {

    private final StudentDAO studentDAO;
    private final StudyGroupDAO studyGroupDAO;
    private final CourseDAO courseDAO;
    private final StudentCourseDAO studentCourseDAO;

    public StudentPortalService(
            StudentDAO studentDAO,
            StudyGroupDAO studyGroupDAO,
            CourseDAO courseDAO,
            StudentCourseDAO studentCourseDAO
    ) {
        this.studentDAO = studentDAO;
        this.studyGroupDAO = studyGroupDAO;
        this.courseDAO = courseDAO;
        this.studentCourseDAO = studentCourseDAO;
    }

    public List<Student> findStudents(String searchText, Long groupId) {
        List<Student> students = hasText(searchText) ? studentDAO.findByFullNameLike(searchText) : studentDAO.getAll();
        return students.stream()
                .filter(student -> groupId == null || Objects.equals(student.getGroup().getId(), groupId))
                .sorted(Comparator.comparing(Student::getSurname)
                        .thenComparing(Student::getFirstName)
                        .thenComparing(Student::getId))
                .toList();
    }

    public List<StudyGroup> getGroups() {
        return studyGroupDAO.getAll().stream()
                .sorted(Comparator.comparing(StudyGroup::getStudyYear).thenComparing(StudyGroup::getName))
                .toList();
    }

    public Student getStudent(Long studentId) {
        return studentDAO.getById(studentId);
    }

    public StudyGroup getGroup(Long groupId) {
        return studyGroupDAO.getById(groupId);
    }

    public Student updateStudent(Student student) {
        return studentDAO.update(student);
    }

    public void deleteStudent(Long studentId) {
        studentDAO.delete(studentId);
    }

    public List<StudentCourse> getStudentCourses(Long studentId) {
        return studentCourseDAO.findByStudentId(studentId).stream()
                .sorted(Comparator.comparing((StudentCourse sc) -> sc.getCourse().getCourseType())
                        .thenComparing(sc -> sc.getCourse().getName()))
                .toList();
    }

    public List<Course> getAvailableSpecialCourses(Long studentId) {
        Set<Long> enrolledCourseIds = studentCourseDAO.findByStudentId(studentId).stream()
                .map(studentCourse -> studentCourse.getCourse().getId())
                .collect(Collectors.toSet());

        return courseDAO.findSpecialCourses().stream()
                .filter(course -> !enrolledCourseIds.contains(course.getId()))
                .sorted(Comparator.comparing(Course::getName))
                .toList();
    }

    public OperationResult createStudent(StudentForm form) {
        StudyGroup studyGroup = studyGroupDAO.getById(form.getGroupId());
        if (studyGroup == null) {
            return OperationResult.error("Выбранная группа не существует.");
        }

        String normalizedEmail = form.getEmail().trim().toLowerCase();
        if (studentDAO.findByEmail(normalizedEmail) != null) {
            return OperationResult.error("Не удалось добавить студента: email уже используется.");
        }

        Student student = Student.builder()
                .surname(form.getSurname().trim())
                .firstName(form.getFirstName().trim())
                .patronymic(hasText(form.getPatronymic()) ? form.getPatronymic().trim() : null)
                .birthDate(form.getBirthDate())
                .email(normalizedEmail)
                .group(studyGroup)
                .build();

        try {
            Student savedStudent = studentDAO.addStudentWithMandatoryCourses(student);
            return OperationResult.success("Студент добавлен, обязательные курсы назначены автоматически.", savedStudent.getId());
        } catch (RuntimeException ex) {
            return OperationResult.error("Не удалось добавить студента. Проверьте уникальность email и корректность данных.");
        }
    }

    public OperationResult enrollToSpecialCourse(Long studentId, Long courseId) {
        Student student = studentDAO.getById(studentId);
        if (student == null) {
            return OperationResult.error("Студент не найден.");
        }

        Course course = courseDAO.getById(courseId);
        if (course == null) {
            return OperationResult.error("Курс не найден.");
        }

        if (course.getCourseType() != CourseType.SPECIAL) {
            return OperationResult.error("Запись доступна только на спецкурсы.");
        }

        if (studentCourseDAO.findByStudentIdAndCourseId(studentId, courseId) != null) {
            return OperationResult.error("Студент уже записан на этот курс.");
        }

        if (course.getFreePlaces() == null || course.getFreePlaces() <= 0) {
            return OperationResult.error("На курсе больше нет свободных мест.");
        }

        boolean enrolled = studentCourseDAO.enrollToSpecialCourse(studentId, courseId);
        if (!enrolled) {
            return OperationResult.error("Не удалось записать студента на курс.");
        }

        return OperationResult.success("Запись на спецкурс выполнена.");
    }

    public OperationResult unenrollFromSpecialCourse(Long studentId, Long courseId) {
        StudentCourse studentCourse = studentCourseDAO.findByStudentIdAndCourseId(studentId, courseId);
        if (studentCourse == null) {
            return OperationResult.error("Запись на курс не найдена.");
        }

        if (studentCourse.getCourse().getCourseType() != CourseType.SPECIAL) {
            return OperationResult.error("Отчисление доступно только для спецкурсов.");
        }

        boolean removed = studentCourseDAO.unenrollFromSpecialCourse(studentId, courseId);
        if (!removed) {
            return OperationResult.error("Не удалось отменить запись на курс.");
        }

        return OperationResult.success("Запись на спецкурс отменена.");
    }

    public OperationResult updateGrade(Long studentId, Long courseId, Integer grade) {
        StudentCourse studentCourse = studentCourseDAO.findByStudentIdAndCourseId(studentId, courseId);
        if (studentCourse == null) {
            return OperationResult.error("Нельзя выставить оценку: запись на курс не найдена.");
        }

        studentCourseDAO.updateGrade(studentId, courseId, grade);
        return OperationResult.success("Оценка обновлена.");
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    public record OperationResult(boolean success, String message, Long entityId) {

        public static OperationResult success(String message) {
            return new OperationResult(true, message, null);
        }

        public static OperationResult success(String message, Long entityId) {
            return new OperationResult(true, message, entityId);
        }

        public static OperationResult error(String message) {
            return new OperationResult(false, message, null);
        }
    }
}
