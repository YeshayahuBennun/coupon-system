package com.jb.coupon_system.service.company;

import com.jb.coupon_system.data.entity.Company;
import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.entity.Customer;
import com.jb.coupon_system.data.entity.CustomerInfo;
import com.jb.coupon_system.data.repo.CompanyRepository;
import com.jb.coupon_system.data.repo.CouponRepository;
import com.jb.coupon_system.data.repo.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 28/07/2020
 */
@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepo;
    private final CouponRepository couponRepo;
    private final CustomerRepository customerRepo;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepo, CouponRepository couponRepo,
            CustomerRepository customerRepo) {
        this.companyRepo = companyRepo;
        this.couponRepo = couponRepo;
        this.customerRepo = customerRepo;
    }

    public Optional<Company> getCompany(long id) {
        return companyRepo.findById(id);
    }

    public Coupon createCoupon(Coupon coupon, Company company) {

        coupon.setId(0);

        coupon.setCompany(company);

        couponRepo.save(coupon);

        return coupon;
    }

    @Override
    public Optional<Coupon> getCouponById(long id) {
        return couponRepo.findById(id);
    }

    @Override
    public Coupon updateCoupon(Coupon coupon, Coupon couponNewData) {

        Coupon updatedCoupon = toSetCoupon(coupon, couponNewData);

        couponRepo.save(updatedCoupon);

        return updatedCoupon;
    }

    private Coupon toSetCoupon(Coupon coupon, Coupon couponNewData) {

        coupon.setCategory(couponNewData.getCategory());
        coupon.setTitle(couponNewData.getTitle());
        coupon.setStartDate(couponNewData.getStartDate());
        coupon.setEndDate(couponNewData.getEndDate());
        coupon.setAmount(couponNewData.getAmount());
        coupon.setDescription(couponNewData.getDescription());
        coupon.setPrice(couponNewData.getPrice());
        coupon.setImageUrl(couponNewData.getImageUrl());

        return coupon;
    }

    @Override
    public Optional<List<Coupon>> getAllCouponsByCompanyId(long id) {
        return couponRepo.findAllCouponsByCompanyId(id);
    }

    @Override
    public void removeCoupon(long id) {

        couponRepo.deleteById(id);
    }

    @Override
    public List<CustomerInfo> getCustomersInformation(long companyId) {

        List<Customer> allCustomers = customerRepo.findAll();

        List<CustomerInfo> customersInfo = new ArrayList<>();

        for (Customer customer : allCustomers) {
            Optional<List<Coupon>> optCoupons = couponRepo.findByCustomersId(customer.getId());

            if (optCoupons != null && optCoupons.isPresent()) {
                for (Coupon coupon : optCoupons.get()) {

                    if (coupon.getCompany().getId() == companyId) {
                        CustomerInfo info = new CustomerInfo();

                        info.setFirstName(customer.getFirstName());

                        info.setLastName(customer.getLastName());

                        info.setEmail(customer.getEmail());

                        if (!infoIsPresent(customersInfo, info)) {
                            customersInfo.add(info);
                        }
                    }
                }
            }
        }

        /*Comparator sorts the customer list alphabetically*/

        Comparator name = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                CustomerInfo info1 = (CustomerInfo) o1;
                CustomerInfo info2 = (CustomerInfo) o2;
                int firstComp = info1.getFirstName().compareTo(info2.getFirstName());
                return (firstComp != 0 ? firstComp : info1.getLastName().compareTo(info2.getLastName()));
            }
        };

        customersInfo.sort(name);

        return customersInfo;
    }

    /*Auxiliary and verification methods*/

    @Override
    public boolean titlesMatches(String title, String newTitle) {
        return title.contentEquals(newTitle);
    }

    @Override
    public boolean validDates(Coupon couponNewData) {
        return couponNewData.getStartDate().isAfter(LocalDateTime.now()) && couponNewData.getEndDate()
                                                                                         .isAfter(LocalDateTime.now());
    }

    @Override
    public boolean validPrice(Coupon couponNewData) {
        return couponNewData.getPrice() >= 0;
    }

    @Override
    public boolean couponTitleIsPresent(List<Coupon> coupons, Coupon coupon) {

        int counter = 0;

        for (Coupon c : coupons) {
            if (c.getTitle().contentEquals(coupon.getTitle())) {
                ++counter;
            }
        }
        return counter != 0;
    }

    @Override
    public boolean thereAreEmptyFields(Coupon coupon) {

        int checker = 0;

        if (coupon.getTitle().contentEquals("") | coupon.getDescription().contentEquals("")) {
            ++checker;
        }
        return checker != 0;
    }

    private boolean infoIsPresent(List<CustomerInfo> customersInfo, CustomerInfo info) {

        int counter = 0;

        for (CustomerInfo cf : customersInfo) {
            if (cf.equals(info)) {
                ++counter;
            }
        }
        return counter != 0;
    }
}
