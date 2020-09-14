package com.jb.coupon_system.rest.ex;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 02/09/2020
 */
public class UnviablePrice extends Exception {
    public UnviablePrice(String msg) {
        super(msg);
    }
}
