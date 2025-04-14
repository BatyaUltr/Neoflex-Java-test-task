package com.example.neoflextesttask.controllers;

import com.example.neoflextesttask.dto.VacationPay;
import com.example.neoflextesttask.util.CalculatorUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/vacations")
public class CalculatorController {
    @PostMapping("/calculate")
    public ResponseEntity<String> calculateVacationMoney
            (@RequestBody @Valid VacationPay vacationPay) {
        CalculatorUtil calculatorUtil = new CalculatorUtil();
        System.out.println("Ваши отпускные за " + vacationPay.getVacationDays() +
                " дней составили: " + calculatorUtil.calculate(vacationPay));

        return ResponseEntity.ok("Ваши отпускные за " + vacationPay.getVacationDays() +
                " дней составили: " + calculatorUtil.calculate(vacationPay));
    }
}
