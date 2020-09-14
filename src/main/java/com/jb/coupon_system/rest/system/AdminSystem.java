package com.jb.coupon_system.rest.system;

import com.jb.coupon_system.data.entity.Admin;
import com.jb.coupon_system.data.repo.AdminRepository;
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
public class AdminSystem {
    private AdminRepository adminRepo;

    public AdminSystem(AdminRepository adminRepo) {
        this.adminRepo = adminRepo;
    }

    public ClientSession createSession(String email, String password) throws InvalidLoginException {
        Optional<Admin> optAdmin = adminRepo.findAdminByEmailAndPassword(email, password);

        if (optAdmin.isPresent()) {
            return ClientSession.create(optAdmin.get().getId());
        }
        throw new InvalidLoginException("Unable to login with provider credentials");
    }
}
