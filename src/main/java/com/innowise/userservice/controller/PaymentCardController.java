  package com.innowise.userservice.controller;

  import com.innowise.userservice.dto.request.PaymentCardFilterRequest;
  import com.innowise.userservice.dto.request.PaymentCardRequest;
  import com.innowise.userservice.dto.response.PaymentCardResponse;
  import com.innowise.userservice.service.PaymentCardService;
  import jakarta.validation.Valid;
  import java.util.List;
  import lombok.RequiredArgsConstructor;
  import org.springframework.data.domain.Page;
  import org.springframework.http.HttpStatus;
  import org.springframework.http.ResponseEntity;
  import org.springframework.security.access.prepost.PreAuthorize;
  import org.springframework.web.bind.annotation.DeleteMapping;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.ModelAttribute;
  import org.springframework.web.bind.annotation.PatchMapping;
  import org.springframework.web.bind.annotation.PathVariable;
  import org.springframework.web.bind.annotation.PostMapping;
  import org.springframework.web.bind.annotation.RequestBody;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RestController;

  @RestController
  @RequiredArgsConstructor
  @RequestMapping("/api/payment-cards")
  public class PaymentCardController {

    private final PaymentCardService paymentCardService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentCardResponse>  getCardById(@PathVariable Long id) {
      return ResponseEntity.ok(paymentCardService.findById(id));
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
     @PostMapping("/users/{userId}")
     public ResponseEntity<PaymentCardResponse>  createCard(@PathVariable Long userId,
         @RequestBody @Valid PaymentCardRequest request) {
       return ResponseEntity
           .status(HttpStatus.CREATED)
           .body(paymentCardService.create(userId, request));
     }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<PaymentCardResponse>>  getAllCardsByUserId(@PathVariable Long userId) {
      return ResponseEntity.ok(paymentCardService.findAllByUserId(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<PaymentCardResponse>>  getAllCards(
        @Valid @ModelAttribute PaymentCardFilterRequest filterRequest) {
      return ResponseEntity.ok(paymentCardService.findAll(filterRequest));
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping("/users/{userId}/{cardId}/deactivate")
    public ResponseEntity<Void> deactivateCard(@PathVariable Long userId,
        @PathVariable Long cardId) {
        paymentCardService.deactivate(userId, cardId);
      return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/users/{userId}/{cardId}")
    public ResponseEntity<Void> deleteCard(
        @PathVariable Long userId,
        @PathVariable Long cardId) {
      paymentCardService.delete(userId, cardId);
      return ResponseEntity.noContent().build();
    }
  }
