package com.epam.esm.exceptionhandler;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionMessage {
    private String errorMessage;
    private int errorCode;
}
