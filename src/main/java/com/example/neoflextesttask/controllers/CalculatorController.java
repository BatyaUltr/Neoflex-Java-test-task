package com.example.neoflextesttask.controllers;

import com.example.neoflextesttask.util.CalculatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/vacations")
@RequiredArgsConstructor
public class CalculatorController {
    private final CalculatorUtil calculatorUtil;
    private static final Integer minDays = 0;
    private static final Integer maxDays = 365;

    @GetMapping("/calculate")
    public ResponseEntity<String> calculateVacationMoney
            (@RequestParam BigDecimal salaryPerYear,
             @RequestParam(required = false) Integer vacationDays,
             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

//        CalculatorUtil calculatorUtil = new CalculatorUtil();

        ResponseEntity<String> response = null;

        if (salaryPerYear.intValue() < 0) {
            return ResponseEntity.ok("Зарплата не может быть отрицательной!");
        }

        if (startDate == null && endDate == null && vacationDays != null) {
            if (vacationDays < minDays || vacationDays > maxDays) {
                return ResponseEntity.ok("Количество дней должно быть в " +
                        "диапазоне от " + minDays + " до " + maxDays + " дней!");
            }

            String days = calculatorUtil.getDayAddition(vacationDays);
            response = ResponseEntity.ok("Ваши отпускные за " + vacationDays +
                    days + " составили: " + calculatorUtil.calculate(salaryPerYear, vacationDays));
        } else if (startDate != null && endDate != null && vacationDays == null) {
            if (startDate.isAfter(endDate)) {
                return ResponseEntity.ok("Дата начала отпуска должна быть раньше конца отпуска!");
            }

            int paymentDays = calculatorUtil.countPaymentDays(startDate, endDate);
            String days = calculatorUtil.getDayAddition(paymentDays);
            response = ResponseEntity.ok("Ваши отпускные за " + paymentDays + days +
                    " составили: " + calculatorUtil.calculateMoreAccurate(salaryPerYear, startDate, endDate));
        }

        if (response == null) {
            return ResponseEntity.ok("Вы неправильно заполнили параметры!");
        }

        return response;
    }
}
