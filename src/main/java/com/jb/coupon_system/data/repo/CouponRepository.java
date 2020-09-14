package com.jb.coupon_system.data.repo;

import com.jb.coupon_system.data.entity.Coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    /**
     * Gets all company coupons by a company id.
     *
     * @param id
     *
     * @return a List of coupons by company id.
     */
    Optional<List<Coupon>> findAllCouponsByCompanyId(long id);

    /**
     * Gets all coupons of a customer.
     *
     * @param customerId
     *
     * @return List customer coupons.
     */
    Optional<List<Coupon>> findByCustomersId(long customerId);

    /**
     * Gets all coupons that a customer haven't bought yet.
     *
     * @param customerId
     *
     * @return
     */
    @Query("select b from Coupon b where b not in (select c from Coupon c inner join c.customers d where d" +
            ".id=:customerId)")
    Optional<List<Coupon>> findCouponsToPurchaseByCustomersId(long customerId);

    /**
     * Gets all coupons that a customer haven't bought yet by category.
     *
     * @param category
     * @param customerId
     *
     * @return a list of coupons.
     */
    @Query("select b from Coupon b where b.category=:category and b not in (select c from Coupon c inner join c" +
            ".customers d where d.id=:customerId)")
    Optional<List<Coupon>> findCouponsToPurchaseByCategoryAndCustomersId(int category, long customerId);

    /**
     * Gets all coupons lower than a specific price.
     *
     * @param price
     * @param customerId
     *
     * @return
     */
    @Query("select b from Coupon b where b.price<:price and b not in(select c from Coupon c inner join c.customers d " +
            "where d.id=:customerId)")
    Optional<List<Coupon>> findCouponsLowerThanPrice(double price, long customerId);

    /**
     * Gets all coupons end date before a another date.
     *
     * @param endDate
     * @param customerId
     *
     * @return a List of coupons.
     */
    @Query("select b from Coupon b where b.endDate<:endDate and b not in(select c from Coupon c inner join c" +
            ".customers d where d.id=:customerId)")
    Optional<List<Coupon>> findCouponsBeforeAnEndDate(LocalDateTime endDate, long customerId);
}
