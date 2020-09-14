package com.jb.coupon_system.data.repo;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 04/08/2020
 */

import com.jb.coupon_system.data.entity.Customer;

import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Dynamic repository for generating a list of customers by first name or last name.
 */
@Repository
public class DynamicRepositoryForCustomer {
    private final EntityManager manager;

    public DynamicRepositoryForCustomer(EntityManager manager) {
        this.manager = manager;
    }

    public List findCustomerByNames(String firstName, String lastName) {

        Query firstNameQuery = manager
                .createQuery("select c from Customer c where c.firstName=:firstName", Customer.class)
                .setParameter("firstName", firstName);

        Query lastNameQuery = manager
                .createQuery("select c from Customer c where c.lastName=:lastName", Customer.class)
                .setParameter("lastName", lastName);

        if (firstName != null && lastName.contentEquals("")) {
            return firstNameQuery.getResultList();
        } else {
            return lastNameQuery.getResultList();
        }
    }
}
