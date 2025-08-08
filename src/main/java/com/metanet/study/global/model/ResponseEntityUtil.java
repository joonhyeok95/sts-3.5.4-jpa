package com.metanet.study.global.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.metanet.study.global.domain.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

public class ResponseEntityUtil {

  public static <T> ResponseEntity<ApiResponse<T>> buildResponse(T data, HttpStatus status,
      String message, HttpServletRequest request) {
    ApiResponse<T> response = new ApiResponse<>(status, status.getReasonPhrase(), message,
        request.getLocale(), request.getRequestURI(), data);
    return new ResponseEntity<>(response, status);
  }
}
