package com.opera.controllers;

import com.opera.model.ShoppingCart;
import com.opera.model.dto.OrderResponseDto;
import com.opera.service.OrderService;
import com.opera.service.ShoppingCartService;
import com.opera.service.UserService;
import com.opera.service.mapper.OrderMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderMapper orderMapper;
    private final OrderService orderService;
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderMapper orderMapper, OrderService orderService,
                           ShoppingCartService shoppingCartService, UserService userService) {
        this.orderMapper = orderMapper;
        this.orderService = orderService;
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @PostMapping("/complete")
    public void completeOrder(Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String email = principal.getUsername();
        ShoppingCart shoppingCartByUserEmail = shoppingCartService
                .getByUser(userService.findByEmail(email).get());
        orderService.completeOrder(shoppingCartByUserEmail);
    }

    @GetMapping
    public List<OrderResponseDto> getHistoryOrdersForUser(Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String email = principal.getUsername();
        return orderService.getOrdersHistory(userService.findByEmail(email).get())
                .stream()
                .map(orderMapper::toOrderDto)
                .collect(Collectors.toList());
    }
}
