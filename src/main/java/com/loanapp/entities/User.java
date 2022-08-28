package com.loanapp.entities;

import javax.validation.constraints.*;

import com.loanapp.enums.UserStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
@Entity
public class User {
    @Id
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Column(unique = true)
    @NotNull
    private String email;

    @Column(length = 11)
    @NotNull
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String password;

    @NotNull
    private String pin;

    @OneToOne(mappedBy = "user")
    private Wallet wallet;

    @OneToMany(mappedBy = "user")
    private List<Loan> loan;

    private Long previousOperationsCount;

    @NotNull
    private boolean qualified;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserStatus userStatus;


}
