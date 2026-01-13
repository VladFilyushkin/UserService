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

public class UserResponse {
  private String name;
  private String surname;
  private LocalDateTime birthDate;
  private String email;

}
