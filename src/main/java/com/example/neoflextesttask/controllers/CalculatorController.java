package com.example.neoflextesttask.controllers;

import com.example.neoflextesttask.dto.VacationPay;
import com.example.neoflextesttask.util.CalculatorUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vacations")
public class CalculatorController {
    @PostMapping("/calculate")
    public ResponseEntity<String> calculateVacationMoney
            (@RequestBody VacationPay vacationPay) {
        CalculatorUtil calculatorUtil = new CalculatorUtil();

        if (vacationPay.getSalaryPerYear().intValue() < 0) {
            return ResponseEntity.ok("Зарплата не может быть отрицательной!");
        }

        if (vacationPay.getVacationDays() < 1 || vacationPay.getVacationDays() > 365) {
            return ResponseEntity.ok("Количество дней должно быть в " +
                    "диапазоне от 1 до 365 дней!");
        }

        String days = calculatorUtil.getDayAddition(vacationPay.getVacationDays());
        return ResponseEntity.ok("Ваши отпускные за " + vacationPay.getVacationDays() +
                days + " составили: " + calculatorUtil.calculate(vacationPay));
    }

    @PostMapping("/calculate_accurate")
    public ResponseEntity<String> calculateVacationMoneyMoreAccurate
            (@RequestBody VacationPay vacationPay) {
        CalculatorUtil calculatorUtil = new CalculatorUtil();

        if (vacationPay.getSalaryPerYear().intValue() < 0) {
            return ResponseEntity.ok("Зарплата не может быть отрицательной!");
        }

        if (vacationPay.getStartDate().isAfter(vacationPay.getEndDate())) {
            return ResponseEntity.ok("Дата начала отпуска должна быть раньше конца отпуска!");
        }

        int paymentDays = calculatorUtil.countPaymentDays(vacationPay);
        String days = calculatorUtil.getDayAddition(paymentDays);
        return ResponseEntity.ok("Ваши отпускные за " + paymentDays + days +
                " составили: " + calculatorUtil.calculateMoreAccurate(vacationPay));
    }
}
