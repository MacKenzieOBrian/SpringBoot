package com.example.demo.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Account;
import com.example.demo.model.Account.AccountStatus;
import com.example.demo.model.Transaction;
import com.example.demo.model.Transaction.TransactionType;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public Account getAccountByUserId(Long userId) {
        Account account = accountRepository.findByUserId(userId);
        if (account == null) throw new EntityNotFoundException("Account not found for user: " + userId);
        return account;
    }

    @Override
    @Transactional
    public Account deposit(Long userId, BigDecimal amount, String description, String idempotencyKey) {
        Account account = getAccountByUserId(userId);
        if (account.getStatus() != AccountStatus.ACTIVE) throw new IllegalStateException("Account not active");
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        Transaction tx = new Transaction();
        tx.setTransactionId(UUID.randomUUID().toString());
        tx.setAccount(account);
        tx.setType(TransactionType.DEPOSIT);
        tx.setAmount(amount);
        tx.setBalanceAfter(account.getBalance());
        tx.setDescription(description);
        tx.setIdempotencyKey(idempotencyKey);
        transactionRepository.save(tx);
        return account;
    }

    @Override
    @Transactional
    public Account withdraw(Long userId, BigDecimal amount, String description, String idempotencyKey) {
        Account account = getAccountByUserId(userId);
        if (account.getStatus() != AccountStatus.ACTIVE) throw new IllegalStateException("Account not active");
        if (hasInsufficientFunds(userId, amount)) throw new IllegalArgumentException("Insufficient funds");
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        Transaction tx = new Transaction();
        tx.setTransactionId(UUID.randomUUID().toString());
        tx.setAccount(account);
        tx.setType(TransactionType.WITHDRAWAL);
        tx.setAmount(amount);
        tx.setBalanceAfter(account.getBalance());
        tx.setDescription(description);
        tx.setIdempotencyKey(idempotencyKey);
        transactionRepository.save(tx);
        return account;
    }

    @Override
    public boolean hasInsufficientFunds(Long userId, BigDecimal amount) {
        Account account = getAccountByUserId(userId);
        return account.getBalance().subtract(amount).compareTo(account.getMinimumBalance()) < 0;
    }
}
