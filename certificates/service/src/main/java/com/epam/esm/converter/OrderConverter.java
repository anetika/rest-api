package com.epam.esm.converter;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {

    private final GiftCertificateConverter certificateConverter;

    public OrderConverter(GiftCertificateConverter certificateConverter) {
        this.certificateConverter = certificateConverter;
    }

    public Order convertDtoToEntity(OrderDto dto){
        Order order = new Order();
        order.setId(dto.getId());
        order.setCertificate(certificateConverter.convertDtoToEntity(dto.getCertificateDto()));
        order.setTotalPrice(dto.getTotalPrice());
        order.setOrderDate(dto.getOrderDate());
        return order;
    }

    public OrderDto convertEntityToDto(Order order){
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setCertificateDto(certificateConverter.convertEntityToDto(order.getCertificate()));
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalPrice(order.getTotalPrice());
        return dto;
    }
}
