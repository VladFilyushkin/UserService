package com.innowise.userservice.configuration;


import com.innowise.userservice.exception.ExpiredTokenException;
import com.innowise.userservice.exception.InvalidTokenException;
import com.innowise.userservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@RequiredArgsConstructor
@EnableMethodSecurity
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Missing Authorization header");
      return;
    }

    String token = authHeader.substring(7);

    try {
      var claims = jwtService.extractAllClaims(token);

      if (!"ACCESS".equals(claims.get("type", String.class))) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("Invalid token type");
        return;
      }

      String login = claims.getSubject();
      String role = claims.get("role", String.class);

      var authority = new SimpleGrantedAuthority("ROLE_" + role);

      UsernamePasswordAuthenticationToken auth =
          new UsernamePasswordAuthenticationToken(
              login,
              null,
              List.of(authority)
          );

      SecurityContextHolder.getContext().setAuthentication(auth);

    } catch (ExpiredTokenException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Token expired");
      return;
    } catch (InvalidTokenException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid token");
      return;
    }

    filterChain.doFilter(request, response);
  }
}
