package com.example.neoflextesttask;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NeoflexTestTaskApplicationTests {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testCalculateNoBody() {
        ResponseEntity<String> response = testRestTemplate
                .getForEntity("/api/vacations/calculate", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCalculateBodyF() {
        String url = "/api/vacations/calculate?salaryPerYear=1000000&vacationDays=2";
        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Ваши отпускные за 2 дня составили: 5688.28"));
    }

    @Test
    void testCalculateBodyS() {
        String url = "/api/vacations/calculate?salaryPerYear=1680000&vacationDays=11";
        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Ваши отпускные за 11 дней составили: 52559.76"));
    }

    @Test
    void testCalculateBodyT() {
        String url = "/api/vacations/calculate?salaryPerYear=1680000&vacationDays=21";
        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Ваши отпускные за 21 день составили: 100341.36"));
    }

    @Test
    void testCalculateNegativeSalary() {
        String url = "/api/vacations/calculate?salaryPerYear=-1680000&vacationDays=11";
        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Зарплата не может быть отрицательной!"));
    }

    @Test
    void testCalculateMinMaxDays() {
        String url = "/api/vacations/calculate?salaryPerYear=1680000&vacationDays=-11";
        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Количество дней должно быть в диапазоне от 1 до 365 дней!"));
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    //Далее проверка с датами ///////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

    @Test
    void testAccurateCalculateWrongDates() {
        String url = "localhost:8080/api/vacations/calculate?" +
                "salaryPerYear=600000&startDate=2025-05-17&endDate=2025-04-28";
        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Дата начала отпуска должна быть раньше конца отпуска!"));
    }

    @Test
    void testAccurateCalculateDatesF() {
        String url = "localhost:8080/api/vacations/calculate?" +
                "salaryPerYear=600000&startDate=2025-04-17&endDate=2025-04-28";
        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Ваши отпускные за 8 дней составили: 13651.84"));
    }

    @Test
    void testAccurateCalculateDatesS() {
        String url = "localhost:8080/api/vacations/calculate?" +
                "salaryPerYear=600000&startDate=2025-01-01&endDate=2025-01-14";
        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Ваши отпускные за 4 дня составили: 19112.64"));
    }

    @Test
    void testAccurateCalculateLessParam() {
        String url = "localhost:8080/api/vacations/calculate?salaryPerYear=600000";
        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Вы неправильно заполнили параметры!"));
    }
}
