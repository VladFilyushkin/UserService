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

public class UserResponse implements Serializable {
  private String name;
  private String surname;
  private LocalDateTime birthDate;
  private String email;

}
