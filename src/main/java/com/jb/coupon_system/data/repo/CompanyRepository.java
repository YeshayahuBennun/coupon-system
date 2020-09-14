package com.jb.coupon_system.data.repo;

import com.jb.coupon_system.data.entity.Company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 28/07/2020
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    /**
     * Gets a company from the data base by email and password.
     *
     * @param email
     * @param password
     *
     * @return a company from the data base by email and password.
     */
    Optional<Company> findCompanyByEmailAndPassword(String email, String password);
}
