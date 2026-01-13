package com.innowise.userservice.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentCardResponse implements Serializable {
  private Long id;
  private String number;
  private String holder;
  private LocalDateTime expirationDate;
  private Boolean active;

}
