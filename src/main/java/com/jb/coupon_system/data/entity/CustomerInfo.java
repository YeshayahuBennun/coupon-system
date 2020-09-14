package com.jb.coupon_system.data.entity;

import java.util.Objects;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 10/09/2020
 */
public class CustomerInfo implements Comparable {
    private String firstName;
    private String lastName;
    private String email;

    public CustomerInfo() {
        /*Empty*/
    }

    public CustomerInfo(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerInfo that = (CustomerInfo) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email);
    }

    @Override
    public int compareTo(Object o) {
        CustomerInfo c = (CustomerInfo) o;
        int lastComp = lastName.compareTo(c.lastName);
        return (lastComp != 0 ? lastComp : firstName.compareTo(c.firstName));
    }
}
