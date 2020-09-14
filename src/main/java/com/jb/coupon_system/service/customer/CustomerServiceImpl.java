package com.jb.coupon_system.service.customer;

import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.entity.Customer;
import com.jb.coupon_system.data.repo.CouponRepository;
import com.jb.coupon_system.data.repo.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
public class CustomerServiceImpl implements CustomerService {
    private final CouponRepository couponRepo;
    private final CustomerRepository customerRepo;

    @Autowired
    public CustomerServiceImpl(CouponRepository couponRepo,
            CustomerRepository customerRepo, CustomerRepository customerRepo1) {
        this.couponRepo = couponRepo;
        this.customerRepo = customerRepo1;
    }

    @Override
    public Coupon purchaseCoupon(Coupon coupon, Customer customer) {

        customer.getCoupons().add(coupon);

        Coupon purchasedCoupon = decrementAmount(coupon);

        couponRepo.save(purchasedCoupon);

        return purchasedCoupon;
    }

    private Coupon decrementAmount(Coupon coupon) {

        coupon.setAmount(coupon.getAmount() - 1);

        return coupon;
    }

    @Override
    public boolean isPurchased(Coupon coupon, Customer customer) {

        List<Coupon> coupons = customer.getCoupons();

        int status = 0;

        for (Coupon c : coupons) {
            if (c.getId() == coupon.getId()) {
                ++status;
            }
        }
        return status != 0;
    }

    @Override
    public Optional<List<Coupon>> getAllCustomerCoupons(long id) {
        return couponRepo.findByCustomersId(id);
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepo.findAll();
    }

    @Override
    public Optional<Coupon> getCouponById(long id) {
        return couponRepo.findById(id);
    }

    @Override
    public Optional<Customer> getCustomerById(long id) {
        return customerRepo.findById(id);
    }

    @Override
    public void couponUpdate(Coupon couponAmountUpdated) {

        couponRepo.save(couponAmountUpdated);
    }

    @Override
    public Optional<List<Coupon>> getAllAvailableCouponsToPurchase(long customerId) {
        return couponRepo.findCouponsToPurchaseByCustomersId(customerId);
    }

    @Override
    public Optional<List<Coupon>> getCouponsToPurchaseByCategoryAndCustomerId(int category, long customerId) {
        return couponRepo.findCouponsToPurchaseByCategoryAndCustomersId(category, customerId);
    }

    @Override
    public Optional<List<Coupon>> getCouponsLowerThanPrice(double price, long customerId) {
        return couponRepo.findCouponsLowerThanPrice(price, customerId);
    }

    @Override
    public Optional<List<Coupon>> getCouponsBeforeAnEndDate(LocalDateTime endDate, long customerId) {
        return couponRepo.findCouponsBeforeAnEndDate(endDate, customerId);
    }
}
