package com.hotmart.products.controller;

import com.hotmart.products.config.security.RoleBuyer;
import com.hotmart.products.config.security.RoleSeller;
import com.hotmart.products.dto.request.PlanRequestDTO;
import com.hotmart.products.dto.request.ProductRequestDTO;
import com.hotmart.products.dto.response.PlanResponseDTO;
import com.hotmart.products.dto.response.ProductResponseDTO;
import com.hotmart.products.services.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    // Salva um produto
    @RoleSeller
    @PostMapping
    public ResponseEntity<ProductResponseDTO> save(@Validated @RequestBody ProductRequestDTO request) {
        return new ResponseEntity<>(service.save(request), HttpStatus.CREATED);
    }

    // Atualiza o produto
    @RoleSeller
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable("id") Long id, @Validated @RequestBody ProductRequestDTO request) {
        return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);
    }

    // busca o produto
    @RoleSeller
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    // busca todos os produtos do usuário
    @RoleSeller
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "categoryId", required = false) Integer categoryId) {
        return new ResponseEntity<>(service.findAll(name, categoryId), HttpStatus.OK);
    }

    // busca todos os produtos comprados do usuário
    @RoleBuyer
    @GetMapping("/buyer/all")
    public ResponseEntity<List<ProductResponseDTO>> findAllBuyerProducts() {
        return new ResponseEntity<>(service.findAllBuyerProducts(), HttpStatus.OK);
    }

    // atualiza o plano de um produto
    @RoleSeller
    @PutMapping("/plan/{id}")
    public ResponseEntity<PlanResponseDTO> updatePlan(@PathVariable("id") Long id, @Validated @RequestBody PlanRequestDTO request) {
        return new ResponseEntity<>(service.updatePlan(id, request), HttpStatus.OK);
    }

    // exclui o produto
    @RoleSeller
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // exclui um plano
    @RoleSeller
    @DeleteMapping("/plan/{planId}")
    public ResponseEntity<Void> deletePlan(@PathVariable("planId") Long planId) {
        service.deletePlan(planId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // busca todos os produtos que o usuário é afiliado
    @RoleSeller
    @GetMapping("/affiliate-products")
    public ResponseEntity<List<ProductResponseDTO>> findAllAffiliateProducts() {
        return new ResponseEntity<>(service.findAllAffiliateProducts(), HttpStatus.OK);
    }

    // busca todos os produtos afiliados pendentes do usuário
    @RoleSeller
    @GetMapping("/affiliate-products-pending")
    public ResponseEntity<List<ProductResponseDTO>> findAllAffiliateProductsPending() {
        return new ResponseEntity<>(service.findAllProductsAffiliateRequest(), HttpStatus.OK);
    }

}
