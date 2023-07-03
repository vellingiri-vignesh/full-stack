package com.divineaura.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.divineaura.customer.Customer;
import com.divineaura.customer.CustomerDao;
import com.divineaura.customer.CustomerRegistrationRequest;
import com.divineaura.customer.CustomerUpdateRequest;
import com.divineaura.customer.Gender;
import com.divineaura.exception.DuplicateResourceException;
import com.divineaura.exception.RequestValidationException;
import com.divineaura.exception.ResourceNotFoundException;
import com.github.javafaker.Faker;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    private Faker FAKER = new Faker();

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao, passwordEncoder);
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();

        //Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void getCustomerById() {
        //Given
        int id = 2;
        Customer customer = new Customer(id, FAKER.name().fullName(), FAKER.internet().safeEmailAddress(), "password",
            12, Gender.FEMALE);
        when(customerDao.selectCustomerById(id))
            .thenReturn(Optional.of(customer));

        //When
        Customer actual = underTest.getCustomerById(2);

        //Then
        assertThat(actual).isEqualTo(customer);
        verify(customerDao).selectCustomerById(id);
    }

    @Test
    void willThrowWhenGetCustomersReturnEmptyOptional() {
        //Given
        int id = 3;
        when(customerDao.selectCustomerById(id))
            .thenReturn(Optional.empty());
        //When

        //Then
        assertThatThrownBy(() -> underTest.getCustomerById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Customer %d not found...".formatted(id));
    }

    @Test
    void addCustomer() {
        //Given
        int id = 20;
        String email = FAKER.internet().safeEmailAddress();
        String name = FAKER.name().fullName();
        Gender gender = Gender.MALE;

        when(customerDao.existsCustomerWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
            name,
            email,
            "password", 12,
            gender);

        var passwordHash = "@#FC5562Sdd";
        when(passwordEncoder.encode(customerRegistrationRequest.password())).thenReturn(passwordHash);

        //When
        underTest.addCustomer(customerRegistrationRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName().equals(customerRegistrationRequest.name())).isTrue();
        assertThat(capturedCustomer.getEmail().equals(customerRegistrationRequest.email())).isTrue();
        assertThat(capturedCustomer.getPassword().equals(passwordHash)).isTrue();
        assertThat(capturedCustomer.getAge().equals(customerRegistrationRequest.age())).isTrue();
        assertThat(capturedCustomer.getGender().equals(customerRegistrationRequest.gender())).isTrue();
        assertThat(capturedCustomer.getId()).isNull();
    }

    @Test
    void addCustomerThrowsExceptionWhenPassingUnavailableEmail() {
        //Given
        int id = 20;
        String email = FAKER.internet().safeEmailAddress();
        String name = FAKER.name().fullName();
        Gender gender = Gender.MALE;
        when(customerDao.existsCustomerWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
            name,
            email,
            "password", 12,
            gender);

        //When
        assertThatThrownBy(() -> underTest.addCustomer(customerRegistrationRequest))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessage("Customer with email already exists...");

        //Then
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomer() {
        //Given
        int id = 3;
        when(customerDao.existsCustomerWithId(id))
            .thenReturn(true);

        //When
        underTest.deleteCustomer(id);

        //Then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void deleteCustomerWithInvalidIdThrowsException() {
        //Given
        int id = 3;
        when(customerDao.existsCustomerWithId(id))
            .thenReturn(false);

        //When
        assertThatThrownBy(() -> underTest.deleteCustomer(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Customer with ID: %d not found...".formatted(id));

        //Then
        verify(customerDao, never()).deleteCustomerById(any());

    }

    @Test
    void updateCustomerByIdWithNameChange() {
        //Given
        int id = 20;
        String email = FAKER.internet().safeEmailAddress();
        String name = FAKER.name().fullName();
        Gender gender = Gender.MALE;
        int age = 18;

        Customer customer = new Customer(id, name, email, "password", age, gender);

        when(customerDao.existsCustomerWithId(id))
            .thenReturn(true);
        when(customerDao.selectCustomerById(id))
            .thenReturn(Optional.of(customer));

        String updatedName = "New Name";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
            updatedName,
            null,
            null,
            null
        );

        //When
        underTest.updateCustomerById(customerUpdateRequest, id);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer actual = customerArgumentCaptor.getValue();

        assertThat(actual.getName().equals(updatedName)).isTrue();
        assertThat(actual.getEmail().equals(email)).isTrue();
        assertThat(actual.getAge().equals(age)).isTrue();
        assertThat(actual.getGender().equals(gender)).isTrue();
    }

    @Test
    void updateCustomerByIdWithEmailChange() {
        //Given
        int id = 20;
        String email = FAKER.internet().safeEmailAddress();
        String name = FAKER.name().fullName();
        Gender gender = Gender.MALE;
        int age = 18;
        String updatedEmail = UUID.randomUUID().toString();

        Customer customer = new Customer(id, name, email, "password", age, gender);

        when(customerDao.existsCustomerWithId(id))
            .thenReturn(true);
        when(customerDao.selectCustomerById(id))
            .thenReturn(Optional.of(customer));
        when(customerDao.existsCustomerWithEmail(updatedEmail))
            .thenReturn(false);

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
            null,
            updatedEmail,
            null,
            null
        );

        //When
        underTest.updateCustomerById(customerUpdateRequest, id);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer actual = customerArgumentCaptor.getValue();

        assertThat(actual.getName().equals(name)).isTrue();
        assertThat(actual.getEmail().equals(updatedEmail)).isTrue();
        assertThat(actual.getAge().equals(age)).isTrue();
        assertThat(actual.getGender().equals(gender)).isTrue();
    }

    @Test
    void updateCustomerByIdWithAgeChange() {
        //Given
        int id = 20;
        String email = FAKER.internet().safeEmailAddress();
        String name = FAKER.name().fullName();
        Gender gender = Gender.FEMALE;
        int age = 18;

        Customer customer = new Customer(id, name, email, "password", age, gender);

        when(customerDao.existsCustomerWithId(id))
            .thenReturn(true);
        when(customerDao.selectCustomerById(id))
            .thenReturn(Optional.of(customer));

        int updatedAge = 22;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
            null,
            null,
            updatedAge,
            null
        );

        //When
        underTest.updateCustomerById(customerUpdateRequest, id);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer actual = customerArgumentCaptor.getValue();

        assertThat(actual.getName().equals(name)).isTrue();
        assertThat(actual.getEmail().equals(email)).isTrue();
        assertThat(actual.getAge().equals(updatedAge)).isTrue();
        assertThat(actual.getGender().equals(gender)).isTrue();
    }

    @Test
    void updateCustomerByIdWithGenderChange() {
        //Given
        int id = 20;
        String email = FAKER.internet().safeEmailAddress();
        String name = FAKER.name().fullName();
        Gender gender = Gender.FEMALE;
        Gender updatedGender = Gender.MALE;
        int age = 18;

        Customer customer = new Customer(id, name, email, "password", age, gender);

        when(customerDao.existsCustomerWithId(id))
            .thenReturn(true);
        when(customerDao.selectCustomerById(id))
            .thenReturn(Optional.of(customer));

        int updatedAge = 22;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
            null,
            null,
            null,
            updatedGender
        );

        //When
        underTest.updateCustomerById(customerUpdateRequest, id);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer actual = customerArgumentCaptor.getValue();

        assertThat(actual.getName().equals(name)).isTrue();
        assertThat(actual.getEmail().equals(email)).isTrue();
        assertThat(actual.getAge().equals(age)).isTrue();
        assertThat(actual.getGender().equals(updatedGender)).isTrue();
    }

    @Test
    void updateCustomerByIdWithAllAttributeChange() {
        //Given
        int id = 20;
        String email = FAKER.internet().safeEmailAddress();
        String name = FAKER.name().fullName();
        Gender gender = Gender.FEMALE;
        int age = 18;

        int updatedAge = 22;
        String updatedName = "New Name";
        String updatedEmail = UUID.randomUUID().toString();

        Customer customer = new Customer(id, name, email, "password", age, gender);

        when(customerDao.existsCustomerWithId(id))
            .thenReturn(true);
        when(customerDao.selectCustomerById(id))
            .thenReturn(Optional.of(customer));
        when(customerDao.existsCustomerWithEmail(updatedEmail))
            .thenReturn(false);


        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
            updatedName,
            updatedEmail,
            updatedAge,
            gender
        );

        //When
        underTest.updateCustomerById(customerUpdateRequest, id);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer actual = customerArgumentCaptor.getValue();

        assertThat(actual.getName().equals(updatedName)).isTrue();
        assertThat(actual.getEmail().equals(updatedEmail)).isTrue();
        assertThat(actual.getAge().equals(updatedAge)).isTrue();
        assertThat(actual.getGender().equals(gender)).isTrue();
    }

    @Test
    void updateCustomerByIdWithExistingEmailhrowsException() {
        //Given
        int id = 20;
        String email = FAKER.internet().safeEmailAddress();
        String name = FAKER.name().fullName();
        Gender gender = Gender.FEMALE;
        int age = 18;

        String updatedEmail = UUID.randomUUID().toString();

        Customer customer = new Customer(id, name, email, "password", age,gender);

        when(customerDao.existsCustomerWithId(id))
            .thenReturn(true);
        when(customerDao.selectCustomerById(id))
            .thenReturn(Optional.of(customer));
        when(customerDao.existsCustomerWithEmail(updatedEmail))
            .thenReturn(true);

        int updatedAge = 22;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
            null,
            updatedEmail,
            null,
            null
        );

        //When
        assertThatThrownBy(() -> underTest.updateCustomerById(customerUpdateRequest, id))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessage("Customer with email already exists...");

        //Then
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void updateCustomerByIdWithNoChangeThrowsException() {
        //Given
        int id = 20;
        String email = FAKER.internet().safeEmailAddress();
        String name = FAKER.name().fullName();
        Gender gender = Gender.FEMALE;
        int age = 18;

        Customer customer = new Customer(id, name, email, "password", age,gender);

        when(customerDao.existsCustomerWithId(id))
            .thenReturn(true);
        when(customerDao.selectCustomerById(id))
            .thenReturn(Optional.of(customer));

        int updatedAge = 22;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
            null,
            null,
            null,
            null
        );

        //When
        assertThatThrownBy(() -> underTest.updateCustomerById(customerUpdateRequest, id))
            .isInstanceOf(RequestValidationException.class)
            .hasMessage("Nothing has changed!");

        //Then
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void updateCustomerByIdWithInvalidIdThrowsException() {
        //Given
        int id = 20;
        String email = FAKER.internet().safeEmailAddress();
        String name = FAKER.name().fullName();
        int age = 18;

        when(customerDao.existsCustomerWithId(id))
            .thenReturn(false);

        int updatedAge = 22;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
            null,
            null,
            null,
            null
        );

        //When
        assertThatThrownBy(() -> underTest.updateCustomerById(customerUpdateRequest, id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Customer with ID: %d not found...".formatted(id));

        //Then
        verify(customerDao, never()).updateCustomer(any());
    }
}