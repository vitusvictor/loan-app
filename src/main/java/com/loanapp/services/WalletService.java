package com.loanapp.services;

import com.loanapp.entities.User;
import com.loanapp.entities.Wallet;

public interface WalletService {
        Wallet createWallet(User user);
}
