package com.innowise.userservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.userservice.dto.request.PaymentCardFilterRequest;
import com.innowise.userservice.dto.request.PaymentCardRequest;
import com.innowise.userservice.dto.response.PaymentCardResponse;
import com.innowise.userservice.entity.PaymentCard;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.exception.AmountOfCardsException;
import com.innowise.userservice.exception.PaymentCardNotFoundException;
import com.innowise.userservice.exception.UserNotFoundException;
import com.innowise.userservice.mapper.PaymentCardMapper;
import com.innowise.userservice.repository.PaymentCardRepository;
import com.innowise.userservice.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

@ExtendWith(MockitoExtension.class)
class PaymentCardServiceImplTests {

  @Mock
  private PaymentCardRepository paymentCardRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PaymentCardMapper paymentCardMapper;

  @InjectMocks
  private PaymentCardServiceImpl paymentCardService;

  private User user;
  private PaymentCard paymentCard;
  private PaymentCardRequest paymentCardRequest;
  private PaymentCardResponse paymentCardResponse;

  @BeforeEach
  void setup() {
    user = new User();
    user.setId(1L);

    paymentCard = new PaymentCard();
    paymentCard.setId(1L);
    paymentCard.setUser(user);
    paymentCard.setActive(true);

    paymentCardRequest = new PaymentCardRequest();
    paymentCardResponse = new PaymentCardResponse();
  }

  @Test
  void create_whenUserExistsAndCardUnderLimit_shouldCreateCard() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(paymentCardRepository.countByUserIdAndActiveTrue(1L)).thenReturn(0L);
    when(paymentCardMapper.fromCardRequestToCard(paymentCardRequest)).thenReturn(paymentCard);
    when(paymentCardRepository.save(paymentCard)).thenReturn(paymentCard);
    when(paymentCardMapper.fromCardToCardResponse(paymentCard)).thenReturn(paymentCardResponse);

    PaymentCardResponse result = paymentCardService.create(1L, paymentCardRequest);

    assertNotNull(result);
    assertEquals(paymentCardResponse, result);
    verify(paymentCardRepository).save(paymentCard);
  }
  @Test
  void create_whenInvalidCardData_shouldThrowException() {
    paymentCardRequest.setNumber("123");

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(paymentCardRepository.countByUserIdAndActiveTrue(1L)).thenReturn(0L);
    when(paymentCardMapper.fromCardRequestToCard(paymentCardRequest))
        .thenThrow(IllegalArgumentException.class);

    assertThrows(IllegalArgumentException.class, () ->
        paymentCardService.create(1L, paymentCardRequest));

    verify(paymentCardRepository, never()).save(any());
  }

  @Test
  void createCard_whenUserNotFound_shouldUserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> paymentCardService.create(1L, paymentCardRequest));

    verify(paymentCardRepository, never()).save(any());
  }

  @Test
  void createCard_whenCardLimitExceeded_shouldAmountOfCards() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(paymentCardRepository.countByUserIdAndActiveTrue(1L)).thenReturn(5L);

    assertThrows(AmountOfCardsException.class,
        () -> paymentCardService.create(1L, paymentCardRequest));

    verify(paymentCardRepository, never()).save(any());
  }


  @Test
  void findCardById_whenCardExists_shouldReturnCardResponse() {
    when(paymentCardRepository.findById(1L)).thenReturn(Optional.of(paymentCard));
    when(paymentCardMapper.fromCardToCardResponse(paymentCard)).thenReturn(paymentCardResponse);

    PaymentCardResponse result = paymentCardService.findById(1L);

    assertNotNull(result);
    assertEquals(paymentCardResponse, result);
  }

  @Test
  void findCardById_whenCardNotFound_shouldPaymentCardNotFound() {
    when(paymentCardRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(PaymentCardNotFoundException.class, () -> paymentCardService.findById(1L));
  }
  @Test
  void findAll_whenFilterRequestProvided_shouldReturnPageOfCards() {
    PaymentCardFilterRequest request = new PaymentCardFilterRequest();
    request.setPage(0);
    request.setSize(10);

    when(paymentCardRepository.findAll(
        any(org.springframework.data.jpa.domain.Specification.class),
        any(org.springframework.data.domain.Pageable.class)
    )).thenReturn(org.springframework.data.domain.Page.empty());

    var result = paymentCardService.findAll(request);

    assertNotNull(result);
    assertEquals(0, result.getTotalElements());
  }

  @Test
  void findAll_shouldReturnPage() {
    PaymentCardFilterRequest request = new PaymentCardFilterRequest();
    request.setPage(0);
    request.setSize(10);

    when(paymentCardRepository.findAll(
        any(org.springframework.data.jpa.domain.Specification.class),
        any(org.springframework.data.domain.Pageable.class)
    )).thenReturn(Page.empty());

    Page<PaymentCardResponse> result = paymentCardService.findAll(request);

    assertNotNull(result);
  }

  @Test
  void findAllByUserId_whenUserExists_shouldReturnListOfCards() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(paymentCardRepository.findAllByUserId(1L)).thenReturn(List.of(paymentCard));
    when(paymentCardMapper.fromCardToCardResponse(paymentCard)).thenReturn(paymentCardResponse);

    List<PaymentCardResponse> result = paymentCardService.findAllByUserId(1L);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(paymentCardResponse, result.getFirst());
  }

  @Test
  void findAllByUserId_whenUserNotFound_shouldUserNotFoundException() {
    when(userRepository.findById(1L))
        .thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> paymentCardService
        .findAllByUserId(1L));
  }

  @Test
  void deactivate_whenCardExistsAndActive_shouldDeactivateCard() {
    when(paymentCardRepository
        .findByIdAndUserId(1L, 1L))
        .thenReturn(Optional.of(paymentCard));

    paymentCardService.deactivate(1L, 1L);

    assertFalse(paymentCard.getActive());
    verify(paymentCardRepository).save(paymentCard);
  }

  @Test
  void deactivate_whenCardNotFound_shouldPaymentCardNotFoundException() {
    when(paymentCardRepository
        .findByIdAndUserId(1L, 1L))
        .thenReturn(Optional.empty());

    assertThrows(PaymentCardNotFoundException.class, () -> paymentCardService
        .deactivate(1L, 1L));
    verify(paymentCardRepository, never()).save(any());
  }

  @Test
  void delete_whenCardExists_shouldDeleteCard() {
    when(paymentCardRepository
        .findByIdAndUserId(1L, 1L))
        .thenReturn(Optional.of(paymentCard));

    paymentCardService.delete(1L, 1L);

    verify(paymentCardRepository).delete(paymentCard);
  }

  @Test
  void delete_whenCardNotFound_shouldPaymentCardNotFound() {
    when(paymentCardRepository
        .findByIdAndUserId(1L, 1L))
        .thenReturn(Optional.empty());

    assertThrows(PaymentCardNotFoundException.class,
        () -> paymentCardService.delete(1L, 1L));
    verify(paymentCardRepository, never()).delete((PaymentCard) any());
  }


}
