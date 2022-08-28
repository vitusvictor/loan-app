package com.loanapp.controller;

import com.loanapp.dtos.LoanRepaymentDto;
import com.loanapp.dtos.LoanRequestDto;
import com.loanapp.entities.LoanProduct;
import com.loanapp.response.LoanQualificationResponse;
import com.loanapp.services.LoanProcessing;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/loan")
public class LoanController {
    private final LoanProcessing loanProcessing;

    @PostMapping("/loan-request")
    public LoanQualificationResponse<LoanProduct> loanRequest(@RequestBody LoanRequestDto loanRequestDto) {
        return loanProcessing.loanRequest(loanRequestDto);
    }

    @GetMapping("/check-qualification")
    public String checkQualification() {
        return loanProcessing.checkQualification();
    }

    @PostMapping("/loan-repayment")
    public String loanRepayment(@RequestBody LoanRepaymentDto loanRepaymentDto) {
        return loanProcessing.loanRepayment(loanRepaymentDto);
    }

    @PostMapping("/process-loan")
    public String processLoan(@RequestBody LoanProduct pack) {
        return loanProcessing.processLoan(pack);
    }

}
