package com.innowise.userservice.service.impl;

import com.innowise.userservice.dto.request.UserFilterRequest;
import com.innowise.userservice.dto.request.UserRequest;
import com.innowise.userservice.dto.request.UpdateUserRequest;
import com.innowise.userservice.dto.response.UserResponse;
import com.innowise.userservice.exception.UserAlreadyExistsException;
import com.innowise.userservice.exception.UserNotFoundException;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.service.UserService;
import com.innowise.userservice.service.specification.UserSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private static final String USERS_CACHE = "users";

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  @Cacheable(value = USERS_CACHE, key = "#id")
  public UserResponse findById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
    return userMapper.fromUserToUserResponse(user);
  }

  @Override
  @Cacheable(value = USERS_CACHE, key = "'email:' + #email")
  public UserResponse findByEmail(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException(email));
    return userMapper.fromUserToUserResponse(user);
  }

  @Override
  @Transactional
  @CacheEvict(value = USERS_CACHE, key = "#request.email")
  public UserResponse create(UserRequest request) {
    userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
      throw new UserAlreadyExistsException(request.getEmail());
    });

    User user = userMapper.fromUserRequestToUser(request);
    user.setActive(true);
    User savedUser = userRepository.save(user);
    return userMapper.fromUserToUserResponse(savedUser);
  }

  @Override
  public Page<UserResponse> findAll(UserFilterRequest request) {
    Pageable pageable = PageRequest.of(request.getPage(),
        request.getSize(),
        Sort.by(Sort.Direction.DESC, "name")
    );
    Specification<User> specification = UserSpecification.filterByNameAndSurname(
        request.getName(),
        request.getSurname()
    );
    return userRepository.findAll(specification, pageable).map(userMapper::fromUserToUserResponse);
  }
  @Override
  @CacheEvict(value = USERS_CACHE, key = "#id")
  @Transactional
  public UserResponse update(Long id, UpdateUserRequest request) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

    if (StringUtils.isNotBlank(request.getName())) {
      user.setName(request.getName());
    }
    if (StringUtils.isNotBlank(request.getSurname())) {
      user.setSurname(request.getSurname());
    }
    if (request.getBirthDate() != null) {
      user.setBirthDate(request.getBirthDate());
    }
    if (StringUtils.isNotBlank(request.getEmail())){
       user.setEmail(request.getEmail());
    }
    User updatedUser = userRepository.save(user);
    return userMapper.fromUserToUserResponse(updatedUser);
  }

  @Override
  @Transactional
  @CacheEvict(value = USERS_CACHE, key = "#id")
  public void deactivate(Long id) {
    User user = userRepository.findByIdAndActiveTrue(id)
        .orElseThrow(() ->
            new UserNotFoundException(id));
    user.setActive(false);
    userRepository.save(user);
  }

  @Override
  @Transactional
  @CacheEvict(value = USERS_CACHE, key = "#id")
  public void delete(Long id) {
    userRepository.findById(id).orElseThrow(() ->
        new UserNotFoundException(id));
    userRepository.deleteById(id);
  }
}


