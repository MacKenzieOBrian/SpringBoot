package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Transaction;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    public List<Transaction> getTransactionHistory(Long userId) {
        var account = accountRepository.findByUserId(userId);
        if (account == null) throw new EntityNotFoundException("Account not found for user: " + userId);
        return transactionRepository.findByAccountId(account.getId());
    }

    @Override
    public Transaction getTransactionById(String transactionId) {
        Transaction tx = transactionRepository.findByTransactionId(transactionId);
        if (tx == null) throw new EntityNotFoundException("Transaction not found: " + transactionId);
        return tx;
    }
}
