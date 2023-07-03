package com.divineaura.controller;

import com.divineaura.customer.CustomerDTO;
import com.divineaura.customer.CustomerRegistrationRequest;
import com.divineaura.customer.CustomerUpdateRequest;
import com.divineaura.jwt.JWTUtil;
import com.divineaura.service.CustomerService;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final JWTUtil jwtUtil;

    public CustomerController(CustomerService customerService, JWTUtil jwtUtil) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<CustomerDTO> getCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    public CustomerDTO getCustomer(@PathVariable(name = "customerId", required = true) Integer customerId){
        return customerService.getCustomerById(customerId);
    }

    @PostMapping
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        customerService.addCustomer(customerRegistrationRequest);
        String jwtToken = jwtUtil.issueToken(customerRegistrationRequest.email(), "ROLE_USER");
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, jwtToken)
            .build();
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable(name = "customerId", required = true) Integer customerId) {
        customerService.deleteCustomer(customerId);
    }

    @PutMapping("/{customerId}")
    public void updateCustomer(
        @PathVariable(name = "customerId", required = true) Integer customerId,
        @RequestBody CustomerUpdateRequest customerUpdateRequest
    ) {
        customerService.updateCustomerById(customerUpdateRequest, customerId);
    }
}
