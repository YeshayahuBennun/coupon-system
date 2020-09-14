package com.jb.coupon_system.rest.controller.login;

import com.jb.coupon_system.rest.ClientSession;
import com.jb.coupon_system.rest.system.CompanySystem;
import com.jb.coupon_system.rest.ex.InvalidLoginException;
import com.jb.coupon_system.rest.ex.TimeSessionExpiredException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 29/07/2020
 */
@RestController
@RequestMapping("/api")
public class CompanyLogin {
    private static final int LENGTH_TOKEN = 15;
    private static final long LENGTH_SESSION_TIME = 180000;

    private final CompanySystem companySystem;
    private final Map<String, ClientSession> tokensMap;
    private long lastAccessMillis;

    @Autowired
    public CompanyLogin(CompanySystem companySystem, @Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
        this.companySystem = companySystem;
    }

    @PostMapping("/login-companies")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password)
            throws InvalidLoginException {

        ClientSession session = companySystem.createSession(email, password);

        String token = generateToken();

        lastAccessMillis = session.getLastAccessMillis();

        tokensMap.put(token, session);

        return ResponseEntity.ok(token);
    }

    @Scheduled(fixedRateString = "${rate.sessiontime}")
    private void checkLastSessionTime() throws TimeSessionExpiredException {
        if (lastAccessMillis != 0 && System.currentTimeMillis() - lastAccessMillis > LENGTH_SESSION_TIME) {
            tokensMap.clear();
        }
    }

    private String generateToken() {
        return UUID.randomUUID()
                   .toString()
                   .replaceAll("-", "")
                   .substring(0, LENGTH_TOKEN);
    }
}
