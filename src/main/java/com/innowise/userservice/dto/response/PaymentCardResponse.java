package com.innowise.userservice.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentCardResponse {

  private String number;
  private String holder;
  private LocalDateTime expirationDate;
  private Boolean active;

}
