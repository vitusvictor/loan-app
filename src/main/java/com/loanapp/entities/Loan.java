package com.loanapp.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createAt;

    @NotNull
    private LocalDateTime dueDate;

    private BigDecimal percentage;

    private LocalDateTime repaidAt;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal expectedReturn;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
