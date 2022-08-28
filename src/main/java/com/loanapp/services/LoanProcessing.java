package com.loanapp.services;

import com.loanapp.dtos.LoanRepaymentDto;
import com.loanapp.dtos.LoanRequestDto;
import com.loanapp.entities.LoanProduct;
import com.loanapp.response.LoanQualificationResponse;
import java.math.BigDecimal;

public interface LoanProcessing {
    LoanQualificationResponse<LoanProduct> loanRequest(LoanRequestDto loanRequestDto);

    String checkQualification(Long operationsCount, Boolean isQualified);

    String checkQualification();

    BigDecimal expectedReturn(BigDecimal amount, BigDecimal rate);

    String processLoan(LoanProduct pack);

    String loanRepayment(LoanRepaymentDto loanRepaymentDto);
}
