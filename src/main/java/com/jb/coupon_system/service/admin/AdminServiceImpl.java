package com.jb.coupon_system.service.admin;

import com.jb.coupon_system.data.entity.Admin;
import com.jb.coupon_system.data.entity.Company;
import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.entity.Customer;
import com.jb.coupon_system.data.entity.CustomerInfo;
import com.jb.coupon_system.data.repo.AdminRepository;
import com.jb.coupon_system.data.repo.CompanyRepository;
import com.jb.coupon_system.data.repo.CouponRepository;
import com.jb.coupon_system.data.repo.CustomerRepository;
import com.jb.coupon_system.data.repo.DynamicRepositoryForCustomer;

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
 * @since 02/08/2020
 */
@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepo;
    private final CompanyRepository companyRepo;
    private final CouponRepository couponRepo;
    private final CustomerRepository customerRepo;
    private final DynamicRepositoryForCustomer customRepository;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepo, CompanyRepository companyRepo,
            CouponRepository couponRepo,
            CustomerRepository customerRepo, DynamicRepositoryForCustomer customRepository) {
        this.adminRepo = adminRepo;
        this.companyRepo = companyRepo;
        this.couponRepo = couponRepo;
        this.customerRepo = customerRepo;
        this.customRepository = customRepository;
    }

    @Override
    public Admin createAdmin(Admin admin) {

        admin.setId(0);

        adminRepo.save(admin);

        return admin;
    }

    @Override
    public Optional<Admin> getAdminById(long adminId) {
        return adminRepo.findById(adminId);
    }

    @Override
    public Admin updateAdmin(Admin admin, Admin adminNewData) {

        Admin updatedAdmin = toSetAdmin(admin, adminNewData);

        adminRepo.save(updatedAdmin);

        return updatedAdmin;
    }

    @Override
    public void removeAdmin(Admin admin) {

        adminRepo.delete(admin);
    }

    private Admin toSetAdmin(Admin admin, Admin adminNewData) {

        admin.setName(adminNewData.getName());
        admin.setEmail(adminNewData.getEmail());
        admin.setPassword(adminNewData.getPassword());

        return admin;
    }

    @Override
    public boolean adminUsernameIsPresent(Admin admin) {

        List<Admin> admins = adminRepo.findAll();

        int count = 0;

        for (Admin a : admins) {
            if (a.getEmail().contentEquals(admin.getEmail())) {
                ++count;
            }
        }
        return count != 0;
    }

    @Override
    public Company createCompany(Company company) {

        company.setId(0);

        companyRepo.save(company);

        return company;
    }

    @Override
    public boolean companyUsernameIsPresent(Company company) {

        List<Company> companies = companyRepo.findAll();

        int count = 0;

        for (Company c : companies) {
            if (c.getEmail().contentEquals(company.getEmail())) {
                ++count;
            }
        }
        return count != 0;
    }

    @Override
    public boolean companyUsernameMatches(Company newCompanyData, Company originalCompany) {
        return newCompanyData.getEmail().contentEquals(originalCompany.getEmail());
    }

    @Override
    public void removeCompanyById(long id) {

        companyRepo.deleteById(id);
    }

    @Override
    public Company updateCompany(Company company, Company newDataCompany) {

        Company updatedCompany = toSetCompany(company, newDataCompany);

        companyRepo.save(updatedCompany);

        return updatedCompany;
    }

    private Company toSetCompany(Company company, Company companyNewData) {

        company.setName(companyNewData.getName());
        company.setPassword(companyNewData.getPassword());

        return company;
    }

    @Override
    public Optional<Company> getCompany(long id) {
        return companyRepo.findById(id);
    }

    @Override
    public Optional<List<Customer>> getByFirstNameContains(String firstName) {
        return customerRepo.findByFirstNameContains(firstName);
    }

    @Override
    public List getByCustomFirstOrLastName(String firstName, String lastName) {
        return customRepository.findCustomerByNames(firstName, lastName);
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    @Override
    public Customer createCustomer(Customer customer) {

        customer.setId(0);

        customerRepo.save(customer);

        return customer;
    }

    @Override
    public boolean customerUsernameIsPresent(Customer customer) {

        List<Customer> customers = customerRepo.findAll();

        int count = 0;

        for (Customer c : customers) {
            if (c.getEmail().contentEquals(customer.getEmail())) {
                ++count;
            }
        }
        return count != 0;
    }

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
    public boolean customerUsernamesMatches(Customer customer, Customer customerNewData) {
        return customer.getEmail().contentEquals(customerNewData.getEmail());
    }

    @Override
    public boolean adminUsernamesMatches(Admin admin, Admin adminNewData) {
        return admin.getEmail().contentEquals(adminNewData.getEmail());
    }

    @Override
    public boolean couponTitleIsPresent(List<Coupon> companyCoupons, Coupon coupon) {

        int counter = 0;

        for (Coupon c : companyCoupons) {
            if (c.getTitle().contentEquals(coupon.getTitle())) {
                ++counter;
            }
        }
        return counter != 0;
    }

    @Override
    public boolean thereAreEmptyFields(Object o) {

        int checker = 0;

        if (o instanceof Admin && ((Admin) o).getName().contentEquals("") | ((Admin) o).getEmail()
                                                                                       .contentEquals("") | ((Admin) o)
                .getPassword().contentEquals("")) {
            ++checker;
        }

        if (o instanceof Company && ((Company) o).getName().contentEquals("") | ((Company) o).getEmail().contentEquals(
                "") | ((Company) o).getPassword().contentEquals("")) {
            ++checker;
        }

        if (o instanceof Customer && ((Customer) o).getFirstName().contentEquals("") | ((Customer) o).getLastName()
                                                                                                     .contentEquals(
                                                                                                             "") | ((Customer) o)
                .getEmail().contentEquals("") | ((Customer) o).getPassword().contentEquals("")) {
            ++checker;
        }

        if (o instanceof Coupon && ((Coupon) o).getTitle().contentEquals("") | ((Coupon) o).getDescription()
                                                                                           .contentEquals("")) {
            ++checker;
        }
        return checker != 0;

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

    private boolean infoIsPresent(List<CustomerInfo> customersInfo, CustomerInfo info) {
        int counter = 0;

        for (CustomerInfo cf : customersInfo) {
            if (cf.equals(info)) {
                ++counter;
            }
        }
        return counter != 0;
    }

    @Override
    public Optional<Customer> getCustomerById(long id) {
        return customerRepo.findById(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    @Override
    public Customer updateCustomer(Customer customer, Customer customerNewData) {

        Customer customerUpdated = toSetCustomer(customer, customerNewData);

        customerRepo.save(customerUpdated);

        return customerUpdated;
    }

    private Customer toSetCustomer(Customer customer, Customer customerNewData) {

        customer.setFirstName(customerNewData.getFirstName());
        customer.setLastName(customerNewData.getLastName());
        customer.setEmail(customerNewData.getEmail());
        customer.setPassword(customerNewData.getPassword());

        return customer;
    }

    @Override
    public void removeCustomerById(Customer customer) {

        customerRepo.delete(customer);
    }

    @Override
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
    public List<Coupon> getAllCoupons() {
        return couponRepo.findAll();
    }

    @Override
    public Optional<List<Coupon>> getAllCouponsByCompanyId(long id) {
        return couponRepo.findAllCouponsByCompanyId(id);
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
    public void removeCoupon(long id) {

        couponRepo.deleteById(id);
    }

    @Override
    public Optional<List<Coupon>> getAllCustomerCoupons(long customerId) {
        return couponRepo.findByCustomersId(customerId);
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
