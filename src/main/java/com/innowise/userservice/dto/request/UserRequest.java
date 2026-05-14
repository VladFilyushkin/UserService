package com.innowise.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserRequest {

  @NotBlank(message = "Name can't be empty")
  private String name;
  @NotBlank(message = "Surname can't be empty")
  private String surname;
  @NotNull(message = "Birth date can't be empty")
  @Past(message = "Birth date should be in the past")
  private LocalDateTime birthDate;
  @Email(message = "Email must be valid")
  @NotBlank(message = "Email can't be empty")
  private String email;


}
