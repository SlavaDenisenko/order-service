package com.denisenko.controller;

import com.denisenko.dto.BillingRequest;
import com.denisenko.dto.BillingResponse;
import com.denisenko.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/{userId}/deposit")
    @ResponseStatus(OK)
    public BillingResponse deposit(@PathVariable Integer userId, @RequestBody BillingRequest billingRequest) {
        return accountService.deposit(userId, billingRequest);
    }

    @PostMapping("/{userId}/debit")
    @ResponseStatus(OK)
    public BillingResponse debit(@PathVariable Integer userId, @RequestBody BillingRequest billingRequest) {
        return accountService.debit(userId, billingRequest);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(OK)
    public BillingResponse getBalance(@PathVariable Integer userId) {
        return accountService.getBalance(userId);
    }
}
