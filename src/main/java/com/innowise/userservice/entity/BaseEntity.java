package com.innowise.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

  @Column(name = "created_at")
  @CreatedDate
  private LocalDateTime createdAt;
  @Column(name = "updated_at")
  @LastModifiedDate
  private LocalDateTime updatedAt;

}
