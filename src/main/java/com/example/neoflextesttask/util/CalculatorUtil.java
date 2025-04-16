package com.example.neoflextesttask.util;

import com.example.neoflextesttask.dto.VacationPay;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorUtil {
    private static final BigDecimal months = new BigDecimal("12");
    private static final BigDecimal ratio = new BigDecimal("29.3");
    private static final int scale = 2;

    public BigDecimal calculate(VacationPay vacationPay) {
        return vacationPay.getSalaryPerYear()
                .divide(months, scale, RoundingMode.HALF_UP)
                .divide(ratio, scale, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(vacationPay.getVacationDays()));
    }
}
