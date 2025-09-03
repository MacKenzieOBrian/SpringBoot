package com.example.demo.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Account;
import com.example.demo.model.Post;
import com.example.demo.model.Transaction;
import com.example.demo.model.Transaction.TransactionType;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.TransactionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final TransactionRepository transactionRepository;

    @Value("${fintech.post-creation-fee:0.50}")
    private BigDecimal postCreationFee;

    @Override
    @Transactional
    public Post createPost(Long userId, String title, String content) {
        Account account = accountRepository.findByUserId(userId);
        if (account == null) throw new EntityNotFoundException("Account not found for user: " + userId);
        if (account.getBalance().subtract(postCreationFee).compareTo(account.getMinimumBalance()) < 0) {
            throw new IllegalArgumentException("Insufficient funds for post creation");
        }
        account.setBalance(account.getBalance().subtract(postCreationFee));
        accountRepository.save(account);
        Transaction tx = new Transaction();
        tx.setTransactionId(UUID.randomUUID().toString());
        tx.setAccount(account);
        tx.setType(TransactionType.POST_FEE);
        tx.setAmount(postCreationFee);
        tx.setBalanceAfter(account.getBalance());
        tx.setDescription("Post creation fee");
        transactionRepository.save(tx);
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        return postRepository.save(post);
    }
}
