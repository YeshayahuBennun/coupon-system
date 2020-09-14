package com.jb.coupon_system.rest.controller;

import com.jb.coupon_system.data.entity.Admin;
import com.jb.coupon_system.data.entity.Company;
import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.entity.Customer;
import com.jb.coupon_system.data.entity.CustomerInfo;
import com.jb.coupon_system.rest.ClientSession;
import com.jb.coupon_system.rest.ex.AdminNotFound;
import com.jb.coupon_system.rest.ex.CategoryNotFound;
import com.jb.coupon_system.rest.ex.CompanyNotFound;
import com.jb.coupon_system.rest.ex.CouponNotFound;
import com.jb.coupon_system.rest.ex.CouponSoldOut;
import com.jb.coupon_system.rest.ex.CustomerNotFound;
import com.jb.coupon_system.rest.ex.EmptyFields;
import com.jb.coupon_system.rest.ex.EndDateNotFound;
import com.jb.coupon_system.rest.ex.NoContent;
import com.jb.coupon_system.rest.ex.NotFound;
import com.jb.coupon_system.rest.ex.PriceNotFound;
import com.jb.coupon_system.rest.ex.PurchasedCoupon;
import com.jb.coupon_system.rest.ex.TimeSessionExpiredException;
import com.jb.coupon_system.rest.ex.TitleUnavailable;
import com.jb.coupon_system.rest.ex.UnviableDates;
import com.jb.coupon_system.rest.ex.UnviablePrice;
import com.jb.coupon_system.rest.ex.UnviableTitleUpdate;
import com.jb.coupon_system.rest.ex.UnviableUsernameUpdate;
import com.jb.coupon_system.rest.ex.UsernameUnavailable;
import com.jb.coupon_system.service.admin.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 02/08/2020
 */
@RestController
@RequestMapping("/api")
public class AdminController {
    private final AdminService service;
    private final Map<String, ClientSession> tokensMap;

    @Autowired
    public AdminController(AdminService service,
            @Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.service = service;
        this.tokensMap = tokensMap;
    }

    /*Admin Exclusive Methods - Admin*/

    @PostMapping("/admins/add")
    public ResponseEntity<Admin> createAdmin(@RequestParam String token, @RequestBody Admin admin)
            throws TimeSessionExpiredException, UsernameUnavailable, NoContent, EmptyFields {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        if (service.thereAreEmptyFields(admin)) {
            throw new EmptyFields("Please, all fields must be filled");
        }

        if (service.adminUsernameIsPresent(admin)) {
            throw new UsernameUnavailable(" Sorry,the username already exists on our server");
        }

        Admin newAdmin = service.createAdmin(admin);

        Optional<Admin> optAdmin = service.getAdminById(newAdmin.getId());

        if (optAdmin != null && optAdmin.isPresent()) {
            return ResponseEntity.ok(optAdmin.get());
        }
        throw new NoContent("Sorry, the operation cannot be performed");
    }

    @GetMapping("/admins/get")
    public ResponseEntity<Admin> getAdminById(@RequestParam String token, @RequestParam long adminId)
            throws TimeSessionExpiredException, AdminNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        Optional<Admin> optAdmin = service.getAdminById(adminId);

