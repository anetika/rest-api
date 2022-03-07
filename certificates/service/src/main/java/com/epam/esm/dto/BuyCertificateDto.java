package com.epam.esm.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class BuyCertificateDto {

    @Min(1)
    private long userId;

    @Min(1)
    private long certificateId;
}
