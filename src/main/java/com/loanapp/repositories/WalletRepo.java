package com.loanapp.repositories;

import com.loanapp.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository<Wallet, Long> {
}
