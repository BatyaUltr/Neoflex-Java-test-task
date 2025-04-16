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
    void checkCalculateNoBody() {
        ResponseEntity<String> response = testRestTemplate
                .getForEntity("/api/vacations/calculate", String.class);
        assertEquals(405, response.getStatusCodeValue());
    }

    @Test
    void checkCalculateBodyF() {
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
    void checkCalculateBodyS() {
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
    void checkCalculateBodyT() {
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
    void checkCalculateNegativeSalary() {
        String requestJson =
                "{ \"salaryPerYear\": -1000000, \"vacationDays\": 2 }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = testRestTemplate
                .postForEntity("/api/vacations/calculate", request, String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Зарплата не может быть отрицательной!"));
    }

    @Test
    void checkCalculateMinDays() {
        String requestJson =
                "{ \"salaryPerYear\": 1000000, \"vacationDays\": -1 }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = testRestTemplate
                .postForEntity("/api/vacations/calculate", request, String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Количество отпускных дней не может быть меньше 1!"));
    }

    @Test
    void checkCalculateMaxDays() {
        String requestJson =
                "{ \"salaryPerYear\": 1000000, \"vacationDays\": 1000 }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = testRestTemplate
                .postForEntity("/api/vacations/calculate", request, String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains("Количество отпускных дней не может превышать 365!"));
    }
}
