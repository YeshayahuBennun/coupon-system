package com.jb.coupon_system.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 29/07/2020
 */
@Configuration
@PropertySource("classpath:application.properties")
@EnableScheduling
public class RestConfiguration {
    @Bean(name = "tokens")
    public Map<String, ClientSession> tokensMap() {
        return new HashMap<>();
    }
}
