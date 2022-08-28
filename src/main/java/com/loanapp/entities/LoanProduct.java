package com.loanapp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoanProduct {
    private BigDecimal amount;
    private BigDecimal percentage;
    private Integer tenure;
}
