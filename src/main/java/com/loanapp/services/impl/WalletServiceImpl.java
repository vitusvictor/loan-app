package com.loanapp.services.impl;

import com.loanapp.entities.User;
import com.loanapp.entities.Wallet;
import com.loanapp.repositories.WalletRepo;
import com.loanapp.services.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {
    private WalletRepo walletRepository;
    @Override
    public Wallet createWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(5000));
        wallet.setAccountNumber(user.getPhoneNumber());
        wallet.setUser(user);

        return walletRepository.save(wallet);
    }

}
