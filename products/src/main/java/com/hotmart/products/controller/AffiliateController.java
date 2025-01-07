package com.hotmart.products.controller;

import com.hotmart.products.config.security.RoleSeller;
import com.hotmart.products.dto.response.UserResponseDTO;
import com.hotmart.products.services.affiliate.AffiliateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/affiliate")
@RequiredArgsConstructor
public class AffiliateController {

    private final AffiliateService service;

    // solicita a afiliação em algum produto
    @RoleSeller
    @PostMapping("/{productId}")
    public ResponseEntity<Void> requestAffiliation(@PathVariable("productId") Long productId) {
        service.requestAffiliation(productId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // remove a afiliação de algum produto
    @RoleSeller
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeAffiliation(@PathVariable("productId") Long productId) {
        service.removeAffiliation(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // busca as solicitações de afiliações do produto
    @RoleSeller
    @GetMapping("/requests/{productId}")
    public ResponseEntity<List<UserResponseDTO>> affiliatesRequestByProductId(
            @PathVariable("productId") Long productId,
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "email") String email) {
        return new ResponseEntity<>(service.affiliatesRequestByProductId(productId, name, email), HttpStatus.OK);
    }

    // permite afiliação do usuário em algum produto
    @RoleSeller
    @PostMapping("/permit-affiliation/user/{userId}/product/{productId}")
    public ResponseEntity<Void> permitAffiliation(
            @PathVariable("userId") Long userId,
            @PathVariable("productId") Long productId
    ) {
        service.permitAffiliation(productId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
