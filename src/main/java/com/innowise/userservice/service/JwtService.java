package com.innowise.userservice.service;

import io.jsonwebtoken.Claims;

public interface JwtService {

  Claims extractAllClaims(String token);

}
