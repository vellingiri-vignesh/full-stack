package com.divineaura.customer;

import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;

public interface CustomerDao {
    List<Customer> selectAllCustomers();

    Optional<Customer> selectCustomerById(Integer customerId);
}
