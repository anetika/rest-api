package com.epam.esm.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@ToString
public class OrderDto extends RepresentationModel<OrderDto> {
    private long id;

    @NotNull
    private GiftCertificateDto certificateDto;

    @NotNull
    @Min(10)
    @Max(10000)
    private BigDecimal totalPrice;

    private LocalDateTime orderDate;
}
