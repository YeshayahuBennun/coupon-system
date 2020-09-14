package com.jb.coupon_system.service.admin;

import com.jb.coupon_system.data.entity.Admin;
import com.jb.coupon_system.data.entity.Company;
import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.entity.Customer;
import com.jb.coupon_system.data.entity.CustomerInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 01/08/2020
 */
public interface AdminService {

    /* Admin Exclusive Methods - Admin*/

    /**
     * Creates a admin
     *
     * @param admin
     *
     * @return
     */
    Admin createAdmin(Admin admin);

    /**
     * Gets a admin
     *
     * @param adminId
     */
    Optional<Admin> getAdminById(long adminId);

    /**
     * Updates an admin.
     *
     * @param admin
     * @param adminNewData
     *
     * @return
     */
    Admin updateAdmin(Admin admin, Admin adminNewData);

    /**
     * Removes an admin.
     *
     * @param admin
     */
    void removeAdmin(Admin admin);

    /* Admin Exclusive Methods - Company*/

    /**
     * Creates a company.
     *
     * @param company
     *
     * @return
     */
    Company createCompany(Company company);

    /**
     * Gets a company by id.
     *
     * @param id
     *
     * @return a Optional company by id.
     */
    Optional<Company> getCompany(long id);

    /**
     * Gets all companies form the data base.
     *
     * @return a List of companies.
     */
    List<Company> getAllCompanies();

    /**
     * Updates a company.
     *
     * @param company
     * @param newDataCompany
     *
     * @return
     */
    Company updateCompany(Company company, Company newDataCompany);

    /**
     * Removes a company by id.
     *
     * @param id
     */
    void removeCompanyById(long id);

    /* Admin Exclusive Methods - Customer*/

    /**
     * Creates a customer.
     *
     * @param customer
     *
     * @return
     */
    Customer createCustomer(Customer customer);

    /**
     * Gets a customer from the data base by id.
     *
     * @param id
     *
     * @return a customer by id.
     */
    Optional<Customer> getCustomerById(long id);

    /**
     * Gets all customer from the data base.
     *
     * @return a List of customers from the data base.
     */
    List<Customer> getAllCustomers();

    /**
     * Updates a customer.
     *
     * @param customer
     * @param customerNewData
     *
     * @return
     */
    Customer updateCustomer(Customer customer, Customer customerNewData);

    /**
     * Removes a customer.
     *
     * @param customer
     */
    void removeCustomerById(Customer customer);

    /* Client Methods - Company*/

    /**
     * Creates a coupon
     *
     * @param coupon
     * @param company
     *
     * @return
     */
    Coupon createCoupon(Coupon coupon, Company company);

    /**
     * Gets all coupons by company id.
     *
     * @param id
     *
     * @return a List of companies.
     */
    Optional<List<Coupon>> getAllCouponsByCompanyId(long id);

    /**
     * Gets a coupon by id.
     *
     * @param id
     *
     * @return a coupon by id.
     */
    Optional<Coupon> getCouponById(long id);

    /**
     * Filters the customer database and obtains all of them from a single letter of their first name.
     *
     * @param firstName
     *
     * @return a list of customers
     */
    Optional<List<Customer>> getByFirstNameContains(String firstName);

    /**
     * Gets all customers by first name or last name.
     *
     * @param firstName
     * @param lastName
     *
     * @return a list of customers.
     */
    List<Customer> getByCustomFirstOrLastName(String firstName, String lastName);

    /**
     * Gets all coupons from a data base.
     *
     * @return a List of all coupons from the data base.
     */
    List<Coupon> getAllCoupons();

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
     * Gets contacts information of all single customer that purchased a coupon of a specific company.
     *
     * @param companyId
     *
     * @return
     */
    List<CustomerInfo> getCustomersInformation(long companyId);

    /* Client Methods - Customer*/

    Coupon purchaseCoupon(Coupon coupon, Customer customer);

    /**
     * Gets all customer coupons.
     *
     * @param customerId
     *
     * @return a optional list of coupons.
     */
    Optional<List<Coupon>> getAllCustomerCoupons(long customerId);

    /**
     * Gets all purchased coupons of a customer.
     *
     * @param customerId
     *
     * @return
     */
    Optional<List<Coupon>> getAllAvailableCouponsToPurchase(long customerId);

    /**
     * Gets all available coupons in the database.
     *
     * @param category
     * @param customerId
     *
     * @return
     */
    Optional<List<Coupon>> getCouponsToPurchaseByCategoryAndCustomerId(int category, long customerId);

    /**
     * Gets all coupons to purchase lower than a specific price.
     *
     * @param price
     * @param customerId
     *
     * @return
     */
    Optional<List<Coupon>> getCouponsLowerThanPrice(double price, long customerId);

    /**
     * Gets coupons before a specific end date.
     *
     * @param endDate
     * @param customerId
     *
     * @return
     */
    Optional<List<Coupon>> getCouponsBeforeAnEndDate(LocalDateTime endDate, long customerId);

    /*Auxiliary and verification methods*/

    /**
     * Checks if a admin username exist.
     *
     * @param admin
     *
     * @return
     */
    boolean adminUsernameIsPresent(Admin admin);

    /**
     * Checks if a company username exist.
     *
     * @param company
     *
     * @return
     */
    boolean companyUsernameIsPresent(Company company);

    /**
     * Checks if the original username has been modified.
     *
     * @param newCompanyData
     * @param originalCompany
     *
     * @return
     */
    boolean companyUsernameMatches(Company newCompanyData, Company originalCompany);

    /**
     * Checks if a company username exist.
     *
     * @param customer
     *
     * @return
     */
    boolean customerUsernameIsPresent(Customer customer);

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
     * Checks if a coupon has already been purchased by the customer
     *
     * @param coupon
     * @param customer
     *
     * @return
     */
    boolean isPurchased(Coupon coupon, Customer customer);

    /**
     * Checks if the user modified the username of a customer.
     *
     * @param customer
     * @param customerNewData
     *
     * @return
     */
    boolean customerUsernamesMatches(Customer customer, Customer customerNewData);

    /**
     * Checks if the user modified the username of an admin.
     *
     * @param admin
     * @param adminNewData
     *
     * @return
     */
    boolean adminUsernamesMatches(Admin admin, Admin adminNewData);

    /**
     * Checks id a coupon title is present in the database for a specific company.
     *
     * @param companyCoupons
     * @param coupon
     */
    boolean couponTitleIsPresent(List<Coupon> companyCoupons, Coupon coupon);

    /**
     * check if there are no empty fields.
     *
     * @param admin
     *
     * @return
     */
    boolean thereAreEmptyFields(Object o);
}
