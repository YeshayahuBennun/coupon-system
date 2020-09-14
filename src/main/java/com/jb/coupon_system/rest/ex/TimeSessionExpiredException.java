package com.jb.coupon_system.rest.ex;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 31/07/2020
 */
public class TimeSessionExpiredException extends Exception {
    public TimeSessionExpiredException(String msg) {
        super(msg);
    }
}
