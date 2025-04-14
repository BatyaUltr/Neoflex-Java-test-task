package com.example.neoflextesttask.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class VacationPay {
    @NotNull(message = "Зарплата не может быть равна null!")
    @Positive(message = "Зарплата не может быть отрицательной!")
    private BigDecimal salaryPerYear;

    @NotNull(message = "Количество отпускных дней не " +
            "может быть равно null!")
    @Min(value = 1, message = "Количество отпускных дней не " +
            "может быть меньше 1!")
    @Max(value = 365, message = "Количество отпускных дней " +
            "не может превышать 365!")
    private int vacationDays;
}