        if (optAdmin != null && optAdmin.isPresent()) {
            return ResponseEntity.ok(optAdmin.get());
        }
        throw new AdminNotFound("Admin not found.");
    }

    @PutMapping("/admins/update")
    public ResponseEntity<Admin> updateAdmin(@RequestParam String token, @RequestBody Admin adminNewData)
            throws TimeSessionExpiredException, AdminNotFound, UnviableUsernameUpdate, EmptyFields {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        if (service.thereAreEmptyFields(adminNewData)) {
            throw new EmptyFields("Please, all fields must be filled");
        }

        Optional<Admin> optAdmin = service.getAdminById(adminNewData.getId());

        if (optAdmin != null && optAdmin.isPresent()) {
            if (!service.adminUsernamesMatches(optAdmin.get(), adminNewData)) {
                throw new UnviableUsernameUpdate("Sorry,username can't be updated.");
            }

            Admin updatedAdmin = service.updateAdmin(optAdmin.get(), adminNewData);

            return ResponseEntity.ok(updatedAdmin);
        }
        throw new AdminNotFound("Error, admin not found");
    }

    @DeleteMapping("/admins/delete")
    public ResponseEntity<Admin> removeAdmin(@RequestParam String token, @RequestParam long adminId)
            throws TimeSessionExpiredException, AdminNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        Optional<Admin> optAdmin = service.getAdminById(adminId);

        if (optAdmin != null && optAdmin.isPresent()) {
            Admin deletedAdmin = optAdmin.get();

            service.removeAdmin(optAdmin.get());

            return ResponseEntity.ok(deletedAdmin);
        }
        throw new AdminNotFound("Error, admin not found");
    }

    /*Admin Exclusive Methods - Company*/

    @PostMapping("/companies/add")
    public ResponseEntity<Company> createCompany(@RequestParam String token, @RequestBody Company companyNewData)
            throws TimeSessionExpiredException, NoContent, UsernameUnavailable, EmptyFields {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        if (service.thereAreEmptyFields(companyNewData)) {
            throw new EmptyFields("Please, all fields must be filled");
        }

        if (service.companyUsernameIsPresent(companyNewData)) {
            throw new UsernameUnavailable(" Sorry,the username already exists on our server");
        }

        Company newCompany = service.createCompany(companyNewData);

        Optional<Company> optCompany = service.getCompany(newCompany.getId());

        if (optCompany != null && optCompany.isPresent()) {
            return ResponseEntity.ok(optCompany.get());
        }
        throw new NoContent("Sorry, the operation cannot be performed");
    }

    @GetMapping("/companies/{id}/get")
    private ResponseEntity<Company> getCompanyById(@RequestParam String token, @PathVariable long id)
            throws TimeSessionExpiredException, CompanyNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        Optional<Company> optCompany = service.getCompany(id);

        if (optCompany != null && optCompany.isPresent()) {
            return ResponseEntity.ok(optCompany.get());
        }
        throw new CompanyNotFound("Error, company not found");
    }

    @GetMapping("/companies/get-all")
    public ResponseEntity<List<Company>> getAllCompanies(@RequestParam String token)
            throws TimeSessionExpiredException, NotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        List<Company> companies = service.getAllCompanies();

        if (companies != null && !companies.isEmpty()) {
            return ResponseEntity.ok(companies);
        }
        throw new NotFound("The request could not be found");
    }

    @GetMapping("/companies/coupons/get-all")
    public ResponseEntity<List<Coupon>> getAllCoupons(@RequestParam String token)
            throws TimeSessionExpiredException, NotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        List<Coupon> coupons = service.getAllCoupons();

        if (coupons != null && !coupons.isEmpty()) {
            return ResponseEntity.ok(coupons);
        }
        throw new NotFound("The request could not be found.");
    }

    @PutMapping("/companies/update")
    public ResponseEntity<Company> updateCompany(@RequestParam String token, @RequestBody Company companyNewData)
            throws TimeSessionExpiredException, UnviableUsernameUpdate, CompanyNotFound, EmptyFields {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        if (service.thereAreEmptyFields(companyNewData)) {
            throw new EmptyFields("Please, all fields must be filled");
        }

        Optional<Company> optCompany = service.getCompany(companyNewData.getId());


        if (optCompany != null && optCompany.isPresent()) {
            if (!service.companyUsernameMatches(companyNewData, optCompany.get())) {
                throw new UnviableUsernameUpdate("Sorry,username can't be updated.");
            }

            Company updatedCompany = service.updateCompany(optCompany.get(), companyNewData);

            return ResponseEntity.ok(updatedCompany);
        }
        throw new CompanyNotFound("Error, company not found");
    }

    @DeleteMapping("companies/{id}/delete")
    public ResponseEntity<Company> removeCompanyById(@RequestParam String token, @PathVariable long id)
            throws TimeSessionExpiredException, CompanyNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        Optional<Company> optCompany = service.getCompany(id);

        if (optCompany != null && optCompany.isPresent()) {
            Company company = optCompany.get();

            service.removeCompanyById(company.getId());

            return ResponseEntity.ok(company);
        }
        throw new CompanyNotFound("Error, company not found.");
    }

    /*Admin Exclusive Methods - Customer*/

    @PostMapping("/customers/add")
    public ResponseEntity<Customer> createCustomer(@RequestParam String token, @RequestBody Customer customer)
            throws TimeSessionExpiredException, NoContent, UsernameUnavailable, EmptyFields {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        if (service.thereAreEmptyFields(customer)) {
            throw new EmptyFields("Please, all fields must be filled");
        }

        if (service.customerUsernameIsPresent(customer)) {
            throw new UsernameUnavailable(" Sorry,the username already exists on our server");
        }

        Customer newCustomer = service.createCustomer(customer);

        Optional<Customer> optCustomer = service.getCustomerById(newCustomer.getId());

        if (optCustomer != null && optCustomer.isPresent()) {
            return ResponseEntity.ok(optCustomer.get());
        }
        throw new NoContent("Sorry, the operation cannot be performed");
    }

    @GetMapping("/customers/{id}/get")
    public ResponseEntity<Customer> getCustomerById(@RequestParam String token, @PathVariable long id)
            throws TimeSessionExpiredException, CustomerNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        Optional<Customer> optCustomer = service.getCustomerById(id);

        if (optCustomer != null && optCustomer.isPresent()) {
            return ResponseEntity.ok(optCustomer.get());
        }
        throw new CustomerNotFound("Error, customer not found.");
    }

    @GetMapping("/customers/get-all")
    public ResponseEntity<List<Customer>> getAllCustomers(@RequestParam String token)
            throws TimeSessionExpiredException, NotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        List<Customer> allCustomers = service.getAllCustomers();

        if (allCustomers != null && !allCustomers.isEmpty()) {
            return ResponseEntity.ok(allCustomers);
        }
        throw new NotFound("The request could not be found.");
    }

    /**
     * Gets customers by first name or by a single letter of his complete name or char sequence of his first name.
     *
     * @param token
     * @param firstName
     *
     * @return
     *
     * @throws TimeSessionExpiredException
     * @throws NotFound
     */
    @GetMapping("/customers/get-by-first-name")
    public ResponseEntity<List<Customer>> getByNameOrSingleLetter(@RequestParam String token,
            @RequestParam String firstName)
            throws TimeSessionExpiredException, NotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<List<Customer>> optCoupons = service.getByFirstNameContains(firstName);

        if (optCoupons != null && optCoupons.isPresent()) {
            List<Customer> collect = service.getByFirstNameContains(firstName).get();

            /*Filter customers by a single or a sequence of letters of their names by instantiating them in the
            convert method to return them in the list.*/

            List<Customer> customersByNames = collect.stream()
                                                     .map(Customer::converter)
                                                     .collect(Collectors.toList());

            if (!collect.isEmpty()) {
                return ResponseEntity.ok(customersByNames);
            }
        }
        throw new NotFound("The request could not be found.");
    }

    /**
     * Gets customers by first name or last name.
     *
     * @param token
     * @param firstName
     * @param lastName
     *
     * @return
     *
     * @throws TimeSessionExpiredException
     * @throws NotFound
     */
    @GetMapping("/customers/custom/get-by-dynamic")
    public ResponseEntity<List<Customer>> getByCustomFirstOrLastName(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName)
            throws TimeSessionExpiredException, NotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        List<Customer> customers = service
                .getByCustomFirstOrLastName(firstName, lastName);

        if (customers != null && !customers.isEmpty()) {
            return ResponseEntity.ok(customers);
        }
        throw new NotFound("The request could not be found.");
    }

    @PutMapping("/customers/update")
    public ResponseEntity<Customer> updateCustomer(@RequestParam String token, @RequestBody Customer newCustomerData)
            throws TimeSessionExpiredException, UnviableUsernameUpdate, CustomerNotFound, EmptyFields {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        if (service.thereAreEmptyFields(newCustomerData)) {
            throw new EmptyFields("Please, all fields must be filled");
        }

        Optional<Customer> optCustomer = service.getCustomerById(newCustomerData.getId());

        if (optCustomer != null && optCustomer.isPresent()) {
            if (!service.customerUsernamesMatches(optCustomer.get(), newCustomerData)) {
                throw new UnviableUsernameUpdate("Sorry,username can't be updated.");
            }

            Customer customerUpdated = service.updateCustomer(optCustomer.get(), newCustomerData);

            return ResponseEntity.ok(customerUpdated);
        }
        throw new CustomerNotFound("Error, customer not found.");
    }

    @DeleteMapping("/customers/delete/{customerId}")
    public ResponseEntity<Customer> removeCustomer(@RequestParam String token, @PathVariable long customerId)
            throws TimeSessionExpiredException, CustomerNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        Optional<Customer> optCustomer = service.getCustomerById(customerId);

        if (optCustomer != null && optCustomer.isPresent()) {
            Customer customer = optCustomer.get();

            service.removeCustomerById(customer);

            return ResponseEntity.ok(customer);
        }
        throw new CustomerNotFound("Error,customer not found.");
    }

    /*Client Methods - Company*/

    @PostMapping("/companies/coupons/add")
    public ResponseEntity<Coupon> createCoupon(@RequestParam String token, @RequestParam long companyId,
            @RequestBody Coupon coupon)
            throws TimeSessionExpiredException, NoContent, CompanyNotFound, UnviableDates, UnviablePrice,
                   TitleUnavailable, EmptyFields {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
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

        Optional<Company> optCompany = service.getCompany(companyId);

        if (optCompany.isEmpty()) {
            throw new CompanyNotFound("Error, company not found");
        }

        Optional<List<Coupon>> optCoupons = service.getAllCouponsByCompanyId(companyId);

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

    @GetMapping("/companies/coupons/get-byId")
    public ResponseEntity<Coupon> getCouponByCouponId(@RequestParam String token, @RequestParam long couponId)
            throws TimeSessionExpiredException, CouponNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<Coupon> optCoupon = service.getCouponById(couponId);

        if (optCoupon != null && optCoupon.isPresent()) {
            return ResponseEntity.ok(optCoupon.get());
        }
        throw new CouponNotFound("Error,coupon not found.");
    }

    @GetMapping("/companies/coupons/{companyId}")
    public ResponseEntity<List<Coupon>> getAllCouponsByCompanyId(@RequestParam String token,
            @PathVariable long companyId)
            throws TimeSessionExpiredException, CompanyNotFound, NotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<List<Coupon>> optCoupons = service.getAllCouponsByCompanyId(companyId);

        Optional<Company> optCompany = service.getCompany(companyId);

        if (optCompany.isEmpty()) {
            throw new CompanyNotFound("Error, company not found.");
        }

        if (optCoupons != null && optCoupons.isPresent()) {
            return ResponseEntity.ok(optCoupons.get());
        }
        throw new NotFound("The request could not be found.");
    }

    @PutMapping("/companies/coupons/update")
    public ResponseEntity<Coupon> updateCoupon(@RequestParam String token, @RequestBody Coupon couponNewData)
            throws TimeSessionExpiredException, CouponNotFound, UnviableTitleUpdate, UnviableDates, UnviablePrice,
                   EmptyFields {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        if (service.thereAreEmptyFields(couponNewData)) {
            throw new EmptyFields("Please, all fields must be filled");
        }

        Optional<Coupon> optCoupon = service.getCouponById(couponNewData.getId());

        if (optCoupon != null && optCoupon.isPresent()) {
            if (!service.titlesMatches(optCoupon.get().getTitle(), couponNewData.getTitle())) {
                throw new UnviableTitleUpdate("Sorry,title can't be updated.");
            }

            if (!service.validDates(couponNewData)) {
                throw new UnviableDates("Please insert a valid Start and End dates");
            }

            if (!service.validPrice(couponNewData)) {
                throw new UnviablePrice("Please insert a price greater or equal than 0");
            }

            Coupon updatedCoupon = service.updateCoupon(optCoupon.get(), couponNewData);

            return ResponseEntity.ok(updatedCoupon);
        }
        throw new CouponNotFound("Please insert a existent id");
    }

    @DeleteMapping("/companies/coupons/delete")
    public ResponseEntity<Coupon> removeCoupon(@RequestParam String token, @RequestParam long couponId)
            throws TimeSessionExpiredException, CouponNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<Coupon> optCoupon = service.getCouponById(couponId);

        if (optCoupon != null && optCoupon.isPresent()) {
            Coupon coupon = optCoupon.get();

            service.removeCoupon(couponId);

            return ResponseEntity.ok(coupon);
        }
        throw new CouponNotFound("Error, coupon not found.");
    }

    @GetMapping("/companies/get-customer-info-byAdmin")
    public ResponseEntity<List<CustomerInfo>> getCustomersContact(@RequestParam String token,
            @RequestParam long companyId)
            throws TimeSessionExpiredException, NotFound, CompanyNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<Company> optCompany = service.getCompany(companyId);

        if (optCompany.isEmpty()) {
            throw new CompanyNotFound("Error, company not found");
        }

        List<CustomerInfo> customersInfo = service.getCustomersInformation(companyId);
        if (!customersInfo.isEmpty()) {
            return ResponseEntity.ok(customersInfo);
        }
        throw new NotFound("Not found!!!");
    }

    /*Client Methods - Customer*/

    @PostMapping("/customers/coupons/purchase-admin")
    public ResponseEntity<Coupon> purchaseCoupon(@RequestParam String token, @RequestParam long customerId,
            @RequestParam long couponId)
            throws TimeSessionExpiredException, PurchasedCoupon, CouponSoldOut, CouponNotFound,
                   CustomerNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again");
        }

        session.access();

        Optional<Coupon> optCoupon = service.getCouponById(couponId);

        Optional<Customer> optCustomer = service.getCustomerById(customerId);

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

    @GetMapping("/customers/coupons/purchased-get/{customerId}")
    public ResponseEntity<List<Coupon>> getPurchasedCoupons(@RequestParam String token, @PathVariable long customerId)
            throws TimeSessionExpiredException, NotFound, CustomerNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<List<Coupon>> optCoupons = service.getAllCustomerCoupons(customerId);

        Optional<Customer> optCustomer = service.getCustomerById(customerId);

        if (optCustomer.isEmpty()) {
            throw new CustomerNotFound("Error,customer not found");
        }

        if (optCoupons != null && optCoupons.isPresent()) {
            return ResponseEntity.ok(optCoupons.get());
        }
        throw new NotFound("The request could not be found.");
    }

    @GetMapping("/customers/coupons/admin-get-all")
    public ResponseEntity<List<Coupon>> getCouponsForPurchase(@RequestParam String token,
            @RequestParam long customerId)
            throws TimeSessionExpiredException, NotFound, CustomerNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<Customer> optCustomer = service.getCustomerById(customerId);

        if (optCustomer.isEmpty()) {
            throw new CustomerNotFound("Error, customer not found.");
        }

        Optional<List<Coupon>> optCoupons = service
                .getAllAvailableCouponsToPurchase(customerId);

        if (optCoupons != null && optCoupons.isPresent()) {
            return ResponseEntity.ok(optCoupons.get());
        }
        throw new NotFound("The request could not be found.");
    }

    @GetMapping("/customers/coupons/admin-get-category/{category}")
    public ResponseEntity<List<Coupon>> getForPurchaseByCategoryAndCustomerId(@RequestParam String token,
            @PathVariable int category, @RequestParam long customerId)
            throws TimeSessionExpiredException, CustomerNotFound, CategoryNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<Customer> optCustomer = service.getCustomerById(customerId);

        if (optCustomer.isEmpty()) {
            throw new CustomerNotFound("Error, customer not found.");
        }

        Optional<List<Coupon>> optCoupons = service
                .getCouponsToPurchaseByCategoryAndCustomerId(category, customerId);

        if (optCoupons != null && optCoupons.isPresent()) {
            return ResponseEntity.ok(optCoupons.get());
        }
        throw new CategoryNotFound("Error, please insert an existent category");
    }

    @GetMapping("/customers/coupons/admin-get-lower")
    public ResponseEntity<List<Coupon>> getCouponsLowerThanPriceForPurchase(@RequestParam String token,
            @RequestParam double price, @RequestParam long customerId)
            throws TimeSessionExpiredException, PriceNotFound, CustomerNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<Customer> optCustomer = service.getCustomerById(customerId);

        if (optCustomer.isEmpty()) {
            throw new CustomerNotFound("Error,customer not found");
        }

        Optional<List<Coupon>> optCoupons = service.getCouponsLowerThanPrice(price, customerId);

        if (optCoupons != null && optCoupons.isPresent()) {
            return ResponseEntity.ok(optCoupons.get());
        }
        throw new PriceNotFound("Error, there are no coupons below the price entered.");
    }

    @GetMapping("/customers/coupons/end-date-get")
    public ResponseEntity<List<Coupon>> getCouponsBeforeAnEndDateForPurchase(@RequestParam String token,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam LocalDateTime endDate,
            @RequestParam long customerId
    ) throws TimeSessionExpiredException, CustomerNotFound, EndDateNotFound {

        ClientSession session = tokensMap.get(token);

        if (session == null) {
            throw new TimeSessionExpiredException("Please login again.");
        }

        session.access();

        Optional<Customer> optCustomer = service.getCustomerById(customerId);

        if (optCustomer.isEmpty()) {
            throw new CustomerNotFound("Error,customer not found");
        }

        Optional<List<Coupon>> optCoupons = service.getCouponsBeforeAnEndDate(endDate, customerId);

        if (optCoupons != null && optCoupons.isPresent()) {
            return ResponseEntity.ok(optCoupons.get());
        }
        throw new EndDateNotFound("Error, there are no coupons before the end date entered");
    }
}
