package com.innowise.userservice.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.userservice.config.BaseIntegrationTest;
import com.innowise.userservice.dto.request.PaymentCardFilterRequest;
import com.innowise.userservice.dto.request.PaymentCardRequest;
import com.innowise.userservice.dto.request.UserRequest;
import com.innowise.userservice.dto.response.PaymentCardResponse;
import com.innowise.userservice.entity.PaymentCard;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.repository.PaymentCardRepository;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.service.PaymentCardService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class PaymentCardControllerTest extends BaseIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private PaymentCardRepository paymentCardRepository;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PaymentCardService paymentCardService;

  private PaymentCard paymentCard;
  @BeforeEach
  void setup() {

    paymentCard = new PaymentCard();
    paymentCard.setNumber("1234567890123456");
    paymentCard.setHolder("John Doe");
    paymentCard.setExpirationDate(LocalDateTime.now().plusMonths(12));
    paymentCard.setActive(true);
    paymentCardRepository.save(paymentCard);


  }

  @AfterEach
  void tearDown() {
    paymentCardRepository.deleteAll();
  }


  @Test
  void createPaymentCard_ShouldCreateCard() throws Exception {
    UserRequest userRequest = new UserRequest();
    userRequest.setName("fsdfds");
    userRequest.setSurname("sdasdas");
    userRequest.setEmail("fsdfdsd@mail.ru");
    userRequest.setBirthDate(LocalDateTime.now().minusYears(30));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(status().isCreated());

    User createdUser = userRepository.findByEmail("fsdfdsd@mail.ru").orElseThrow();

    PaymentCardRequest paymentCardRequest = new PaymentCardRequest();
    paymentCardRequest.setNumber("2876543210987654");
    paymentCardRequest.setHolder("fsdfds sdasdas");
    paymentCardRequest.setExpirationDate(LocalDateTime.now().plusMonths(12));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/payment-cards/users/{userId}", createdUser.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(paymentCardRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.number").value("2876543210987654"))
        .andExpect(jsonPath("$.holder").value("fsdfds sdasdas"));
  }

  @Test
  void getPaymentCardById_ShouldReturnCard() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .get("/api/payment-cards/{id}", paymentCard.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.number").value(paymentCard.getNumber()))
        .andExpect(jsonPath("$.holder").value(paymentCard.getHolder()));
  }

  @Test
  void getAllPaymentCardsByUserId_ShouldReturnCards() throws Exception {

    UserRequest userRequest = new UserRequest();
    userRequest.setName("fsdfds");
    userRequest.setSurname("sdasdas");
    userRequest.setEmail("fsdfds@mail.ru");
    userRequest.setBirthDate(LocalDateTime.now().minusYears(30));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(status().isCreated());


    User createdUser = userRepository.findByEmail("fsdfds@mail.ru").orElseThrow();

    PaymentCardRequest paymentCardRequest = new PaymentCardRequest();
    paymentCardRequest.setNumber("9876543210987654");
    paymentCardRequest.setHolder("fsdfds sdasdas");
    paymentCardRequest.setExpirationDate(LocalDateTime.now().plusMonths(12));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/payment-cards/users/{userId}", createdUser.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(paymentCardRequest)))
        .andExpect(status().isCreated());


    mockMvc.perform(MockMvcRequestBuilders.get("/api/payment-cards/users/{userId}", createdUser.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].number").value("9876543210987654"))
        .andExpect(jsonPath("$[0].holder").value("fsdfds sdasdas"));
  }

  @Test
  void getAllPaymentCards_ShouldReturnCards() throws Exception {
    PaymentCardFilterRequest filterRequest = new PaymentCardFilterRequest();
    filterRequest.setPage(0);
    filterRequest.setSize(10);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/payment-cards")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1));
  }

  @Test
  void deactivatePaymentCard_ShouldDeactivateCard() throws Exception {

    UserRequest userRequest = new UserRequest();
    userRequest.setName("John");
    userRequest.setSurname("Doe");
    userRequest.setEmail("john.doe@mail.com");
    userRequest.setBirthDate(LocalDateTime.now().minusYears(30));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(status().isCreated());

    User createdUser = userRepository.findByEmail("john.doe@mail.com").orElseThrow();

    PaymentCardRequest paymentCardRequest = new PaymentCardRequest();
    paymentCardRequest.setNumber("9876543210987654");
    paymentCardRequest.setHolder("Jane Doe");
    paymentCardRequest.setExpirationDate(LocalDateTime.now().plusMonths(12));

    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/payment-cards/users/{userId}", createdUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentCardRequest)))
        .andExpect(status().isCreated());

    List<PaymentCardResponse> createdCards = paymentCardService.findAllByUserId(
        createdUser.getId());

    PaymentCardResponse createdCard = createdCards.getFirst();

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/api/payment-cards/users/{userId}/{cardId}/deactivate", createdUser.getId(),
                createdCard.getId()))
        .andExpect(status().isNoContent());
  }

  @Test
  void createPaymentCard_ShouldReturnBadRequest_WhenInvalidCardNumber() throws Exception {
    PaymentCardRequest invalidPaymentCardRequest = new PaymentCardRequest();
    invalidPaymentCardRequest.setNumber("12345");
    invalidPaymentCardRequest.setHolder("John Doe");
    invalidPaymentCardRequest.setExpirationDate(LocalDateTime.now().plusMonths(12));

    mockMvc.perform(MockMvcRequestBuilders
            .post("/api/payment-cards/users/{userId}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidPaymentCardRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createPaymentCard_ShouldReturnBadRequest_WhenExpirationDateIsInThePast() throws Exception {
    PaymentCardRequest invalidPaymentCardRequest = new PaymentCardRequest();
    invalidPaymentCardRequest.setNumber("9876543210987654");
    invalidPaymentCardRequest.setHolder("Jane Doe");
    invalidPaymentCardRequest.setExpirationDate(LocalDateTime.now().minusMonths(1));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/payment-cards/users/{userId}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidPaymentCardRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getPaymentCardById_ShouldReturnNotFound_WhenCardDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .get("/api/payment-cards/{id}", 55L))
        .andExpect(status().isNotFound());
  }


  @Test
  void deactivatePaymentCard_ShouldReturnNotFound_WhenCardDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .patch("/api/payment-cards/users/{userId}/{cardId}/deactivate", 1L, 55L))
        .andExpect(status().isNotFound());
  }

  @Test
  void deletePaymentCard_ShouldReturnNotFound_WhenCardDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .delete("/api/payment-cards/users/{userId}/{cardId}", 1L, 55L))
        .andExpect(status().isNotFound());
  }
}