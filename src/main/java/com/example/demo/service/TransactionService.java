package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Transaction;

public interface TransactionService {
    List<Transaction> getTransactionHistory(Long userId);
    Transaction getTransactionById(String transactionId);
}
