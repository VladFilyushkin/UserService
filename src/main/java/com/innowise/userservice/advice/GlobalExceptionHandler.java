package com.innowise.userservice.advice;

import com.innowise.userservice.exception.AmountOfCardsException;
import com.innowise.userservice.exception.PaymentCardNotFoundException;
import com.innowise.userservice.exception.UserAlreadyExistsException;
import com.innowise.userservice.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
  }

  @ExceptionHandler(PaymentCardNotFoundException.class)
  public ResponseEntity<String> handleCardNotFound(PaymentCardNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException ex) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ex.getMessage());
  }

  @ExceptionHandler(AmountOfCardsException.class)
  public ResponseEntity<String> handleAmountOfCards(AmountOfCardsException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ex.getMessage());
  }
}

