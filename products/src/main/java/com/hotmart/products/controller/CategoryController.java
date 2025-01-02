package com.hotmart.products.controller;

import com.hotmart.products.config.security.RoleAdmin;
import com.hotmart.products.dto.request.CategoryRequestDTO;
import com.hotmart.products.dto.response.CategoryResponseDTO;
import com.hotmart.products.services.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

//    @RoleAdmin
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> save(@Validated @RequestBody CategoryRequestDTO request) {
        return new ResponseEntity<>(service.save(request), HttpStatus.CREATED);
    }

//    @RoleAdmin
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable("id") Integer id, @Validated @RequestBody CategoryRequestDTO request) {
        return new ResponseEntity<>(service.update(id, request), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll(@RequestParam(name = "name", required = false) String name) {
        return new ResponseEntity<>(service.findAll(name), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

//    @RoleAdmin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
