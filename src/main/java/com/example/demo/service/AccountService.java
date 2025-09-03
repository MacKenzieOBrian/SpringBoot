package com.example.demo.service;

import java.math.BigDecimal;

import com.example.demo.model.Account;

public interface AccountService {
    Account getAccountByUserId(Long userId);
    Account deposit(Long userId, BigDecimal amount, String description, String idempotencyKey);
    Account withdraw(Long userId, BigDecimal amount, String description, String idempotencyKey);
    boolean hasInsufficientFunds(Long userId, BigDecimal amount);
}
