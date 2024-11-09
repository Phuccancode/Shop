package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.User;
import jakarta.persistence.*;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class OrderResponse {
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("full_name")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    private String note;

    @JsonProperty("order_date")
    private LocalDateTime orderDate;

    private String status;

    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("payment_method")
    private String paymentMethod;

    private Boolean active;//thuộc về admin

    public static OrderResponse fromOrder(Order order){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Order.class, OrderResponse.class)
                .addMappings(mapper -> mapper.skip(OrderResponse::setUserId));
        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
        orderResponse.setUserId(order.getUser().getId());
        //model mapper phai match all fields, neu co 1 filed khong match thi phai skip,
        // khong skip thi gia tri tra ve se bi null het
        return orderResponse;
    }
}
