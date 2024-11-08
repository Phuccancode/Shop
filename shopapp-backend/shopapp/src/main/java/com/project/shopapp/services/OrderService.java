package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderStatus;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.responses.OrderResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        User user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found with id "+orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        order = modelMapper.map(orderDTO, Order.class);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new DateTimeException("Invalid shipping date");
        }
        order.setActive(true);
        order.setShippingDate(shippingDate);
        orderRepository.save(order);
        modelMapper.typeMap(Order.class, OrderResponse.class)
                .addMappings(mapper -> mapper.skip(OrderResponse::setUserId));
        //model mapper phai match all fields, neu co 1 filed khong match thi phai skip,
        // khong skip thi gia tri tra ve se bi null het
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderResponse getOrder(Long orderId) {
        return null;
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) {
        return null;
    }

    @Override
    public void deleteOrder(Long orderId) {

    }

    @Override
    public List<OrderResponse> getAllOrders(Long userId) {
        return null;
    }
}
