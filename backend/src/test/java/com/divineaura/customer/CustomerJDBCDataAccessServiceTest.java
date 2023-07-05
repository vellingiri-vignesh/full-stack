package com.divineaura.customer;

import static org.assertj.core.api.Assertions.assertThat;

import com.divineaura.AbstractTestContainer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainer {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(getJdbcTemplate(), customerRowMapper);
    }

    @Test
    void selectAllCustomers() {
        //Given
        Customer customer = new Customer(
            FAKER.name().fullName(),
            FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
            "password", 22,
            Gender.FEMALE
        );
        underTest.insertCustomer(customer);

        //When
        List<Customer> customers = underTest.selectAllCustomers();

        //Then
        assertThat(customers).isNotEmpty();

    }

    @Test
    void selectCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            FAKER.name().fullName(),
            email,
            "password", 22,
            Gender.FEMALE
        );
        underTest.insertCustomer(customer);
        Integer customerId = underTest.selectAllCustomers().stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        //When
        Optional<Customer> actual = underTest.selectCustomerById(customerId);

        //Then
        assertThat(actual).isPresent()
            .hasValueSatisfying(c -> {
                assertThat(c.getId().equals(customerId));
                assertThat(c.getName().equals(customer.getName()));
                assertThat(c.getEmail().equals(customer.getEmail()));
                assertThat(c.getAge().equals(customer.getAge()));
            });
    }

    @Test
    void selectCustomerByInvalidIdThrowsError() {
        //Given
        int id = -1;

        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            FAKER.name().fullName(),
            email,
            "password", 22,
            Gender.FEMALE
        );
        underTest.insertCustomer(customer);
        Integer customerId = underTest.selectAllCustomers().stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        //When
        Optional<Customer> actual = underTest.selectCustomerById(customerId);

        //Then
        assertThat(actual).isPresent()
            .hasValueSatisfying(c -> {
                assertThat(c.getId().equals(customerId));
                assertThat(c.getName().equals(customer.getName()));
                assertThat(c.getEmail().equals(customer.getEmail()));
                assertThat(c.getAge().equals(customer.getAge()));
            });

    }

    @Test
    void existsCustomerWithEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            FAKER.name().fullName(),
            email,
            "password", 22,
            Gender.MALE
        );
        underTest.insertCustomer(customer);

        //When
        boolean actual = underTest.existsCustomerWithEmail(email);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithInvalidEmailReturnsFalse() {
        //Given
        String email = UUID.randomUUID().toString();

        //When
        boolean actual = underTest.existsCustomerWithEmail(email);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerWithId() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            FAKER.name().fullName(),
            email,
            "password", 22,
            Gender.FEMALE
        );
        underTest.insertCustomer(customer);
        Integer customerId = underTest.selectAllCustomers().stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        //When
        boolean actual = underTest.existsCustomerWithId(customerId);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithInvalidIdReturnsFalse() {
        //Given
        int id = -1;

        //When
        boolean actual = underTest.existsCustomerWithId(id);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            FAKER.name().fullName(),
            email,
            "password", 22,
            Gender.FEMALE
        );
        underTest.insertCustomer(customer);
        Integer customerId = underTest.selectAllCustomers().stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        //When
        underTest.deleteCustomerById(customerId);

        //Then
        boolean actual = underTest.existsCustomerWithId(customerId);
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerByInvalidIdThrowsError() {
        //Given
        int id = -1;

        //When
        underTest.deleteCustomerById(id);

        //Then
        boolean actual = underTest.existsCustomerWithId(id);
        assertThat(actual).isFalse();
    }

//    @Test
//    TODO: Uncomment after fixing Hikari connection pool size with testContainer
    void updateCustomerEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            FAKER.name().fullName(),
            email,
            "password", 22,
            Gender.FEMALE
        );
        underTest.insertCustomer(customer);
        Integer customerID = underTest.selectAllCustomers()
            .stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        //When
        Customer updatedCustomer = new Customer();
        String newEmail = UUID.randomUUID().toString();
        updatedCustomer.setId(customerID);
        updatedCustomer.setEmail(newEmail);
        underTest.updateCustomer(updatedCustomer);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(customerID);
        assertThat(actual).isPresent()
            .hasValueSatisfying(c -> {
                assertThat(c.getEmail().equals(newEmail));
            });


    }
    @Test
    void updateCustomerName() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String newName = "Dummy Name";
        Customer customer = new Customer(
            FAKER.name().fullName(),
            email,
            "password", 22,
            Gender.FEMALE
        );
        underTest.insertCustomer(customer);
        Integer customerID = underTest.selectAllCustomers()
            .stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        //When
        Customer updatedCustomer = new Customer();
        String newEmail = UUID.randomUUID().toString();
        updatedCustomer.setId(customerID);
        updatedCustomer.setName(newName);
        underTest.updateCustomer(updatedCustomer);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(customerID);
        assertThat(actual).isPresent()
            .hasValueSatisfying(c -> {
                assertThat(c.getName().equals(newName));
            });
    }

    @Test
    void updateCustomerAge() {
        //Given
        Integer newAge = 44;
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            FAKER.name().fullName(),
            email,
            "password", 22,
            Gender.FEMALE
        );
        underTest.insertCustomer(customer);
        Integer customerID = underTest.selectAllCustomers()
            .stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        //When
        Customer updatedCustomer = new Customer();
        String newEmail = UUID.randomUUID().toString();
        updatedCustomer.setId(customerID);
        updatedCustomer.setAge(newAge);
        underTest.updateCustomer(updatedCustomer);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(customerID);
        assertThat(actual).isPresent()
            .hasValueSatisfying(c -> {
                assertThat(c.getAge().equals(newAge));
            });
    }

    @Test
    void updateCustomerWithNoChanges() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            FAKER.name().fullName(),
            email,
            "password", 22,
            Gender.FEMALE
        );
        underTest.insertCustomer(customer);
        Integer customerID = underTest.selectAllCustomers()
            .stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();
        customer.setId(customerID);

        //When
        underTest.updateCustomer(customer);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(customerID);
        assertThat(actual).isPresent()
            .hasValueSatisfying(c -> {
                assertThat(c.getAge().equals(customer.getAge()));
                assertThat(c.getName().equals(customer.getName()));
                assertThat(c.getEmail().equals(customer.getEmail()));
            });
    }
}