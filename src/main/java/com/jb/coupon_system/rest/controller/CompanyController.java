package com.jb.coupon_system.rest.controller;

import com.jb.coupon_system.data.entity.Company;
import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.entity.CustomerInfo;
import com.jb.coupon_system.rest.ClientSession;
import com.jb.coupon_system.rest.ex.CompanyNotFound;
import com.jb.coupon_system.rest.ex.CouponNotFound;
import com.jb.coupon_system.rest.ex.EmptyFields;
import com.jb.coupon_system.rest.ex.NoContent;
import com.jb.coupon_system.rest.ex.NotFound;
import com.jb.coupon_system.rest.ex.TimeSessionExpiredException;
import com.jb.coupon_system.rest.ex.TitleUnavailable;
import com.jb.coupon_system.rest.ex.UnviableDates;
import com.jb.coupon_system.rest.ex.UnviablePrice;
import com.jb.coupon_system.rest.ex.UnviableTitleUpdate;
import com.jb.coupon_system.service.company.CompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 29/07/2020
 */
@RestController
@RequestMapping("/api")
public class CompanyController {
    private final CompanyService service;
    private final Map<String, ClientSession> tokensMap;

    @Autowired
    public CompanyController(CompanyService service,
            @Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.service = service;
        this.tokensMap = tokensMap;
    }

    @PostMapping("/companies/add-coupons")
    public ResponseEntity<Coupon> createCoupon(@RequestParam String token,
            @RequestBody Coupon coupon)
            throws TimeSessionExpiredException, NoContent, UnviableDates, UnviablePrice, CompanyNotFound,
                   TitleUnavailable, EmptyFields {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        if (service.thereAreEmptyFields(coupon)) {
            throw new EmptyFields("Please, all fields must be filled");
        }

        if (!service.validDates(coupon)) {
            throw new UnviableDates("Please insert a valid Start and End dates");
        }

        if (!service.validPrice(coupon)) {
            throw new UnviablePrice("Please insert a price greater or equal than 0");
        }

        Optional<Company> optCompany = service.getCompany(session.getClientId());

        if (optCompany.isEmpty()) {
            throw new CompanyNotFound("Error, company not found");
        }

        Optional<List<Coupon>> optCoupons = service.getAllCouponsByCompanyId(session.getClientId());

        if (optCoupons != null && optCoupons.isPresent() && service.couponTitleIsPresent(optCoupons.get(), coupon)) {
            throw new TitleUnavailable(" Sorry,coupon title already exists for this company.");
        }

        Coupon newCoupon = service.createCoupon(coupon, optCompany.get());

        Optional<Coupon> optCoupon = service.getCouponById(newCoupon.getId());

        if (optCoupon != null && optCoupon.isPresent()) {
            return ResponseEntity.ok(optCoupon.get());
        }
        throw new NoContent("Sorry, the operation cannot be performed");
    }

    @GetMapping("/companies/coupons/get/{couponId}")
    public ResponseEntity<Coupon> getCouponByCouponId(@RequestParam String token, @PathVariable long couponId)
            throws TimeSessionExpiredException, CouponNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<Coupon> optCoupon = service.getCouponById(couponId);

        if (optCoupon != null && optCoupon.isPresent() && optCoupon.get().getCompany().getId() == session
                .getClientId()) {
            return ResponseEntity.ok(optCoupon.get());
        }
        throw new CouponNotFound("Error,coupon not found.");
    }

    @GetMapping("/companies/coupons")
    public ResponseEntity<List<Coupon>> getAllCouponsByCompanyId(@RequestParam String token) throws
                                                                                             TimeSessionExpiredException,
                                                                                             NotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        Optional<List<Coupon>> optCoupons = service.getAllCouponsByCompanyId(session.getClientId());

        if (optCoupons != null && optCoupons.isPresent()) {
            return ResponseEntity.ok(optCoupons.get());
        }
        throw new NotFound("The request could not be found.");
    }

    @PutMapping("/companies/update-coupons")
    public ResponseEntity<Coupon> updateCoupon(@RequestParam String token, @RequestBody Coupon couponNewData)
            throws TimeSessionExpiredException, CouponNotFound, UnviableTitleUpdate, UnviableDates, UnviablePrice,
                   EmptyFields {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        if (service.thereAreEmptyFields(couponNewData)) {
            throw new EmptyFields("Please, all fields must be filled");
        }

        Optional<Coupon> optCoupon = service.getCouponById(couponNewData.getId());

        if (optCoupon != null && optCoupon.isPresent() && session.getClientId() == optCoupon.get().getCompany()
                                                                                            .getId()) {
            if (!service.titlesMatches(optCoupon.get().getTitle(), couponNewData.getTitle())) {
                throw new UnviableTitleUpdate("Sorry,title can't be updated.");
            }

            if (!service.validDates(couponNewData)) {
                throw new UnviableDates("Please insert a valid Start and End dates");
            }

            if (!service.validPrice(couponNewData)) {
                throw new UnviablePrice("Please insert a price greater or equal than 0");
            }

            Coupon updateCoupon = service.updateCoupon(optCoupon.get(), couponNewData);

            return ResponseEntity.ok(updateCoupon);
        }
        throw new CouponNotFound("Please insert a existent coupon id");
    }

    @DeleteMapping("/companies/delete-coupon/{couponId}")
    public ResponseEntity<Coupon> removeCouponById(@RequestParam String token, @PathVariable long couponId)
            throws TimeSessionExpiredException, CouponNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        Optional<Coupon> optCoupon = service.getCouponById(couponId);

        if (optCoupon != null && optCoupon.isPresent() && optCoupon.get().getCompany().getId() == session
                .getClientId()) {

            Coupon coupon = optCoupon.get();

            service.removeCoupon(coupon.getId());

            return ResponseEntity.ok(coupon);
        }
        throw new CouponNotFound("Error, coupon not found.");
    }

    @GetMapping("/companies/get-customer-info")
    public ResponseEntity<List<CustomerInfo>> getCustomersContact(String token)
            throws TimeSessionExpiredException, NotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        List<CustomerInfo> customersInfo = service.getCustomersInformation(session.getClientId());

        if (!customersInfo.isEmpty()) {
            return ResponseEntity.ok(customersInfo);
        }
        throw new NotFound("Not found!!!");
    }
}
