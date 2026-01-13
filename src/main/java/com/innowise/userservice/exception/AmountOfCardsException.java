package com.innowise.userservice.exception;

public class AmountOfCardsException extends RuntimeException {

  public AmountOfCardsException(int cardAmount) {
    super("Amount of your active cards can't be more than" + cardAmount);
  }
}
