package ru.msu.cmc.webprac.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeForm {

    @NotNull(message = "Укажите оценку.")
    @Min(value = 2, message = "Оценка должна быть в диапазоне от 2 до 5.")
    @Max(value = 5, message = "Оценка должна быть в диапазоне от 2 до 5.")
    private Integer grade;
}
