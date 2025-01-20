package com.hotmart.orders.controller;

import com.hotmart.orders.dto.request.OrderRequestDTO;
import com.hotmart.orders.dto.response.TransactionResponseDTO;
import com.hotmart.orders.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping("/create")
    public ResponseEntity<TransactionResponseDTO> createOrder(@RequestBody @Validated OrderRequestDTO request) {
        return new ResponseEntity<>(service.save(request), HttpStatus.CREATED);
    }

}
