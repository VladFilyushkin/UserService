package com.innowise.userservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserFilterRequest {

  private String name;
  private String surname;
  @Min(value = 0, message = "Page must be at least 0")
  private int page = 0;
  @Min(value = 1, message = "Minimum page size is 1")
  @Max(value = 50, message = "Maximum page size is 50")
  private int size = 10;

}
