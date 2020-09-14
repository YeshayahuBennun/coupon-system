package com.jb.coupon_system.rest.ex;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 31/08/2020
 */
public class CustomerNotFound extends Exception {
    public CustomerNotFound(String msg) {
        super(msg);
    }
}
