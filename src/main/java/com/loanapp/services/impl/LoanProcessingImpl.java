package com.loanapp.services.impl;

import com.loanapp.dtos.LoanRepaymentDto;
import com.loanapp.dtos.LoanRequestDto;
import com.loanapp.entities.Loan;
import com.loanapp.entities.LoanProduct;
import com.loanapp.entities.User;
import com.loanapp.exceptions.UserNotFoundException;
import com.loanapp.repositories.LoanRepo;
import com.loanapp.repositories.UserRepo;
import com.loanapp.response.LoanQualificationResponse;
import com.loanapp.services.LoanProcessing;
import com.loanapp.util.SessionUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class LoanProcessingImpl implements LoanProcessing {
    private final LoanRepo loanRepository;
    private final UserRepo userRepository;
    private final SessionUtils sessionUtils;

    @Override
    public LoanQualificationResponse<LoanProduct> loanRequest(LoanRequestDto loanRequestDto) {
        User user = userRepository.findByEmail(loanRequestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String loanStatus = checkQualification( user.getPreviousOperationsCount(), user.isQualified());
        LoanQualificationResponse<LoanProduct> loanQualificationResponse = new LoanQualificationResponse<>();

        if (loanStatus.equals("Not Qualified")) {
            loanQualificationResponse.setMessage("You do not qualify for any loan package");
            loanQualificationResponse.setPack(null);
            loanQualificationResponse.setStatus(HttpStatus.OK);

            return loanQualificationResponse;
        }

        LoanProduct loanProduct = new LoanProduct();

        if (loanStatus.equals("Product B")) {
           loanProduct.setPercentage(BigDecimal.valueOf(12.5));
           loanProduct.setTenure(30);
           loanProduct.setAmount(BigDecimal.valueOf(25000));

           loanQualificationResponse.setMessage("Qualified for loan package B. Loan processed.");

        } else {
            loanProduct.setPercentage(BigDecimal.valueOf(10));
            loanProduct.setTenure(15);
            loanProduct.setAmount(BigDecimal.valueOf(10000));

            loanQualificationResponse.setMessage("Qualified for loan package A. Loan processed.");
        }

        loanQualificationResponse.setPack(loanProduct);
        loanQualificationResponse.setStatus(HttpStatus.OK);
        return loanQualificationResponse;

    }

    @Override
    public String checkQualification(Long operationsCount, Boolean isQualified) {
        if (isQualified && operationsCount > 3)
            return "Product B";
        if (isQualified)
            return "Product A";

        return "Not Qualified";
    }

    @Override
    public String checkQualification() {
        Long id = sessionUtils.getLoggedInUserId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.isQualified() && user.getPreviousOperationsCount() > 3)
            return "Product B";
        if (user.isQualified() )
            return "Product A";

        return "Not Qualified";
    }

    @Override
    public BigDecimal expectedReturn(BigDecimal amount, BigDecimal rate) {
        BigDecimal interest = (rate.multiply(amount)).divide(BigDecimal.valueOf(100));
        return amount.add(interest);
    }

    @Override
    public String processLoan(LoanProduct pack) {
        Long id = sessionUtils.getLoggedInUserId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String status = checkQualification(user.getPreviousOperationsCount(), user.isQualified());

        if (status.equals("Not Qualified"))
            return "Not Qualified for this loan pack";

        int compareValue = compareValue(pack.getAmount(), BigDecimal.valueOf(10000));

        if (status.equals("Product A") && compareValue == 1) {
            return "You only qualify for Product A loan pack";
        }

        compareValue = compareValue(pack.getAmount(), BigDecimal.valueOf(25000));
        if (status.equals("Product B") && compareValue == 1) {
            return "You can only take loan of 25000 max.";
        }

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setCreateAt(LocalDateTime.now());
        loan.setAmount(pack.getAmount());
        loan.setDueDate(LocalDateTime.now().plusDays(pack.getTenure()));
        loan.setPercentage(pack.getPercentage());
        loan.setExpectedReturn(expectedReturn(pack.getAmount(), pack.getPercentage()));

        loanRepository.save(loan);
        return "Loan processed";
    }

    public int compareValue(BigDecimal a, BigDecimal b) {
        return a.compareTo(b);
    }

    @Override
    public String loanRepayment(LoanRepaymentDto loanRepaymentDto) {
        Long loanId = loanRepaymentDto.getLoanId();
        BigDecimal amount = loanRepaymentDto.getAmount();

        Long id = sessionUtils.getLoggedInUserId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Loan loan = loanRepository.findByIdAndUser(loanId, user);
        loan.setRepaidAt(LocalDateTime.now());
        LocalDateTime dueDate = loan.getDueDate();

        if (dueDate.isBefore(LocalDateTime.now())) {
            user.setQualified(false);
        }

        user.setPreviousOperationsCount(user.getPreviousOperationsCount() + 1);
        User user1 = userRepository.save(user);

        loan.setUser(user1);
        loanRepository.save(loan);

        return "Updated successfully";
    }
}
