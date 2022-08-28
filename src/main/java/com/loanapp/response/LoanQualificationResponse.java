package com.loanapp.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanQualificationResponse<T> {
    private HttpStatus status;
    private String message;
    private T pack;
}
