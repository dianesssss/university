package ru.msu.cmc.webprac.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentForm {

    @NotNull(message = "Выберите спецкурс.")
    private Long courseId;
}
