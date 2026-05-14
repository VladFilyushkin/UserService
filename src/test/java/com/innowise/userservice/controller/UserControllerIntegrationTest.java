package com.innowise.userservice.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.innowise.userservice.config.BaseIntegrationTest;
import com.innowise.userservice.dto.request.UpdateUserRequest;
import com.innowise.userservice.dto.request.UserRequest;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

class UserControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ObjectMapper objectMapper;

  private User user;

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
  }

  @BeforeEach
  void setup() {
    String email = "gdfdfg@mail.ru";
    String name = "name";
    String surname = "surname";
    Boolean active = true;
    LocalDateTime birthDate = LocalDateTime.now().minusDays(15);
    user = new User();
    user.setEmail(email);
    user.setName(name);
    user.setSurname(surname);
    user.setBirthDate(birthDate);
    user.setActive(active);
    userRepository.save(user);

  }


  @Test
  void getUserById_ShouldReturnUser() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", user.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(user.getId()))
        .andExpect(jsonPath("$.name").value(user.getName()))
        .andExpect(jsonPath("$.email").value(user.getEmail()));
  }

  @Test
  void getUserById_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", 55L))
        .andExpect(status().isNotFound());
  }

  @Test
  void createUser_ShouldCreateUser() throws Exception {
    UserRequest userRequest = new UserRequest();
    userRequest.setName("John");
    userRequest.setSurname("Doe");
    userRequest.setEmail("john.doe@mail.com");
    userRequest.setBirthDate(LocalDateTime.now().minusYears(25));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.email").value("john.doe@mail.com"))
        .andExpect(jsonPath("$.name").value("John"));
  }

  @Test
  void updateUser_ShouldUpdateUser() throws Exception {
    UpdateUserRequest updateRequest = new UpdateUserRequest();
    updateRequest.setName("Updated Name");
    updateRequest.setSurname("Updated Surname");
    updateRequest.setEmail("updated@mail.com");
    updateRequest.setBirthDate(LocalDateTime.now().minusYears(30));

    mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated Name"))
        .andExpect(jsonPath("$.surname").value("Updated Surname"))
        .andExpect(jsonPath("$.email").value("updated@mail.com"));
  }

  @Test
  void deactivateUser_ShouldDeactivateUser() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/{id}/deactivate", user.getId()))
        .andExpect(status().isNoContent());


  }

  @Test
  void deleteUser_ShouldDeleteUser() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", user.getId()))
        .andExpect(status().isNoContent());


  }

  @Test
  void getUserByEmail_ShouldReturnUser() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/email/")
            .param("email", user.getEmail()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(user.getEmail()))
        .andExpect(jsonPath("$.name").value(user.getName()));
  }

  @Test
  void getUserByEmail_ShouldReturnNotFound_WhenEmailNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/email/")
            .param("email", "nonexistent@mail.com"))
        .andExpect(status().isNotFound());
  }

  @Test
  void findAllUsers_ShouldReturnAllUsers() throws Exception {
    UserRequest userRequest = new UserRequest();
    userRequest.setName("Alice");
    userRequest.setSurname("Smith");
    userRequest.setEmail("alice.smith@mail.com");
    userRequest.setBirthDate(LocalDateTime.now().minusYears(28));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(status().isCreated());

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(2));
  }


  @Test
  void createUser_ShouldReturnBadRequest_WhenMissingRequiredFields() throws Exception {
    UserRequest invalidUserRequest = new UserRequest();
    invalidUserRequest.setSurname("Doe");
    invalidUserRequest.setEmail("john.doe@mail.com");
    invalidUserRequest.setBirthDate(LocalDateTime.now().minusYears(25));
    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidUserRequest)))
        .andExpect(status().isBadRequest());

  }

  @Test
  void createUser_ShouldReturnBadRequest_WhenInvalidEmail() throws Exception {
    UserRequest invalidEmailRequest = new UserRequest();
    invalidEmailRequest.setName("John");
    invalidEmailRequest.setSurname("Doe");
    invalidEmailRequest.setEmail("invalid-email");
    invalidEmailRequest.setBirthDate(LocalDateTime.now().minusYears(25));
    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidEmailRequest)))
        .andExpect(status().isBadRequest());

  }

  @Test
  void createUser_ShouldReturnBadRequest_WhenBirthDateIsInTheFuture() throws Exception {
    UserRequest invalidBirthDateRequest = new UserRequest();
    invalidBirthDateRequest.setName("John");
    invalidBirthDateRequest.setSurname("Doe");
    invalidBirthDateRequest.setEmail("john.doe@mail.com");
    invalidBirthDateRequest.setBirthDate(
        LocalDateTime.now().plusDays(1));
    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidBirthDateRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateUser_ShouldReturnBadRequest_WhenInvalidEmail() throws Exception {
    UpdateUserRequest updateRequest = new UpdateUserRequest();
    updateRequest.setName("Updated Name");
    updateRequest.setSurname("Updated Surname");
    updateRequest.setEmail("invalid-email");
    updateRequest.setBirthDate(LocalDateTime.now().minusYears(30));
    mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isBadRequest());

  }

  @Test
  void updateUser_ShouldReturnBadRequest_WhenBirthDateIsInTheFuture() throws Exception {
    UpdateUserRequest updateRequest = new UpdateUserRequest();
    updateRequest.setName("Updated Name");
    updateRequest.setSurname("Updated Surname");
    updateRequest.setEmail("updated@mail.com");
    updateRequest.setBirthDate(LocalDateTime.now().plusDays(1));
    mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deactivateUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/{id}/deactivate", 999L))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", 999L))
        .andExpect(status().isNotFound());
  }


  @Test
  void findAllUsers_ShouldReturnBadRequest_WhenInvalidPageNumber() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
            .param("page", "-1")
            .param("size", "10"))
        .andExpect(status().isBadRequest());
  }
}