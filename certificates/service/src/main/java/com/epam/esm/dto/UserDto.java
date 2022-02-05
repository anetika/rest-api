package com.epam.esm.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class UserDto extends RepresentationModel<UserDto> {
    private long id;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+$")
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+$")
    private String lastName;

    @NotNull
    @Email
    private String email;

    private String username;

    private List<OrderDto> orderDtoList;
}
