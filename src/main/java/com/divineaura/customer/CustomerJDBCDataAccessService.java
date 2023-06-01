package com.divineaura.customer;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
            SELECT id, name, email, age FROM customer
            """;
        List<Customer> customers = jdbcTemplate.query(sql, customerRowMapper);
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        var sql = """
             SELECT id, name, email, age FROM customer
             WHERE id = ? 
            """;
        return jdbcTemplate.query(sql, customerRowMapper, customerId)
            .stream()
            .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
            INSERT INTO customer(name, email, age)
             VALUES(?,?,?)
            """;
        int update = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
        System.out.println("jdbc.insert result : " + update);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql = """
            SELECT count(id) FROM customer 
            WHERE email = ?
            """;
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {email}, Integer.class);
        return count != null && count >0;
    }

    @Override
    public boolean existsCustomerWithId(Integer id) {
        var sql = """
            SELECT count(id) FROM customer 
            WHERE id = ?
            """;
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);
        return count != null && count >0;
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        var sql = """
            DELETE FROM customer WHERE id = ?
            """;
        int update = jdbcTemplate.update(sql, customerId);
        System.out.println("jdbc.delete result : " + update);
    }

    @Override
    public void updateCustomer(Customer customer) {
        var sql = """
            UPDATE customer SET
            name = ?, email = ? , age = ?
            WHERE ID = ?
            """;
        int update = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge(), customer.getId());
        System.out.println("jdbc.update result : " + update);
    }
}
