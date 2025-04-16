package com.example.neoflextesttask.controllers;

import com.example.neoflextesttask.dto.VacationPay;
import com.example.neoflextesttask.util.CalculatorUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/vacations")
public class CalculatorController {
    public String getDayAddition(int num) {
        int preLastDigit = num % 100 / 10;
        if (preLastDigit == 1) {
            return " дней";
        }

        switch (num % 10) {
            case 1:
                return " день";
            case 2:
            case 3:
            case 4:
                return " дня";
            default:
                return " дней";
        }
    }

    @PostMapping("/calculate")
    public ResponseEntity<String> calculateVacationMoney
            (@RequestBody @Valid VacationPay vacationPay) {
        CalculatorUtil calculatorUtil = new CalculatorUtil();

        String days = getDayAddition(vacationPay.getVacationDays());
        return ResponseEntity.ok("Ваши отпускные за " + vacationPay.getVacationDays() +
                days + " составили: " + calculatorUtil.calculate(vacationPay));
    }
}
