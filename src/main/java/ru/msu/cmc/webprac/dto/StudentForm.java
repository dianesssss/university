package ru.msu.cmc.webprac.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class StudentForm {

    @NotBlank(message = "Фамилия обязательна.")
    private String surname;

    @NotBlank(message = "Имя обязательно.")
    private String firstName;

    private String patronymic;

    @NotNull(message = "Дата рождения обязательна.")
    @Past(message = "Дата рождения должна быть в прошлом.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank(message = "Email обязателен.")
    @Email(message = "Некорректный email.")
    private String email;

    @NotNull(message = "Выберите учебную группу.")
    private Long groupId;
}
