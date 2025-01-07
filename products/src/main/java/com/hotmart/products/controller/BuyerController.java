package com.hotmart.products.controller;

import com.hotmart.products.config.security.RoleSeller;
import com.hotmart.products.dto.response.UserResponseDTO;
import com.hotmart.products.services.buyer.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buyer")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService service;

    /**
     * Busca os compradores para o meu produto
    **/
    @RoleSeller
    @GetMapping("/{productId}")
    public ResponseEntity<List<UserResponseDTO>> findAll(
            @PathVariable("productId") Long productId,
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "email") String email) {
        return new ResponseEntity<>(service.findAll(productId, name, email), HttpStatus.OK);
    }

}
