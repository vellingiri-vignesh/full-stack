package com.divineaura.controller;

import com.divineaura.service.CustomerService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(path = "api/v1/customers")
    public List<com.divineaura.customer.Customer> getCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("api/v1/customers/{customerId}")
    public com.divineaura.customer.Customer getCustomer(@PathVariable(name = "customerId", required = true) Integer customerId){
        return customerService.getCustomerById(customerId);
    }
}
