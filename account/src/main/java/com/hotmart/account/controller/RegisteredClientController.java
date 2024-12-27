package com.hotmart.account.controller;

import com.hotmart.account.dto.request.RegisteredClientRequestDTO;
import com.hotmart.account.dto.response.RegisteredClientResponseDTO;
import com.hotmart.account.services.registeredClient.RegisteredClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/registeredClient")
public class RegisteredClientController {

    private final RegisteredClientService registeredClientService;

    @PostMapping
    public ResponseEntity<Void> save(@Validated @RequestBody RegisteredClientRequestDTO request) {
        registeredClientService.save(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Integer id, @Validated @RequestBody RegisteredClientRequestDTO request) {
        registeredClientService.update(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisteredClient> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(registeredClientService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RegisteredClientResponseDTO>> findAll() {
        return new ResponseEntity<>(registeredClientService.findAll(), HttpStatus.OK);
    }

}

