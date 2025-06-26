package com.api.backend.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.api.backend.utils.AuthEntryPoint;
import com.api.backend.utils.JwtFilter;
import com.api.backend.services.UserDetService;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class webSecurityConfiguration {
  private AuthEntryPoint aEntryPoint;
  private JwtFilter jwtFilter;
  private UserDetService userDetailsServ;

  public webSecurityConfiguration(
      UserDetService userDetailsServ,
      AuthEntryPoint aEntryPoint,
      JwtFilter jwtFilter) {
    this.userDetailsServ = userDetailsServ;
    this.aEntryPoint = aEntryPoint;
    this.jwtFilter = jwtFilter;
  }
  //
  // @Bean
  // public AuthenticationManager authManager(HttpSecurity httpSec) throws
  // Exception {
  // AuthenticationManagerBuilder authenticationManagerBuilder = httpSec
  // .getSharedObject(AuthenticationManagerBuilder.class);
  // authenticationManagerBuilder
  // .userDetailsService(userDetailsServ)
  // .passwordEncoder(passwordEncoder());
  //
  // return authenticationManagerBuilder.build();
  // }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
            .configurationSource(corsConfigurationSource()))
        .csrf(
            csrf -> csrf.disable())
        .httpBasic(
            httpBasic -> httpBasic.disable())
        .authorizeHttpRequests(
            authHttpRequests -> authHttpRequests
                .anyRequest().permitAll())
        .userDetailsService(userDetailsServ)
        .exceptionHandling(
            eh -> eh.authenticationEntryPoint(aEntryPoint))
        .sessionManagement(
            sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(List.of("http://localhost:8080",
        "http://localhost"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
