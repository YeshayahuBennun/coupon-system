package com.jb.coupon_system.rest.system;

import com.jb.coupon_system.data.entity.Company;
import com.jb.coupon_system.data.repo.CompanyRepository;
import com.jb.coupon_system.rest.ClientSession;
import com.jb.coupon_system.rest.ex.InvalidLoginException;

import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 29/07/2020
 */
@Service
public class CompanySystem {
    private CompanyRepository companyRepository;

    public CompanySystem(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public ClientSession createSession(String email, String password) throws InvalidLoginException {
        Optional<Company> optCompany = companyRepository.findCompanyByEmailAndPassword(email, password);

        if (optCompany.isPresent()) {
            return ClientSession.create(optCompany.get().getId());
        }
        throw new InvalidLoginException("Unable to login with provider credentials");
    }
}
