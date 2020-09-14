package com.jb.coupon_system.service;

import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.repo.CouponRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 05/08/2020
 */

/**
 * This class includes the checkCoupons function for excluding expired coupons.
 */
@Service
public class CouponValidityChecker {
    private final CouponRepository couponRepo;

    @Autowired
    public CouponValidityChecker(CouponRepository couponRepo) {
        this.couponRepo = couponRepo;
    }

    /**
     * Clears expired coupons from database periodically.
     */
    @Scheduled(fixedRateString = "${rate.checkcoupons}")
    private void checkCoupons() {
        List<Coupon> coupons = couponRepo.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Coupon c : coupons) {
            if (now.isAfter(c.getEndDate())) {
                couponRepo.deleteById(c.getId());
            }
        }
    }
}
