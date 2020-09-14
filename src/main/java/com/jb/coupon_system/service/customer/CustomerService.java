package com.jb.coupon_system.service.customer;

import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.entity.Customer;

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
public interface CustomerService {
    /**
     * Purchases a coupon.
     *
     * @param coupon
     * @param customer
     *
     * @return
     */
    Coupon purchaseCoupon(Coupon coupon, Customer customer);

    /**
     * Checks if a coupon has already been purchased by the customer
     *
     * @param coupon
     * @param customer
     *
     * @return
     */
    boolean isPurchased(Coupon coupon, Customer customer);

    /**
     * Gets all customer coupons.
     *
     * @param id
     *
     * @return Optional List of customers coupons.
     */
    Optional<List<Coupon>> getAllCustomerCoupons(long id);

    /**
     * Gets all coupons from data base.
     *
     * @return
     */
    List<Coupon> getAllCoupons();

    /**
     * Gets a coupon by id.
     *
     * @param id
     *
     * @return a coupon.
     */
    Optional<Coupon> getCouponById(long id);

    /**
     * get Customer by id.
     *
     * @param id
     *
     * @return a customer.
     */
    Optional<Customer> getCustomerById(long id);

    /**
     * Updates a coupon.
     *
     * @param couponAmountUpdated
     */
    void couponUpdate(Coupon couponAmountUpdated);

    /**
     * Gets all coupons that a customer don't purchased.
     *
     * @param customerId
     *
     * @return List of coupons.
     */
    Optional<List<Coupon>> getAllAvailableCouponsToPurchase(long customerId);

    /**
     * Gets coupons to purchase by category.
     *
     * @param category
     *
     * @return a List of coupons.
     */
    Optional<List<Coupon>> getCouponsToPurchaseByCategoryAndCustomerId(int category, long customerId);

    /**
     * Gets all coupons lower than a specific price.
     *
     * @param price
     * @param price,customerId
     *
     * @return a List of coupons.
     */
    Optional<List<Coupon>> getCouponsLowerThanPrice(double price, long customerId);

    /**
     * Gets all coupons end date before a another date
     *
     * @param endDate
     * @param customerId
     *
     * @return a list of coupons.
     */
    Optional<List<Coupon>> getCouponsBeforeAnEndDate(LocalDateTime endDate, long customerId);
}
