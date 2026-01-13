package com.innowise.userservice.service.impl;

import com.innowise.userservice.dto.request.PaymentCardFilterRequest;
import com.innowise.userservice.dto.request.PaymentCardRequest;
import com.innowise.userservice.dto.response.PaymentCardResponse;
import com.innowise.userservice.exception.AmountOfCardsException;
import com.innowise.userservice.exception.PaymentCardNotFoundException;
import com.innowise.userservice.exception.UserNotFoundException;
import com.innowise.userservice.entity.PaymentCard;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.mapper.PaymentCardMapper;
import com.innowise.userservice.repository.PaymentCardRepository;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.service.PaymentCardService;

import com.innowise.userservice.service.specification.PaymentCardSpecification;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCardServiceImpl implements PaymentCardService {

  private static final int MAX_CARDS_PER_USER = 5;

  private final PaymentCardRepository paymentCardRepository;
  private final UserRepository userRepository;
  private final PaymentCardMapper paymentCardMapper;


  @Override
  @Transactional
  public PaymentCardResponse create(Long userId, PaymentCardRequest request) {
    User user = userRepository.findById(userId).orElseThrow(() ->
        new UserNotFoundException(userId));

    Long activeCardAmount = paymentCardRepository.countByUserIdAndActiveTrue(userId);
    if(activeCardAmount >= MAX_CARDS_PER_USER) {
      throw new AmountOfCardsException(MAX_CARDS_PER_USER);
    }
    PaymentCard paymentCard = paymentCardMapper.fromCardRequestToCard(request);
    paymentCard.setUser(user);
    paymentCard.setActive(true);
    PaymentCard savedCard = paymentCardRepository.save(paymentCard);
    return paymentCardMapper.fromCardToCardResponse(savedCard);
  }

  @Override
  public PaymentCardResponse findById(Long id) {
    PaymentCard paymentCard = paymentCardRepository.findById(id)
        .orElseThrow(() -> new PaymentCardNotFoundException(id));
    return paymentCardMapper.fromCardToCardResponse(paymentCard);
  }

  @Override
  public List<PaymentCardResponse> findAllByUserId(Long userId) {
    userRepository.findById(userId).orElseThrow(() ->
        new UserNotFoundException(userId));
    List<PaymentCard> listOfCards = paymentCardRepository.findAllByUserId(userId);
    return listOfCards.stream().map(paymentCardMapper::fromCardToCardResponse).toList();
  }


    public Page<PaymentCardResponse> findAll(PaymentCardFilterRequest request) {
      Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
      Specification<PaymentCard> specification = PaymentCardSpecification.filterByNumberAndStatus(
          request.getNumber(),
          request.getActive()
      );
      return paymentCardRepository.findAll(specification, pageable)
          .map(paymentCardMapper::fromCardToCardResponse);
  }

  @Transactional
  @Override
  public void deactivate(Long userId, Long cardId) {
    PaymentCard card = paymentCardRepository
        .findByIdAndUserId(cardId, userId)
        .orElseThrow(() ->
            new PaymentCardNotFoundException(cardId));
    if (!card.getActive()) {
      return;
    }
    card.setActive(false);
    paymentCardRepository.save(card);
  }

  @Override
  public void delete(Long userId, Long cardId) {
    PaymentCard card = paymentCardRepository.findByIdAndUserId(cardId, userId)
        .orElseThrow(() -> new PaymentCardNotFoundException(cardId));
    paymentCardRepository.delete(card);
  }
}
