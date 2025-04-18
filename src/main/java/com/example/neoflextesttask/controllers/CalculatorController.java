package com.example.neoflextesttask.controllers;

import com.example.neoflextesttask.util.CalculatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.example.neoflextesttask.util.CalculatorUtil.maxDays;
import static com.example.neoflextesttask.util.CalculatorUtil.minDays;

@RestController
@RequestMapping("/api/vacations")
@RequiredArgsConstructor
public class CalculatorController {
    private final CalculatorUtil calculatorUtil;

    @GetMapping("/calculate")
    public ResponseEntity<String> calculateVacationMoney
            (@RequestParam BigDecimal salaryPerYear,
             @RequestParam(required = false) Integer vacationDays,
             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (salaryPerYear.intValue() < 0) {
            return ResponseEntity.badRequest().body("Зарплата не может быть отрицательной!");
        }

        if (startDate == null && endDate == null && vacationDays != null) {

            if (vacationDays < minDays || vacationDays > maxDays) {
                return ResponseEntity.badRequest().body("Количество дней должно быть в " +
                        "диапазоне от " + minDays + " до " + maxDays + " дней!");
            }

            String days = calculatorUtil.getDayAddition(vacationDays);

            return ResponseEntity.ok("Ваши отпускные за " + vacationDays +
                    days + " составили: " + calculatorUtil.calculate(salaryPerYear, vacationDays));

        } else if (startDate != null && endDate == null && vacationDays != null) {

            if (vacationDays < minDays || vacationDays > maxDays) {
                return ResponseEntity.badRequest().body("Количество дней должно быть в " +
                        "диапазоне от " + minDays + " до " + maxDays + " дней!");
            }

            LocalDate endDateNew = startDate.plusDays(vacationDays - 1);
            int paymentDays = calculatorUtil.countPaymentDays(startDate, endDateNew);
            String days = calculatorUtil.getDayAddition(paymentDays);

            return ResponseEntity.ok("Ваши отпускные за " + paymentDays + days +
                    " составили: " + calculatorUtil.calculate(salaryPerYear, startDate, endDateNew));

        } else if (startDate != null && endDate != null && vacationDays == null) {

            if (startDate.isAfter(endDate)) {
                return ResponseEntity.badRequest().body("Дата начала отпуска должна быть раньше конца отпуска!");
            }

            int paymentDays = calculatorUtil.countPaymentDays(startDate, endDate);
            String days = calculatorUtil.getDayAddition(paymentDays);

            return ResponseEntity.ok("Ваши отпускные за " + paymentDays + days +
                    " составили: " + calculatorUtil.calculate(salaryPerYear, startDate, endDate));

        }

        return ResponseEntity.badRequest().body("Ошибка в заполнении параметров!");
    }
}
