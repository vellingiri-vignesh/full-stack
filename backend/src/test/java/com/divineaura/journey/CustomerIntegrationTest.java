package com.divineaura.journey;

import static org.assertj.core.api.Assertions.assertThat;

import com.divineaura.customer.Customer;
import com.divineaura.customer.CustomerRegistrationRequest;
import com.divineaura.customer.CustomerUpdateRequest;
import com.divineaura.customer.Gender;
import com.github.javafaker.Faker;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private final String CUSTOMER_URI = "/api/v1/customers";

    @Test
    void CanRegisterCustomer() {
        // Create a registration Request
        Faker faker = new Faker();
        int age = 20;
        String name = faker.name().fullName();
        Gender gender = Gender.MALE;
        String email = faker.internet().safeEmailAddress() + "-"
            + UUID.randomUUID()
            + "foobar123@gmail.com";
        CustomerRegistrationRequest customerRegistrationRequest =
            new CustomerRegistrationRequest(
                name,
                email,
                age,
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

        // Get all Customers from API
        List<Customer> allCustomers = webTestClient.get()
            .uri(CUSTOMER_URI)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(new ParameterizedTypeReference<Customer>() {
            })
            .returnResult()
            .getResponseBody();

        // Make sure that Customer is present
        Customer expected = new Customer(name, email, age, gender);
        assertThat(allCustomers)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .contains(expected);

        // Get Customer By ID
        assert allCustomers != null;
        var id = allCustomers.stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();
        expected.setId(id);

        webTestClient.get()
            .uri(CUSTOMER_URI + "/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(new ParameterizedTypeReference<Customer>() {
            })
            .isEqualTo(expected);
    }

    @Test
    void canDeleteCustomer() {
        // Create a registration Request
        Faker faker = new Faker();
        int age = 20;
        Gender gender = Gender.MALE;
        String name = faker.name().fullName();
        String email = faker.internet().safeEmailAddress() + "-"
            + UUID.randomUUID()
            + "foobar123@gmail.com";
        CustomerRegistrationRequest customerRegistrationRequest =
            new CustomerRegistrationRequest(
                name,
                email,
                age,
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

        // Get all Customers from API
        List<Customer> allCustomers = webTestClient.get()
            .uri(CUSTOMER_URI)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(new ParameterizedTypeReference<Customer>() {
            })
            .returnResult()
            .getResponseBody();



        // Delete Customer By ID
        assert allCustomers != null;
        var id = allCustomers.stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        webTestClient.delete()
            .uri(CUSTOMER_URI + "/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk();

        // Get Customer By ID throw an error
        webTestClient.get()
            .uri(CUSTOMER_URI + "/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void canUpdaeCustomer() {
        // Create a registration Request
        Faker faker = new Faker();
        int age = 20;
        String name = faker.name().fullName();
        Gender gender = Gender.MALE;
        String email = faker.internet().safeEmailAddress() + "-"
            + UUID.randomUUID()
            + "foobar123@gmail.com";

        CustomerRegistrationRequest customerRegistrationRequest =
            new CustomerRegistrationRequest(
                name,
                email,
                age,
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

        // Get all Customers from API
        List<Customer> allCustomers = webTestClient.get()
            .uri(CUSTOMER_URI)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(new ParameterizedTypeReference<Customer>() {
            })
            .returnResult()
            .getResponseBody();

        // Update Customer By ID
        assert allCustomers != null;
        var id = allCustomers.stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        var updatedEmail = UUID.randomUUID() + "@fooUpdated.com";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(null, updatedEmail, null, null);

        webTestClient.put()
            .uri(CUSTOMER_URI + "/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(customerUpdateRequest), CustomerUpdateRequest.class)
            .exchange()
            .expectStatus()
            .isOk();

        // Get Customer By ID
        var expected = new Customer(id, name, updatedEmail, age, gender);

        Customer actual = webTestClient.get()
            .uri(CUSTOMER_URI + "/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(new ParameterizedTypeReference<Customer>() {
            })
            .returnResult()
            .getResponseBody();
        assertThat(actual).isEqualTo(expected);

    }
}
