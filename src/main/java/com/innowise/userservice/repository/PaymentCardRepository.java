package com.innowise.userservice.repository;

import com.innowise.userservice.entity.PaymentCard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard,Long>,
    JpaSpecificationExecutor<PaymentCard> {

  Long countByUserIdAndActiveTrue(Long userId);
  List<PaymentCard> findAllByUserId(Long userId);
  Optional<PaymentCard> findByIdAndUserId(Long cardId, Long userId);

}
