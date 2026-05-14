package com.innowise.userservice.service;

import com.innowise.userservice.dto.request.UserFilterRequest;
import com.innowise.userservice.dto.request.UserRequest;
import com.innowise.userservice.dto.request.UpdateUserRequest;
import com.innowise.userservice.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {

  UserResponse findById(Long id);
  UserResponse findByEmail(String email);
  UserResponse create(UserRequest request);
  Page<UserResponse> findAll(UserFilterRequest request);
  UserResponse update(Long id, UpdateUserRequest request);
  void deactivate(Long id);
  void delete(Long id);
}
