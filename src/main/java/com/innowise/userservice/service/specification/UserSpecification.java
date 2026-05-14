package com.innowise.userservice.service.specification;

import com.innowise.userservice.dto.response.UserResponse;
import com.innowise.userservice.entity.User;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

  private UserSpecification() {
    throw new IllegalStateException("Utility class");
  }

  public static Specification<User> filterByNameAndSurname(String name, String surname) {
    return((root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if(name !=null && !name.isBlank()){
        predicates.add(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%"+name.toLowerCase()+"%"));
      }
      if(surname !=null && !surname.isBlank()){
        predicates.add(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")),
                "%"+surname.toLowerCase()+"%"));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    });
  }
}
