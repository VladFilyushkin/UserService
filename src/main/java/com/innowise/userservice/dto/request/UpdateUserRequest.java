package com.innowise.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserRequest {

  private String name;
  private String surname;

  @Past(message = "Birth date should be in the past")
  private LocalDateTime birthDate;

  @Email(message = "Email must be valid")
  private String email;
}
