package com.hotmart.account.controller;

import com.hotmart.account.config.security.RoleAdmin;
import com.hotmart.account.config.security.RoleSeller;
import com.hotmart.account.config.security.ScopeReadUser;
import com.hotmart.account.dto.request.UserRequestDTO;
import com.hotmart.account.dto.request.UserUpdatePasswordRequestDTO;
import com.hotmart.account.dto.request.UserUpdateRequestDTO;
import com.hotmart.account.dto.response.UserResponseDTO;
import com.hotmart.account.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@Validated @RequestBody final UserRequestDTO request) {
        return new ResponseEntity<>(userService.save(request), HttpStatus.CREATED);
    }

    @RoleAdmin
    @PutMapping("/admin/{id}")
    public ResponseEntity<UserResponseDTO> updateAdmin(@PathVariable("id") Long id, @Validated @RequestBody final UserUpdateRequestDTO request) {
        return new ResponseEntity<>(userService.update(id, request), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> update(@AuthenticationPrincipal Jwt jwt, @Validated @RequestBody final UserUpdateRequestDTO request) {
        Long id = Long.parseLong(jwt.getClaims().get("user_id").toString());
        return new ResponseEntity<>(userService.update(id, request), HttpStatus.OK);
    }

    @RoleAdmin
    @GetMapping("/admin/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @RoleAdmin
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> findById(@AuthenticationPrincipal Jwt jwt) {
        Long id = Long.parseLong(jwt.getClaims().get("user_id").toString());
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @ScopeReadUser
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(userService.findByEmail(email), HttpStatus.OK);
    }

    @RoleAdmin
    @PatchMapping("/admin/updatePassword/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable("id") Long id, @Validated @RequestBody final UserUpdatePasswordRequestDTO request) {
        userService.updatePassword(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal Jwt jwt, @Validated @RequestBody final UserUpdatePasswordRequestDTO request) {
        Long id = Long.parseLong(jwt.getClaims().get("user_id").toString());
        userService.updatePassword(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RoleAdmin
    @DeleteMapping("/admin/closeAccount/{id}")
    public ResponseEntity<Void> closeAccount(@PathVariable("id") Long id) {
        userService.closeAccount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RoleSeller
    @DeleteMapping("/closeAccount")
    public ResponseEntity<Void> closeAccount(@AuthenticationPrincipal Jwt jwt) {
        Long id = Long.parseLong(jwt.getClaims().get("user_id").toString());
        userService.closeAccount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RoleSeller
    @PostMapping("/dataAccessEmail")
    public ResponseEntity<Void> dataAccessRequestByEmail(@AuthenticationPrincipal Jwt jwt) {
        Long id = Long.parseLong(jwt.getClaims().get("user_id").toString());
        userService.dataAccessRequestByEmail(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
