package com.jb.coupon_system.rest;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 29/07/2020
 */
public class ClientSession {
    private final long clientId;
    private long lastAccessMillis;

    public ClientSession(long clientId, long lastAccessMillis) {
        this.clientId = clientId;
        this.lastAccessMillis = lastAccessMillis;
    }

    public static ClientSession create(long clientId) {
        return new ClientSession(clientId, System.currentTimeMillis());
    }

    public long getClientId() {
        return clientId;
    }

    public long getLastAccessMillis() {
        return lastAccessMillis;
    }

    public void access() {
        lastAccessMillis = System.currentTimeMillis();
    }
}
