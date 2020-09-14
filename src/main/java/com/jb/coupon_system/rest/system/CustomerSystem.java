package com.jb.coupon_system.rest.system;

import com.jb.coupon_system.data.entity.Customer;
import com.jb.coupon_system.data.repo.CustomerRepository;
import com.jb.coupon_system.rest.ClientSession;
import com.jb.coupon_system.rest.ex.InvalidLoginException;

import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 02/08/2020
 */
@Service
public class CustomerSystem {
    private CustomerRepository customerRepo;

    public CustomerSystem(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    public ClientSession createSession(String email, String password) throws InvalidLoginException {
        Optional<Customer> optCustomer = customerRepo.findCustomerByEmailAndPassword(email, password);

        if (optCustomer.isPresent()) {
            return ClientSession.create(optCustomer.get().getId());
        }
        throw new InvalidLoginException("Unable to login with provider credentials");
    }
}
