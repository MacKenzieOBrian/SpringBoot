package com.example.demo.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Account;
import com.example.demo.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long userId) {
        Account account = accountService.getAccountByUserId(userId);
        return ResponseEntity.ok(account.getBalance());
    }

    @PostMapping("/{userId}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable Long userId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        Account account = accountService.deposit(userId, amount, description, idempotencyKey);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable Long userId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        Account account = accountService.withdraw(userId, amount, description, idempotencyKey);
        return ResponseEntity.ok(account);
    }
}
