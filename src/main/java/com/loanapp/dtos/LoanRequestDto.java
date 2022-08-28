package com.loanapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class LoanRequestDto {
    private String email;
    private BigDecimal amount;
}
