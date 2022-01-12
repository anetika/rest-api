package com.epam.esm.util;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HateoasUtil {
    public void attacheTagLink(TagDto tagDto) {
        Link selfLink = linkTo(methodOn(TagController.class).getById(tagDto.getId())).withSelfRel();
        tagDto.add(selfLink);
    }

    public void attacheCertificateLink(GiftCertificateDto certificateDto) {
        Link selfLink = linkTo(methodOn(GiftCertificateController.class).getById(certificateDto.getId())).withSelfRel();
        certificateDto.add(selfLink);
        certificateDto.getTags().forEach(this::attacheTagLink);
    }

    public void attacheUserLink(UserDto userDto){
        Link selfLink = linkTo(methodOn(UserController.class).getById(userDto.getId())).withSelfRel();
        userDto.add(selfLink);
        userDto.getOrderDtoList().forEach(this::attacheOrderLink);
    }

    public void attacheOrderLink(OrderDto orderDto){
        Link selfLink = linkTo(methodOn(OrderController.class).getOrderById(orderDto.getId())).withSelfRel();
        orderDto.add(selfLink);
        attacheCertificateLink(orderDto.getCertificateDto());
    }
}
