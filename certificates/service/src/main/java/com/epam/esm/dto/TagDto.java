package com.epam.esm.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TagDto extends RepresentationModel<TagDto> {
    private long id;

    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9_-]{3,16}$")
    private String name;
}
