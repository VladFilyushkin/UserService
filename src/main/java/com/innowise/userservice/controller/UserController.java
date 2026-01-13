package com.innowise.userservice.controller;

import com.innowise.userservice.dto.request.UpdateUserRequest;
import com.innowise.userservice.dto.request.UserFilterRequest;
import com.innowise.userservice.dto.request.UserRequest;
import com.innowise.userservice.dto.response.UserResponse;
import com.innowise.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<Page<UserResponse>> findAllUsers(@Valid UserFilterRequest filterRequest) {
    return ResponseEntity.ok(userService.findAll(filterRequest));
  }

  
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.findById(id));
  }

  @GetMapping("/email/")
  public ResponseEntity<UserResponse>  findByEmail(@RequestParam String email) {
    return ResponseEntity.ok(userService.findByEmail(email));
  }

  @PostMapping
  public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
  }

  @PutMapping("/{id}")
  public  ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {
    return ResponseEntity.ok(userService.update(id, request));
  }

  @PatchMapping("/{id}/deactivate")
  public ResponseEntity<Void> deactivate(@PathVariable Long id) {
    userService.deactivate(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }


}
