package com.innowise.userservice.exception;

public class ExpiredTokenException extends  RuntimeException  {

  public ExpiredTokenException() {
    super("Token is expired");
  }

}
