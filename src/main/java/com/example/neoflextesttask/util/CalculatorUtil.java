package com.example.neoflextesttask.util;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CalculatorUtil {
    private static final BigDecimal months = new BigDecimal("12");
    private static final BigDecimal ratio = new BigDecimal("29.3");
    private static final int scale = 2;
    private static final Set<MonthDay> holidays = Set.of(
            MonthDay.of(Month.JANUARY, 1),
            MonthDay.of(Month.JANUARY, 2),
            MonthDay.of(Month.JANUARY, 3),
            MonthDay.of(Month.JANUARY, 4),
            MonthDay.of(Month.JANUARY, 5),
            MonthDay.of(Month.JANUARY, 6),
            MonthDay.of(Month.JANUARY, 7),
            MonthDay.of(Month.JANUARY, 8),

            MonthDay.of(Month.FEBRUARY, 23),

            MonthDay.of(Month.MARCH, 8),

            MonthDay.of(Month.MAY, 1),
            MonthDay.of(Month.MAY, 2),
            MonthDay.of(Month.MAY, 3),
            MonthDay.of(Month.MAY, 4),
            MonthDay.of(Month.MAY, 8),
            MonthDay.of(Month.MAY, 9),
            MonthDay.of(Month.MAY, 10),
            MonthDay.of(Month.MAY, 11),

            MonthDay.of(Month.JUNE, 12),
            MonthDay.of(Month.JUNE, 13),
            MonthDay.of(Month.JUNE, 14),
            MonthDay.of(Month.JUNE, 15),

            MonthDay.of(Month.NOVEMBER, 2),
            MonthDay.of(Month.NOVEMBER, 3),
            MonthDay.of(Month.NOVEMBER, 4),

            MonthDay.of(Month.DECEMBER, 29),
            MonthDay.of(Month.DECEMBER, 30),
            MonthDay.of(Month.DECEMBER, 31)
    );

    public static final Integer minDays = 1;
    public static final Integer maxDays = 365;

    public BigDecimal calculate(BigDecimal salaryPerYear, int vacationDays) {
        return salaryPerYear
                .divide(months, scale, RoundingMode.HALF_UP)
                .divide(ratio, scale, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(vacationDays));
    }

    public BigDecimal calculate(BigDecimal salaryPerYear, LocalDate start, LocalDate end) {
        int paymentDays = countPaymentDays(start, end);

        return salaryPerYear
                .divide(months, scale, RoundingMode.HALF_UP)
                .divide(ratio, scale, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(paymentDays));
    }

    public int countPaymentDays(LocalDate start, LocalDate end) {
        AtomicInteger paymentDays = new AtomicInteger();
        start.datesUntil(end.plusDays(1)).forEach(date -> {
            if (checkDate(date)) {
                paymentDays.getAndIncrement();
            }
        });

        return paymentDays.intValue();
    }

    private boolean checkDate(LocalDate date) {
        return date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY &&
                !holidays.contains(MonthDay.from(date));
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
