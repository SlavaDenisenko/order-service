package com.denisenko.service;

import com.denisenko.dto.BillingRequest;
import com.denisenko.dto.BillingResponse;
import com.denisenko.event.UserCreatedEvent;
import com.denisenko.model.Account;
import com.denisenko.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;

    @KafkaListener(topics = "${spring.kafka.topics.user-creation}", groupId = "${spring.kafka.consumer.group-id}")
    public void createAccount(UserCreatedEvent userCreatedEvent) {
        Account account = new Account();
        account.setUserId(userCreatedEvent.getId());
        account.setUsername(userCreatedEvent.getUsername());
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);
        log.info("Account was created for user with username={}", userCreatedEvent.getUsername());
    }

    @Transactional
    public BillingResponse deposit(Integer userId, BillingRequest billingRequest) {
        Account account = findAccount(userId);
        if (account == null)
            return accountNotFoundResponse(userId);

        account.setBalance(account.getBalance().add(billingRequest.getAmount()));
        log.info("The user's ({}) balance has been increased by {}", account.getUsername(), billingRequest.getAmount());
        return BillingResponse.builder()
                .success(true)
                .remainingBalance(account.getBalance())
                .message("Deposit successful")
                .build();
    }

    @Transactional
    public BillingResponse debit(Integer userId, BillingRequest billingRequest) {
        Account account = findAccount(userId);
        if (account == null)
            return accountNotFoundResponse(userId);

        if (account.getBalance().compareTo(billingRequest.getAmount()) < 0)
            return BillingResponse.builder()
                    .success(false)
                    .remainingBalance(account.getBalance())
                    .message("Insufficient funds")
                    .build();

        account.setBalance(account.getBalance().subtract(billingRequest.getAmount()));
        log.info("The user's ({}) balance has been reduced by {}", account.getUsername(), billingRequest.getAmount());
        return BillingResponse.builder()
                .success(true)
                .remainingBalance(account.getBalance())
                .message("Debit successful")
                .build();
    }

    public BillingResponse getBalance(Integer userId) {
        Account account = findAccount(userId);
        if (account == null)
            return accountNotFoundResponse(userId);

        return BillingResponse.builder()
                .success(true)
                .remainingBalance(account.getBalance())
                .message("Balance retrieved successful")
                .build();
    }

    private Account findAccount(Integer userId) {
        return accountRepository.findByUserId(userId).orElse(null);
    }

    private BillingResponse accountNotFoundResponse(Integer userId) {
        log.warn("User with ID={} not found", userId);
        return BillingResponse.builder()
                .success(false)
                .message("User with ID=" + userId + " not found")
                .build();
    }
}
