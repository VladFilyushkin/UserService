package com.innowise.userservice.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentCardRequest {

  @NotBlank
  @Pattern(regexp = "\\d{13,19}", message = "Card number must be 13 to 19 digits")
  private String number;
  @NotBlank(message = "Card holder can't be empty")
  private String holder;
  @NotNull(message = "Expiration date can't be empty")
  @Future(message = "Expiration date must be in the future")
  private LocalDateTime expirationDate;


}
