package com.innowise.userservice.service;

import com.innowise.userservice.dto.request.PaymentCardFilterRequest;
import com.innowise.userservice.dto.request.PaymentCardRequest;
import com.innowise.userservice.dto.response.PaymentCardResponse;
import com.innowise.userservice.entity.PaymentCard;
import java.util.List;
import org.springframework.data.domain.Page;

public interface PaymentCardService {

  PaymentCardResponse findById(Long id);
  PaymentCardResponse create(Long userId, PaymentCardRequest request);
  List<PaymentCardResponse> findAllByUserId(Long userId);
  Page<PaymentCardResponse> findAll(PaymentCardFilterRequest request);
  void deactivate(Long userId, Long cardId);
  void delete(Long userId, Long cardId);

}
