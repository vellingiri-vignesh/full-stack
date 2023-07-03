package com.divineaura.service;

import com.divineaura.customer.Customer;
import com.divineaura.customer.CustomerDTO;
import com.divineaura.customer.CustomerDTOMapper;
import com.divineaura.customer.CustomerDao;
import com.divineaura.customer.CustomerRegistrationRequest;
import com.divineaura.customer.CustomerUpdateRequest;
import com.divineaura.exception.DuplicateResourceException;
import com.divineaura.exception.RequestValidationException;
import com.divineaura.exception.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service()
public class CustomerService {
    private final CustomerDao customerDao;
    private final CustomerDTOMapper customerDTOMapper;
    private final PasswordEncoder passwordEncoder;


    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao,
                           CustomerDTOMapper customerDTOMapper,
                           PasswordEncoder passwordEncoder
                           ) {
        this.customerDao = customerDao;
        this.customerDTOMapper = customerDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerDao.selectAllCustomers()
            .stream()
            .map(customerDTOMapper)
            .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(Integer customerId) {
        return customerDao.selectCustomerById(customerId)
            .map(customerDTOMapper)
            .orElseThrow(() -> new ResourceNotFoundException("Customer %d not found...".formatted(customerId)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if (customerDao.existsCustomerWithEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException("Customer with email already exists...");
        }
        customerDao.insertCustomer(new Customer(customerRegistrationRequest.name(), customerRegistrationRequest.email(),
            passwordEncoder.encode(customerRegistrationRequest.password()), customerRegistrationRequest.age(), customerRegistrationRequest.gender()));

    }

    public void deleteCustomer(Integer customerId) {
        if (customerDao.existsCustomerWithId(customerId)) {
            customerDao.deleteCustomerById(customerId);
        } else {
            throw new ResourceNotFoundException("Customer with ID: %d not found...".formatted(customerId));
        }
    }

    public void updateCustomerById(CustomerUpdateRequest customerUpdateRequest, Integer customerId) {
        if (!customerDao.existsCustomerWithId(customerId)) {
            throw new ResourceNotFoundException("Customer with ID: %d not found...".formatted(customerId));
        } else {
            Customer customer = customerDao.selectCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer %d not found...".formatted(customerId)));
            boolean hasChange = false;

            if (customerUpdateRequest.name() != null && !customerUpdateRequest.name().equals(customer.getName())) {
                customer.setName(customerUpdateRequest.name());
                hasChange = true;
            }

            if (customerUpdateRequest.email() != null && !customerUpdateRequest.email().equals(customer.getEmail())) {
                if (customerDao.existsCustomerWithEmail(customerUpdateRequest.email())) {
                    throw new DuplicateResourceException("Customer with email already exists...");
                }
                customer.setEmail(customerUpdateRequest.email());
                hasChange = true;
            }

            if (customerUpdateRequest.age() != null && !customerUpdateRequest.age().equals(customer.getAge())) {
                customer.setAge(customerUpdateRequest.age());
                hasChange = true;
            }

            if (customerUpdateRequest.gender() != null && !customerUpdateRequest.gender().equals(customer.getGender())) {
                customer.setGender(customerUpdateRequest.gender());
                hasChange = true;
            }

            if (hasChange) {
                customerDao.updateCustomer(customer);
            } else {
                throw new RequestValidationException("Nothing has changed!");
            }
        }
    }
}
