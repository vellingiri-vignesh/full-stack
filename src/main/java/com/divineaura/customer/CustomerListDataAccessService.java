package com.divineaura.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    private static final List<Customer> customers = new ArrayList<>();

     {
        Customer anand = new Customer(1, "Anand" , "anand@hotmail.com", 18);
        customers.add(anand);
        Customer saki = new Customer(2, "Saki" , "saki@hotmail.com", 16);
        customers.add(saki);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        return customers.stream()
            .filter(e -> e.getId().equals(customerId))
            .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return customers.stream()
            .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existsCustomerWithId(Integer id) {
        return customers.stream()
            .anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        Optional<Customer> customer = selectCustomerById(customerId);
        customers.remove(customer);
    }
}
