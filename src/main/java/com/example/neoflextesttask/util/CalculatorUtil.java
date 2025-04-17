package com.example.neoflextesttask.util;

import com.example.neoflextesttask.dto.VacationPay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class CalculatorUtil {
    private static final BigDecimal months = new BigDecimal("12");
    private static final BigDecimal ratio = new BigDecimal("29.3");
    private static final int scale = 2;
    private static final Set<LocalDate> holidays = Set.of(
            LocalDate.of(2025, Month.JANUARY, 1),
            LocalDate.of(2025, Month.JANUARY, 2),
            LocalDate.of(2025, Month.JANUARY, 3),
            LocalDate.of(2025, Month.JANUARY, 6),
            LocalDate.of(2025, Month.JANUARY, 7),
            LocalDate.of(2025, Month.JANUARY, 8),

            LocalDate.of(2025, Month.MAY, 1),
            LocalDate.of(2025, Month.MAY, 2),
            LocalDate.of(2025, Month.MAY, 8),
            LocalDate.of(2025, Month.MAY, 9),

            LocalDate.of(2025, Month.JUNE, 12),
            LocalDate.of(2025, Month.JUNE, 13),

            LocalDate.of(2025, Month.NOVEMBER, 3),
            LocalDate.of(2025, Month.NOVEMBER, 4),

            LocalDate.of(2025, Month.DECEMBER, 31)
    );

    public BigDecimal calculate(VacationPay vacationPay) {
        return vacationPay.getSalaryPerYear()
                .divide(months, scale, RoundingMode.HALF_UP)
                .divide(ratio, scale, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(vacationPay.getVacationDays()));
    }

    public BigDecimal calculateMoreAccurate(VacationPay vacationPay) {
        int paymentDays = countPaymentDays(vacationPay);

        return vacationPay.getSalaryPerYear()
                .divide(months, scale, RoundingMode.HALF_UP)
                .divide(ratio, scale, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(paymentDays));
    }

    public int countPaymentDays(VacationPay vacationPay) {
        LocalDate start = vacationPay.getStartDate();
        LocalDate end = vacationPay.getEndDate();

        AtomicInteger paymentDays = new AtomicInteger();
        start.datesUntil(end.plusDays(1)).forEach(date -> {
            if (checkDate(date)) {
                paymentDays.getAndIncrement();
            }
        });

        return paymentDays.intValue();
    }

    private boolean checkDate(LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY ||
        holidays.contains(date)) {
            return false;
        }

        return true;
    }

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
}
