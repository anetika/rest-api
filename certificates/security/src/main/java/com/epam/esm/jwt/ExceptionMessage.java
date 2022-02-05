package com.epam.esm.jwt;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionMessage {
    private String errorMessage;
    private HttpStatus errorStatus;
    private int errorCode;
}
