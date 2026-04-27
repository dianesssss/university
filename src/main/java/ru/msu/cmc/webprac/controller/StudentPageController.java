package ru.msu.cmc.webprac.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.msu.cmc.webprac.dto.EnrollmentForm;
import ru.msu.cmc.webprac.dto.GradeForm;
import ru.msu.cmc.webprac.dto.StudentForm;
import ru.msu.cmc.webprac.model.Student;
import ru.msu.cmc.webprac.model.StudyGroup;
import ru.msu.cmc.webprac.service.StudentPortalService;
import ru.msu.cmc.webprac.service.StudentPortalService.OperationResult;

import java.time.LocalDate;

@Controller
@RequestMapping("/students")
public class StudentPageController {

    private final StudentPortalService studentPortalService;

    public StudentPageController(StudentPortalService studentPortalService) {
        this.studentPortalService = studentPortalService;
    }

    @GetMapping
    public String studentsPage(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long groupId,
            Model model
    ) {
        if (!model.containsAttribute("studentForm")) {
            model.addAttribute("studentForm", new StudentForm());
        }

        model.addAttribute("students", studentPortalService.findStudents(search, groupId));
        model.addAttribute("groups", studentPortalService.getGroups());
        model.addAttribute("search", search == null ? "" : search);
        model.addAttribute("selectedGroupId", groupId);
        return "students";
    }

    @PostMapping
    public String createStudent(
            @Valid @ModelAttribute("studentForm") StudentForm studentForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("students", studentPortalService.findStudents(null, null));
            model.addAttribute("groups", studentPortalService.getGroups());
            model.addAttribute("search", "");
            return "students";
        }

        OperationResult result = studentPortalService.createStudent(studentForm);
        if (!result.success()) {
            model.addAttribute("students", studentPortalService.findStudents(null, null));
            model.addAttribute("groups", studentPortalService.getGroups());
            model.addAttribute("search", "");
            model.addAttribute("errorMessage", result.message());
            return "students";
        }

        redirectAttributes.addFlashAttribute("successMessage", result.message());
        return "redirect:/students/" + result.entityId();
    }

    @GetMapping("/{studentId}")
    public String studentDetails(@PathVariable Long studentId, Model model, RedirectAttributes redirectAttributes) {
        Student student = studentPortalService.getStudent(studentId);
        if (student == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Студент не найден.");
            return "redirect:/students";
        }

        if (!model.containsAttribute("enrollmentForm")) {
            model.addAttribute("enrollmentForm", new EnrollmentForm());
        }
        if (!model.containsAttribute("gradeForm")) {
            model.addAttribute("gradeForm", new GradeForm());
        }

        model.addAttribute("student", student);
        model.addAttribute("studentCourses", studentPortalService.getStudentCourses(studentId));
        model.addAttribute("availableCourses", studentPortalService.getAvailableSpecialCourses(studentId));
        return "student-details";
    }

    @PostMapping("/{studentId}/enrollments")
    public String enroll(
            @PathVariable Long studentId,
            @Valid @ModelAttribute("enrollmentForm") EnrollmentForm enrollmentForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/students/" + studentId;
        }

        OperationResult result = studentPortalService.enrollToSpecialCourse(studentId, enrollmentForm.getCourseId());
        redirectAttributes.addFlashAttribute(result.success() ? "successMessage" : "errorMessage", result.message());
        return "redirect:/students/" + studentId;
    }

    @PostMapping("/{studentId}/enrollments/{courseId}/delete")
    public String unenroll(
            @PathVariable Long studentId,
            @PathVariable Long courseId,
            RedirectAttributes redirectAttributes
    ) {
        OperationResult result = studentPortalService.unenrollFromSpecialCourse(studentId, courseId);
        redirectAttributes.addFlashAttribute(result.success() ? "successMessage" : "errorMessage", result.message());
        return "redirect:/students/" + studentId;
    }

    @PostMapping("/{studentId}/grades/{courseId}")
    public String updateGrade(
            @PathVariable Long studentId,
            @PathVariable Long courseId,
            @Valid @ModelAttribute("gradeForm") GradeForm gradeForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/students/" + studentId;
        }

        OperationResult result = studentPortalService.updateGrade(studentId, courseId, gradeForm.getGrade());
        redirectAttributes.addFlashAttribute(result.success() ? "successMessage" : "errorMessage", result.message());
        return "redirect:/students/" + studentId;
    }

    @PostMapping("/{studentId}/edit")
    public String updateStudent(
            @PathVariable Long studentId,
            @RequestParam String surname,
            @RequestParam String firstName,
            @RequestParam(required = false) String patronymic,
            @RequestParam LocalDate birthDate,
            @RequestParam String email,
            @RequestParam Long groupId,
            RedirectAttributes redirectAttributes
    ) {
        Student student = studentPortalService.getStudent(studentId);
        StudyGroup group = studentPortalService.getGroup(groupId);
        if (student == null || group == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Не удалось обновить студента.");
            return "redirect:/students";
        }

        student.setSurname(surname.trim());
        student.setFirstName(firstName.trim());
        student.setPatronymic(patronymic == null || patronymic.isBlank() ? null : patronymic.trim());
        student.setBirthDate(birthDate);
        student.setEmail(email.trim().toLowerCase());
        student.setGroup(group);
        studentPortalService.updateStudent(student);
        redirectAttributes.addFlashAttribute("successMessage", "Студент обновлен.");
        return "redirect:/students";
    }

    @PostMapping("/{studentId}/delete")
    public String deleteStudent(@PathVariable Long studentId, RedirectAttributes redirectAttributes) {
        studentPortalService.deleteStudent(studentId);
        redirectAttributes.addFlashAttribute("successMessage", "Студент удален.");
        return "redirect:/students";
    }
}
