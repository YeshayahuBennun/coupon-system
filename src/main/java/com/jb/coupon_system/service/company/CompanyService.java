package com.jb.coupon_system.service.company;

import com.jb.coupon_system.data.entity.Company;
import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.entity.CustomerInfo;

import java.util.List;
import java.util.Optional;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 28/07/2020
 */
public interface CompanyService {
    /**
     * Gets a company by id.
     *
     * @param id
     *
     * @return a company by id.
     */
    Optional<Company> getCompany(long id);

    /**
     * Gets all the coupons by company id.
     *
     * @param id
     *
     * @return a Optional list of coupons by company id.
     */
    Optional<List<Coupon>> getAllCouponsByCompanyId(long id);

    /**
     * Creates a coupon.
     *
     * @param coupon
     * @param company
     *
     * @return
     */
    Coupon createCoupon(Coupon coupon, Company company);

    /**
     * Gets a coupon by id.
     *
     * @param id
     *
     * @return
     */
    Optional<Coupon> getCouponById(long id);

    /**
     * Updates a coupon.
     *
     * @param coupon
     * @param couponNewData
     *
     * @return
     */
    Coupon updateCoupon(Coupon coupon, Coupon couponNewData);

    /**
     * Removes a coupon.
     *
     * @param id
     */
    void removeCoupon(long id);

    /**
     * Gets contacts information of all single customer that purchased a coupon of this company.
     *
     * @param companyId
     *
     * @return
     */
    List<CustomerInfo> getCustomersInformation(long companyId);

    /*Auxiliary and verification methods*/

    /**
     * Checks if the user modified the title coupon.
     *
     * @param title
     * @param newTitle
     *
     * @return
     */
    boolean titlesMatches(String title, String newTitle);

    /**
     * Checks if the user inserted valid dates.
     *
     * @param couponNewData
     *
     * @return
     */
    boolean validDates(Coupon couponNewData);

    /**
     * Checks if the user inserted a valid price.
     *
     * @param couponNewData
     *
     * @return
     */
    boolean validPrice(Coupon couponNewData);

    /**
     * Checks id a coupon title is present in the database for a specific company.
     *
     * @param coupons
     * @param coupon
     *
     * @return
     */
    boolean couponTitleIsPresent(List<Coupon> coupons, Coupon coupon);

    /**
     * check if there are no empty fields.
     *
     * @param coupon
     *
     * @return
     */
    boolean thereAreEmptyFields(Coupon coupon);
}
