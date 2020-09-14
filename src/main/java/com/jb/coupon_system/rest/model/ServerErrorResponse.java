package com.jb.coupon_system.rest.model;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 31/07/2020
 */
public class ServerErrorResponse {
    private final String message;
    private final long timestamp;

    private ServerErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public static ServerErrorResponse ofNow(String message) {
        return new ServerErrorResponse(message, System.currentTimeMillis());
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
