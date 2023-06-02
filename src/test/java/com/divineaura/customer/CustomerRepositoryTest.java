package com.divineaura.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.divineaura.AbstractTestContainer;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainer {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    void setUp() {
        // Because in Main we have CommandLineRunner where we insert
        // a customer for testing
        underTest.deleteAll();
        System.out.println(context.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            FAKER.name().fullName(),
            email,
            22
        );
        underTest.save(customer);
        Integer customerId = underTest.findAll().stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        //When
        boolean actual = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByInvalidEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //When
        boolean actual = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            FAKER.name().fullName(),
            email,
            22
        );
        underTest.save(customer);
        Integer customerId = underTest.findAll().stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        //When
        boolean actual = underTest.existsCustomerById(customerId);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByInvalidId() {
        //Given
        int id = -1;

        //When
        boolean actual = underTest.existsCustomerById(id);

        //Then
        assertThat(actual).isFalse();
    }
}