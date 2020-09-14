package com.jb.coupon_system.data.repo;

import com.jb.coupon_system.data.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 28/07/2020
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /**
     * Gets a customer by email and password.
     *
     * @param email
     * @param password
     *
     * @return a customer by email and password.
     */
    Optional<Customer> findCustomerByEmailAndPassword(String email, String password);

    /**
     * Filters the customer database and obtains all of them from a single letter of their first name.
     *
     * @param firstName
     *
     * @return a List of customers.
     */
    Optional<List<Customer>> findByFirstNameContains(String firstName);
}
