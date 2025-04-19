package com.example.neoflextesttask.service;

import org.springframework.http.ResponseEntity;
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
public class CalculatorService {
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

    private static final Integer minDays = 1;
    private static final Integer maxDays = 365;

    private BigDecimal calculate(BigDecimal salaryPerYear, int vacationDays) {
        return salaryPerYear
                .divide(months, scale, RoundingMode.HALF_UP)
                .divide(ratio, scale, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(vacationDays));
    }

    private BigDecimal calculate(BigDecimal salaryPerYear, LocalDate start, LocalDate end) {
        int paymentDays = countPaymentDays(start, end);

        return salaryPerYear
                .divide(months, scale, RoundingMode.HALF_UP)
                .divide(ratio, scale, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(paymentDays));
    }

    private int countPaymentDays(LocalDate start, LocalDate end) {
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

    public ResponseEntity<String> finalCalculation(
        BigDecimal salaryPerYear, Integer vacationDays,
        LocalDate startDate, LocalDate endDate
    ) {
        if (!isSalaryValid(salaryPerYear)) {
            return ResponseEntity.badRequest().body("Зарплата не может быть отрицательной!");
        }

        if (startDate == null && endDate == null && vacationDays != null) {

            if (!isVacationDaysValid(vacationDays)) {
                return ResponseEntity.badRequest().body("Количество дней должно быть в " +
                        "диапазоне от " + minDays + " до " + maxDays + " дней!");
            }

            String days = getDayAddition(vacationDays);

            return ResponseEntity.ok("Ваши отпускные за " + vacationDays +
                    days + " составили: " + calculate(salaryPerYear, vacationDays));

        } else if (startDate != null && endDate == null && vacationDays != null) {

            if (!isVacationDaysValid(vacationDays)) {
                return ResponseEntity.badRequest().body("Количество дней должно быть в " +
                        "диапазоне от " + minDays + " до " + maxDays + " дней!");
            }

            LocalDate endDateNew = startDate.plusDays(vacationDays - 1);
            int paymentDays = countPaymentDays(startDate, endDateNew);
            String days = getDayAddition(paymentDays);

            return ResponseEntity.ok("Ваши отпускные за " + paymentDays + days +
                    " составили: " + calculate(salaryPerYear, startDate, endDateNew));

        } else if (startDate != null && endDate != null && vacationDays == null) {

            if (!isDatesValid(startDate, endDate)) {
                return ResponseEntity.badRequest().body("Дата начала отпуска должна быть раньше конца отпуска!");
            }

            int paymentDays = countPaymentDays(startDate, endDate);
            String days = getDayAddition(paymentDays);

            return ResponseEntity.ok("Ваши отпускные за " + paymentDays + days +
                    " составили: " + calculate(salaryPerYear, startDate, endDate));

        }

        return ResponseEntity.badRequest().body("Ошибка в заполнении параметров!");
    }

    private String getDayAddition(int num) {
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

    private boolean isSalaryValid(BigDecimal salary) {
        return salary.intValue() >= 0;
    }

    private boolean isVacationDaysValid(int days) {
        return days >= minDays && days <= maxDays;
    }

    private boolean isDatesValid(LocalDate start, LocalDate end) {
        return start.isBefore(end);
    }
}
