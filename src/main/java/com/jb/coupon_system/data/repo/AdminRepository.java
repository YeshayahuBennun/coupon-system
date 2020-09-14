package com.jb.coupon_system.data.repo;

import com.jb.coupon_system.data.entity.Admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 02/08/2020
 */
public interface AdminRepository extends JpaRepository<Admin, Long> {
    /**
     * Gets a admin from the data base by email and password.
     *
     * @param email
     * @param password
     *
     * @return a admin by email and password.
     */
    Optional<Admin> findAdminByEmailAndPassword(String email, String password);
}
