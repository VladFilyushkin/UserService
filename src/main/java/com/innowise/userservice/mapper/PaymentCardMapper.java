package com.innowise.userservice.mapper;

import com.innowise.userservice.dto.request.PaymentCardRequest;
import com.innowise.userservice.dto.response.PaymentCardResponse;
import com.innowise.userservice.entity.PaymentCard;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PaymentCardMapper {

  PaymentCard fromCardRequestToCard(PaymentCardRequest request);
  PaymentCardResponse fromCardToCardResponse(PaymentCard card);

}
