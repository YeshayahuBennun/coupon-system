package com.jb.coupon_system.rest.controller;

import com.jb.coupon_system.rest.ex.AdminNotFound;
import com.jb.coupon_system.rest.ex.CategoryNotFound;
import com.jb.coupon_system.rest.ex.CompanyNotFound;
import com.jb.coupon_system.rest.ex.CouponNotFound;
import com.jb.coupon_system.rest.ex.CouponSoldOut;
import com.jb.coupon_system.rest.ex.CustomerNotFound;
import com.jb.coupon_system.rest.ex.EmptyFields;
import com.jb.coupon_system.rest.ex.EndDateNotFound;
import com.jb.coupon_system.rest.ex.InvalidLoginException;
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
import com.jb.coupon_system.rest.model.ServerErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This file is a part of coupon-system project.
 *
 * @author Yeshayahu Bennun
 * @version 1.0.0
 * @since 31/07/2020
 */
@RestControllerAdvice
public class ServerControllerAdvice {
    @ExceptionHandler(InvalidLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ServerErrorResponse handleUnauthorized(InvalidLoginException ex) {
        return ServerErrorResponse.ofNow("some error just happened.");
    }

    @ExceptionHandler(TimeSessionExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ServerErrorResponse handExpiredSessionTime(TimeSessionExpiredException ex) {
        return ServerErrorResponse.ofNow("Please login again.");
    }

    @ExceptionHandler(NoContent.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ServerErrorResponse NoContent(NoContent ex) {
        return ServerErrorResponse.ofNow("Sorry, the operation cannot be performed");
    }

    @ExceptionHandler(NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServerErrorResponse NotFound(NotFound ex) {
        return ServerErrorResponse.ofNow("The request could not be found.");
    }

    @ExceptionHandler(PurchasedCoupon.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ServerErrorResponse PurchasedCoupon(PurchasedCoupon ex) {
        return ServerErrorResponse.ofNow("You already own this product.");
    }

    @ExceptionHandler(CouponNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServerErrorResponse IdNotFound(CouponNotFound ex) {
        return ServerErrorResponse.ofNow("Coupon not found.");
    }

    @ExceptionHandler(CouponSoldOut.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ServerErrorResponse CouponSoldOut(CouponSoldOut ex) {
        return ServerErrorResponse.ofNow("Sorry, this coupon is sold out.");
    }

    @ExceptionHandler(CategoryNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServerErrorResponse CategoryNotFound(CategoryNotFound ex) {
        return ServerErrorResponse.ofNow("Please, insert an existent category.");
    }

    @ExceptionHandler(PriceNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServerErrorResponse PriceNotFound(PriceNotFound ex) {
        return ServerErrorResponse.ofNow("Sorry, there are no coupons below the price entered.");
    }

    @ExceptionHandler(EndDateNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServerErrorResponse EndDateNotFound(EndDateNotFound ex) {
        return ServerErrorResponse.ofNow("Sorry, there are no coupons before the end date entered.");
    }

    @ExceptionHandler(UsernameUnavailable.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ServerErrorResponse UsernameUnavailable(UsernameUnavailable ex) {
        return ServerErrorResponse.ofNow("Sorry,the username already exists on our server.");
    }

    @ExceptionHandler(CompanyNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServerErrorResponse CompanyNotFound(CompanyNotFound ex) {
        return ServerErrorResponse.ofNow("Company not found.");
    }

    @ExceptionHandler(UnviableUsernameUpdate.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ServerErrorResponse UnviableUsernameUpdate(UnviableUsernameUpdate ex) {
        return ServerErrorResponse.ofNow("Sorry,username can't be updated.");
    }

    @ExceptionHandler(CustomerNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServerErrorResponse CustomerNotFound(CustomerNotFound ex) {
        return ServerErrorResponse.ofNow("Customer not found.");
    }

    @ExceptionHandler(UnviableTitleUpdate.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ServerErrorResponse UnviableTitleUpdate(UnviableTitleUpdate ex) {
        return ServerErrorResponse.ofNow("Sorry,title can't be updated.");
    }

    @ExceptionHandler(UnviableDates.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ServerErrorResponse UnviableDates(UnviableDates ex) {
        return ServerErrorResponse.ofNow("Please, insert a valid Start and End dates.");
    }

    @ExceptionHandler(UnviablePrice.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ServerErrorResponse UnviablePrice(UnviablePrice ex) {
        return ServerErrorResponse.ofNow("Please, insert a price greater or equal than 0.");
    }

    @ExceptionHandler(AdminNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServerErrorResponse AdminNotFound(AdminNotFound ex) {
        return ServerErrorResponse.ofNow("Admin not found.");
    }

    @ExceptionHandler(TitleUnavailable.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ServerErrorResponse TitleUnavailable(TitleUnavailable ex) {
        return ServerErrorResponse.ofNow("Sorry,the coupon title already exists for this company.");
    }

    @ExceptionHandler(EmptyFields.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ServerErrorResponse EmptyFields(EmptyFields ex) {
        return ServerErrorResponse.ofNow("Please, all fields must be filled");
    }
}
