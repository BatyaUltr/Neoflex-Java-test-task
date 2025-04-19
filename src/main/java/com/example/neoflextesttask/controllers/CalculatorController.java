package com.example.neoflextesttask.controllers;

import com.example.neoflextesttask.service.CalculatorService;
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
    private final CalculatorService calculatorService;

    @GetMapping("/calculate")
    public ResponseEntity<String> calculateVacationMoney
            (@RequestParam BigDecimal salaryPerYear,
             @RequestParam(required = false) Integer vacationDays,
             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return calculatorService.finalCalculation(salaryPerYear, vacationDays, startDate, endDate);
    }
}
