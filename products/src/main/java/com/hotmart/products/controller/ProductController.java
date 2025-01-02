package com.hotmart.products.controller;

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

    @RoleSeller
    @PostMapping
    public ResponseEntity<ProductResponseDTO> save(@Validated @RequestBody ProductRequestDTO request) {
        return new ResponseEntity<>(service.save(request), HttpStatus.CREATED);
    }

    @RoleSeller
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable("id") Long id, @Validated @RequestBody ProductRequestDTO request) {
        return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);
    }

    @RoleSeller
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @RoleSeller
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "categoryId", required = false) Integer categoryId) {
        return new ResponseEntity<>(service.findAll(name, categoryId), HttpStatus.OK);
    }

    @RoleSeller
    @PutMapping("/plan/{id}")
    public ResponseEntity<PlanResponseDTO> updatePlan(@PathVariable("id") Long id, @Validated @RequestBody PlanRequestDTO request) {
        return new ResponseEntity<>(service.updatePlan(id, request), HttpStatus.OK);
    }

    @RoleSeller
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RoleSeller
    @DeleteMapping("/plan/{planId}")
    public ResponseEntity<Void> deletePlan(@PathVariable("planId") Long planId) {
        service.deletePlan(planId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
