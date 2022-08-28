package com.loanapp.repositories;

import com.loanapp.entities.Loan;
import com.loanapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepo extends JpaRepository<Loan, Long> {

    Loan findByIdAndUser(Long id, User user);
}
