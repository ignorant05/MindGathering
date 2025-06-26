package com.api.backend.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.lang.NonNull;
import com.api.backend.services.UserDetService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {
  final ArrayList<String> PUBLIC_PATHS = new ArrayList<>(Arrays.asList("/api/v1/auth/login", "/api/v1/auth/register"));

  private final JwtUtils jwtUtils;
  private final UserDetService userDetailsServ;

  public JwtFilter(UserDetService userDetailsServ, JwtUtils jwtUtils) {
    this.jwtUtils = jwtUtils;
    this.userDetailsServ = userDetailsServ;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws IOException, ServletException {

    String path = request.getServletPath();

    if (PUBLIC_PATHS.contains(path)) {
      filterChain.doFilter(request, response);
      return;
    }
    try {
      String authHeader = request.getHeader("Authorization");

      final String access_token;
      String username;

      if (authHeader == null || !authHeader.startsWith("Bearer")) {
        filterChain.doFilter(request, response);
        return;
      }

      access_token = parseJwt(request);
      username = jwtUtils.extractUsername(access_token);

      if (!username.isEmpty() &&
          SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsServ.loadUserByUsername(username);
        System.out.println("Loaded userDetails class: " + userDetails.getClass().getName());

        if (jwtUtils.validateJwtToken(access_token)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid or expired token" + e);
      return;
    }

    filterChain.doFilter(request, response);
  }

  public String parseJwt(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    return null;
  }
}
