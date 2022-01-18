package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Builder
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {
    private long id;

    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9_]{3,30}$")
    private String name;

    @NotNull
    @Pattern(regexp = "^[\\w\\s,.!?:;\"'-_]+$")
    private String description;

    @NotNull
    private BigDecimal price;

    @Min(0)
    @Max(365)
    private int duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime lastUpdateDate;

    @NotNull
    private Set<TagDto> tags;
}
