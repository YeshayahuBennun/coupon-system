package com.jb.coupon_system.rest.controller;

import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.entity.Customer;
import com.jb.coupon_system.rest.ClientSession;
import com.jb.coupon_system.rest.ex.CategoryNotFound;
import com.jb.coupon_system.rest.ex.CouponNotFound;
import com.jb.coupon_system.rest.ex.CouponSoldOut;
import com.jb.coupon_system.rest.ex.CustomerNotFound;
import com.jb.coupon_system.rest.ex.EndDateNotFound;
import com.jb.coupon_system.rest.ex.NotFound;
import com.jb.coupon_system.rest.ex.PriceNotFound;
import com.jb.coupon_system.rest.ex.PurchasedCoupon;
import com.jb.coupon_system.rest.ex.TimeSessionExpiredException;
import com.jb.coupon_system.service.customer.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 02/08/2020
 */
@RestController
@RequestMapping("/api")
public class CustomerController {
    private final CustomerService service;
    private final Map<String, ClientSession> tokensMap;

    @Autowired
    public CustomerController(CustomerService service,
            @Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.service = service;
        this.tokensMap = tokensMap;
    }

    @PostMapping("/customers/coupons/purchase/{couponId}")
    public ResponseEntity<Coupon> purchaseCoupon(@RequestParam String token, @PathVariable long couponId)
            throws TimeSessionExpiredException, PurchasedCoupon, CouponSoldOut, CouponNotFound,
                   CustomerNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("please login again");
        }

        session.access();

        Optional<Coupon> optCoupon = service.getCouponById(couponId);

        Optional<Customer> optCustomer = service.getCustomerById(session.getClientId());

        if (optCoupon.isEmpty()) {
            throw new CouponNotFound("Error, coupon not found.");
        }

        if (optCustomer.isEmpty()) {
            throw new CustomerNotFound("Error, customer not found.");
        }

        if (!service.isPurchased(optCoupon.get(), optCustomer.get())) {
            if (optCoupon.get().getAmount() <= 0) {
                throw new CouponSoldOut("Sorry, this coupon is sold out.");

            }

            Coupon purchaseCoupon = service.purchaseCoupon(optCoupon.get(), optCustomer.get());

            return ResponseEntity.ok(purchaseCoupon);
        }
        throw new PurchasedCoupon("You already own this product.");
    }

    @GetMapping("/customers/coupons/get-purchased")
    public ResponseEntity<List<Coupon>> getPurchasedCoupons(@RequestParam String token)
            throws TimeSessionExpiredException, NotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<List<Coupon>> optCoupons = service.getAllCustomerCoupons(session.getClientId());

        if (optCoupons != null && optCoupons.isPresent()) {
            return ResponseEntity.ok(optCoupons.get());
        }
        throw new NotFound("The request could not be found.");
    }

    @GetMapping("/customers/coupons/get-all")
    public ResponseEntity<List<Coupon>> getCouponsForPurchase(@RequestParam String token)
            throws TimeSessionExpiredException, NotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<List<Coupon>> optCoupons = service
                .getAllAvailableCouponsToPurchase(session.getClientId());

        if (optCoupons != null && optCoupons.isPresent()) {
            return ResponseEntity.ok(optCoupons.get());
        }
        throw new NotFound("The request could not be found.");
    }

    @GetMapping("/customers/coupons/get-category/{category}")
    public ResponseEntity<List<Coupon>> getForPurchaseByCategoryAndCustomerId(@RequestParam String token,
            @PathVariable int category)
            throws TimeSessionExpiredException, CategoryNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<List<Coupon>> optCoupons = service
                .getCouponsToPurchaseByCategoryAndCustomerId(category, session.getClientId());

        if (optCoupons != null && optCoupons.isPresent()) {
            return ResponseEntity.ok(optCoupons.get());
        }
        throw new CategoryNotFound("Error, please insert an existent category.");
    }

    @GetMapping("/customers/coupons/get-lower")
    public ResponseEntity<List<Coupon>> getCouponsLowerThanPriceForPurchase(@RequestParam String token,
            @RequestParam double price) throws TimeSessionExpiredException, PriceNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<List<Coupon>> optCoupons = service.getCouponsLowerThanPrice(price, session.getClientId());

        if (optCoupons != null && optCoupons.isPresent()) {
            return ResponseEntity.ok(optCoupons.get());
        }
        throw new PriceNotFound("Error, there are no coupons below the price entered");
    }

    @GetMapping("/customers/coupons/get-end-date")
    public ResponseEntity<List<Coupon>> getCouponsBeforeAnEndDate(@RequestParam String token,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("endDate") LocalDateTime endDate
    ) throws TimeSessionExpiredException, EndDateNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<List<Coupon>> optCoupons = service.getCouponsBeforeAnEndDate(endDate, session.getClientId());

        if (optCoupons != null && optCoupons.isPresent()) {
            return ResponseEntity.ok(optCoupons.get());
        }
        throw new EndDateNotFound("Error, there are no coupons before the end date entered.");
    }
}
