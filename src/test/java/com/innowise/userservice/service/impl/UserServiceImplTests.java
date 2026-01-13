package com.innowise.userservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.userservice.dto.request.UpdateUserRequest;
import com.innowise.userservice.dto.request.UserRequest;
import com.innowise.userservice.dto.response.UserResponse;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.exception.UserAlreadyExistsException;
import com.innowise.userservice.exception.UserNotFoundException;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper userMapper;
  @InjectMocks
  private UserServiceImpl userService;
  private User user;
  private UserResponse userResponse;
  private UserRequest userRequest;
  private UpdateUserRequest updateUserRequest;

  @BeforeEach
  void setup(){
    user = new User();
    userResponse = new UserResponse();
    userRequest = new UserRequest();
    updateUserRequest = new UpdateUserRequest();
    Long userId = 1L;
    String email = "dfs@mail.ru";
    Boolean active = true;
    user.setId(userId);
    user.setEmail(email);
    user.setActive(active);
    userResponse.setEmail(email);
    userRequest.setEmail(email);
    userResponse.setEmail(email);
  }

  @Test
  void findUserById_whenUserExists_shouldReturnUser() {

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userMapper.fromUserToUserResponse(user)).thenReturn(userResponse);

    UserResponse result = userService.findById(1L);
    assertNotNull(result);
    assertEquals(userResponse, result);
    verify(userRepository).findById(1L);
  }
  @Test
  void findUserById_whenUserExists_shouldReturnUserNotFound(){
    when(userRepository.findById(2L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.findById(2L));
    verify(userRepository, times(1)).findById(any());
    verify(userMapper, never()).fromUserToUserResponse(any());
  }

  @Test
  void findByEmail_whenUserExists_shouldReturnUser(){
    when(userRepository.findByEmail("dfs@mail.ru")).thenReturn(Optional.of(user));
    when(userMapper.fromUserToUserResponse(user)).thenReturn(userResponse);

    UserResponse result = userService.findByEmail("dfs@mail.ru");

    assertNotNull(result);
    assertEquals(userResponse, result);
    verify(userRepository).findByEmail("dfs@mail.ru");
    verify(userRepository, times(1)).findByEmail("dfs@mail.ru");
  }


  @Test
  void findUserByEmail_whenUserNotExists_shouldUserNotFound() {
    when(userRepository.findByEmail("dffds@mail.ru"))
        .thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> userService.findByEmail("dffds@mail.ru"));

    verify(userRepository).findByEmail("dffds@mail.ru");
    verify(userMapper, never()).fromUserToUserResponse(any());
  }

  @Test
  void createUser_whenEmailNotExists_shouldCreateUser() {
    when(userRepository.findByEmail("dfs@mail.ru"))
        .thenReturn(Optional.empty());
    when(userMapper.fromUserRequestToUser(userRequest))
        .thenReturn(user);
    when(userRepository.save(user))
        .thenReturn(user);
    when(userMapper.fromUserToUserResponse(user))
        .thenReturn(userResponse);

    UserResponse result = userService.create(userRequest);

    assertNotNull(result);
    assertEquals(userResponse, result);

    verify(userRepository).findByEmail("dfs@mail.ru");
    verify(userRepository).save(user);
  }

  @Test
  void createUser_whenEmailExists_shouldUserAlreadyExistsException() {
    when(userRepository.findByEmail("dfs@mail.ru"))
        .thenReturn(Optional.of(user));

    assertThrows(UserAlreadyExistsException.class,
        () -> userService.create(userRequest));

    verify(userRepository).findByEmail("dfs@mail.ru");
    verify(userRepository, never()).save(any());
  }

  @Test
  void deactivateUser_whenActiveUserExists_shouldDeactivateUser() {
    when(userRepository.findByIdAndActiveTrue(1L))
        .thenReturn(Optional.of(user));

    userService.deactivate(1L);

    assertFalse(user.getActive());
    verify(userRepository).findByIdAndActiveTrue(1L);
    verify(userRepository).save(user);
  }

  @Test
  void deactivateUser_whenUserNotFound_shouldUserNotFound() {
    when(userRepository.findByIdAndActiveTrue(2L))
        .thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> userService.deactivate(2L));

    verify(userRepository).findByIdAndActiveTrue(2L);
    verify(userRepository, never()).save(any());
  }

  @Test
  void updateUser_whenUserNotFound_shouldUserNotFound() {

    when(userRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class,
        () -> userService.update(1L, updateUserRequest));

    verify(userRepository).findById(1L);
    verify(userRepository, never()).save(any());
  }

  @Test
  void updateUser_whenValidRequest_shouldUpdateUser() {
    UpdateUserRequest request = new UpdateUserRequest();
    request.setName("NewName");
    request.setSurname("NewSurname");
    request.setEmail("new@mail.com"); // Новый email
    request.setBirthDate(LocalDateTime.now().minusDays(10)); // Новая дата рождения

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.fromUserToUserResponse(user)).thenReturn(userResponse);

    UserResponse result = userService.update(1L, request);

    assertNotNull(result);
    assertEquals(userResponse, result);

    verify(userRepository).findById(1L);
    verify(userRepository).save(user);
  }

  @Test
  void deleteUser_whenUserExists_shouldDeleteUser() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    userService.delete(1L);

    verify(userRepository).findById(1L);
    verify(userRepository).deleteById(1L);
  }
  @Test
  void deleteUser_whenUserNotFound_shouldUserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> userService.delete(1L));

    verify(userRepository).findById(1L);
    verify(userRepository, never()).deleteById(any());
  }
}
