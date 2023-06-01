package com.divineaura.service;

import com.divineaura.customer.Customer;
import com.divineaura.customer.CustomerDao;
import com.divineaura.customer.CustomerRegistrationRequest;
import com.divineaura.exception.DuplicateResourceException;
import com.divineaura.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service()
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Integer customerId) {
        return customerDao.selectCustomerById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer %d not found...".formatted(customerId)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if (customerDao.existsCustomerWithEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException("Customer with email already exists...");
        }
        customerDao.insertCustomer(new Customer(customerRegistrationRequest.name(), customerRegistrationRequest.email(),
            customerRegistrationRequest.age()));

    }

    public void deleteCustomer(Integer customerId) {
        if (customerDao.existsCustomerWithId(customerId)) {
            customerDao.deleteCustomerById(customerId);
        } else {
            throw new ResourceNotFoundException("Customer with ID: %d not found...".formatted(customerId));
        }
    }
}
