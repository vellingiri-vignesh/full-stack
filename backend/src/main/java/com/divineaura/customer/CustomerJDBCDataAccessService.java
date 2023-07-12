package com.divineaura.customer;

import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
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
            SELECT id, name, email, password, age, gender, profile_image_id FROM customer
            """;
        List<Customer> customers = jdbcTemplate.query(sql, customerRowMapper);
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        var sql = """
             SELECT id, name, email, password, age, gender, profile_image_id FROM customer
             WHERE id = ? 
            """;
        return jdbcTemplate.query(sql, customerRowMapper, customerId)
            .stream()
            .peek((c) -> System.out.println(c))
            .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
            INSERT INTO customer(name, email, password, age, gender, profile_image_id)
             VALUES(?,?,?,?,?,?)
            """;
        int update =
            jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getPassword(), customer.getAge(),
                customer.getGender().toString(), customer.getProfileImageId());
        System.out.println("jdbc.insert result : " + update);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql = """
            SELECT count(id) FROM customer 
            WHERE email = ?
            """;
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {email}, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean existsCustomerWithId(Integer id) {
        var sql = """
            SELECT count(id) FROM customer 
            WHERE id = ?
            """;
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);
        return count != null && count > 0;
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
        if (!StringUtils.isBlank(customer.getName())) {
            var sql = """
                UPDATE customer SET
                name = ?
                WHERE ID = ?
                """;
            int update =
                jdbcTemplate.update(sql, customer.getName(), customer.getId());
            System.out.println("jdbc.update name result : " + update);
        }
        if (!StringUtils.isBlank(customer.getEmail())) {
            var sql = """
                UPDATE customer SET
                email = ? 
                WHERE ID = ?
                """;
            int update =
                jdbcTemplate.update(sql, customer.getEmail(), customer.getId());
            System.out.println("jdbc.update email result : " + update);
        }
        if (customer.getAge() != null && customer.getAge() != 0) {
            var sql = """
                UPDATE customer SET
                age = ?
                WHERE ID = ?
                """;
            int update =
                jdbcTemplate.update(sql, customer.getAge(), customer.getId());
            System.out.println("jdbc.update age result : " + update);
        }
        if (customer.getGender() != null) {
            var sql = """
                UPDATE customer SET
                gender = ?
                WHERE ID = ?
                """;
            int update =
                jdbcTemplate.update(sql, customer.getGender().name(), customer.getId());
            System.out.println("jdbc.update gender result : " + update);
        }

    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        var sql = """
             SELECT id, name, email, password, age, gender, image_url FROM customer
             WHERE email = ? 
            """;
        return jdbcTemplate.query(sql, customerRowMapper, email)
            .stream()
            .findFirst();
    }

    @Override
    public void updateCustomerProfileImageId(Integer customerId, String profileImageId) {
        var sql = """
            UPDATE customer SET
            profile_image_id = ?
            WHERE ID = ?
            """;
        int update =
            jdbcTemplate.update(sql, profileImageId, customerId);
        System.out.println("jdbc.update image_url result : " + update);
    }
}
