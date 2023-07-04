package com.divineaura.journey;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.divineaura.auth.AuthenticationRequest;
import com.divineaura.auth.AuthenticationResponse;
import com.divineaura.customer.CustomerDTO;
import com.divineaura.customer.CustomerRegistrationRequest;
import com.divineaura.customer.CustomerUpdateRequest;
import com.divineaura.customer.Gender;
import com.divineaura.jwt.JWTUtil;
import com.github.javafaker.Faker;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;
    private final String LOGIN_URI = "/api/v1/auth/login";
    private final String CUSTOMER_URI = "/api/v1/customers";

    @Test
    void InvalidCredentialsThrowUnauthorizedException() {
        Faker faker = new Faker();
        String email = faker.internet().safeEmailAddress() + "-"
            + UUID.randomUUID()
            + "foobar123@gmail.com";
        String password = "password";

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);

        webTestClient.post()
            .uri(LOGIN_URI)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
            .exchange()
            .expectStatus()
            .isUnauthorized()
        ;
    }

    @Test
    void CanLogin() {
        // Create a registration Request
        Faker faker = new Faker();
        int age = 20;
        String name = faker.name().fullName();
        Gender gender = Gender.MALE;
        String email = faker.internet().safeEmailAddress() + "-"
            + UUID.randomUUID()
            + "foobar123@gmail.com";
        String password = "password";
        CustomerRegistrationRequest customerRegistrationRequest =
            new CustomerRegistrationRequest(
                name,
                email,
                password, age,
                gender
            );

        // Send a POST request to API
        webTestClient.post()
            .uri(CUSTOMER_URI)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(customerRegistrationRequest), CustomerUpdateRequest.class)
            .exchange()
            .expectStatus()
            .isOk();

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);

        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
            .uri(LOGIN_URI)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
            })
            .returnResult();
        AuthenticationResponse responseBody = result.getResponseBody();
        String jwtToken = result.getResponseHeaders()
            .get(AUTHORIZATION)
            .get(0);

        assertThat(jwtUtil.isTokenValid(jwtToken, email)).isTrue();

    }
}
