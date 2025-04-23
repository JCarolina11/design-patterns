package com.example.patterns_banking.repositories;

import com.example.patterns_banking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountRepository extends JpaRepository<Account, Long> {
}
