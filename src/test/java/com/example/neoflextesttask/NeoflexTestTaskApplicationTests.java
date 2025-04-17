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
        assertEquals(405, response.getStatusCodeValue());
    }

    @Test
    void testCalculateBodyF() {
        String requestJson =
                "{ \"salaryPerYear\": 1000000, \"vacationDays\": 2 }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = testRestTemplate
                .postForEntity("/api/vacations/calculate", request, String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Ваши отпускные за 2 дня составили: 5688.28"));
    }

    @Test
    void testCalculateBodyS() {
        String requestJson =
                "{ \"salaryPerYear\": 1680000, \"vacationDays\": 11 }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = testRestTemplate
                .postForEntity("/api/vacations/calculate", request, String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Ваши отпускные за 11 дней составили: 52559.76"));
    }

    @Test
    void testCalculateBodyT() {
        String requestJson =
                "{ \"salaryPerYear\": 1680000, \"vacationDays\": 21 }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = testRestTemplate
                .postForEntity("/api/vacations/calculate", request, String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Ваши отпускные за 21 день составили: 100341.36"));
    }

    @Test
    void testCalculateNegativeSalary() {
        String requestJson =
                "{ \"salaryPerYear\": -1000000, \"vacationDays\": 2 }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = testRestTemplate
                .postForEntity("/api/vacations/calculate", request, String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Зарплата не может быть отрицательной!"));
    }

    @Test
    void testCalculateMinMaxDays() {
        String requestJson =
                "{ \"salaryPerYear\": 1000000, \"vacationDays\": -1 }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = testRestTemplate
                .postForEntity("/api/vacations/calculate", request, String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Количество дней должно быть в диапазоне от 1 до 365 дней!"));
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    //Далее проверка с датами ///////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

    @Test
    void testAccurateCalculateWrongDates() {
        String requestJson =
                "{ \"salaryPerYear\": 1680000, \"startDate\": \"03.03.2025\", " +
                        "\"endDate\": \"21.02.2025\" }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = testRestTemplate
                .postForEntity("/api/vacations/calculate_accurate", request, String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Дата начала отпуска должна быть раньше конца отпуска!"));
    }
}
