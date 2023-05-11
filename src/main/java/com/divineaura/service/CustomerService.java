package com.divineaura.service;

import com.divineaura.customer.Customer;
import com.divineaura.customer.CustomerDao;
import com.divineaura.exception.ResourceNotFound;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service()
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Integer customerId){
        return customerDao.selectCustomerById(customerId)
            .orElseThrow(() -> new ResourceNotFound("Customer %d not found...".formatted(customerId)));
    }
}
