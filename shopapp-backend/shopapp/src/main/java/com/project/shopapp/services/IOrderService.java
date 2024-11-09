package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;

    OrderResponse getOrder(Long orderId) throws Exception;

    OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws Exception;

    void deleteOrder(Long orderId) throws DataNotFoundException;

    List<OrderResponse> findByUserId(Long userId);
}
