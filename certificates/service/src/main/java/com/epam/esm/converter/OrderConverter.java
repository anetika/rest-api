package com.epam.esm.converter;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import org.springframework.stereotype.Component;

/**
 * The converter for {@link Order} and {@link OrderDto}.
 */
@Component
public class OrderConverter {

    private final GiftCertificateConverter certificateConverter;

    public OrderConverter(GiftCertificateConverter certificateConverter) {
        this.certificateConverter = certificateConverter;
    }

    /**
     * Converts order dto to entity.
     *
     * @param dto the order dto
     * @return the order
     */
    public Order convertDtoToEntity(OrderDto dto){
        return Order.builder()
                .id(dto.getId())
                .certificate(certificateConverter.convertDtoToEntity(dto.getCertificateDto()))
                .totalPrice(dto.getTotalPrice())
                .orderDate(dto.getOrderDate())
                .build();
    }

    /**
     * Converts entity to order dto.
     *
     * @param order the order
     * @return the order dto
     */
    public OrderDto convertEntityToDto(Order order){
        return OrderDto.builder()
                .id(order.getId())
                .certificateDto(certificateConverter.convertEntityToDto(order.getCertificate()))
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                .build();
    }
}
