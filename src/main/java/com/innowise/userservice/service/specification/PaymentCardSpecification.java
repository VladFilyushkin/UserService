package com.innowise.userservice.service.specification;

import com.innowise.userservice.entity.PaymentCard;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class PaymentCardSpecification {

  private PaymentCardSpecification() {
    throw new IllegalStateException("Utility class");
  }

  public static Specification<PaymentCard> filterByNumberAndStatus(String number, Boolean active) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (number != null && !number.isBlank()) {
        predicates.add(
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("number")),
                "%" + number.toLowerCase() + "%"
            )
        );
      }

      if (active != null) {
        predicates.add(criteriaBuilder.equal(root.get("active"), active));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }



}
