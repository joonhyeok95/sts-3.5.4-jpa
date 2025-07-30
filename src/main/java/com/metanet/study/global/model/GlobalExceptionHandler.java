package com.metanet.study.global.model;

import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.metanet.study.global.domain.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleException(Exception e,
      HttpServletRequest request, Locale locale) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    ApiResponse<Object> response = new ApiResponse<>(status, status.getReasonPhrase(),
        e.getMessage(), locale, request.getRequestURI(), null);
    return new ResponseEntity<>(response, status);
  }
}
