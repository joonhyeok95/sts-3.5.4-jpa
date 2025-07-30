package com.metanet.study.global.model;

import java.util.Locale;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.metanet.study.global.domain.ApiResponse;

@RestControllerAdvice
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType, Class converterType) {
    // 예를 들어 ApiResponse 타입이면 중복 래핑 방지
    return !ApiResponse.class.isAssignableFrom(returnType.getParameterType());
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request,
      ServerHttpResponse response) {

    int statusCode = 200; // 기본값
    if (response instanceof ServletServerHttpResponse servletResponse) {
      statusCode = servletResponse.getServletResponse().getStatus();
    }

    // Locale 등은 필요에 따라 request에서 추출
    Locale locale = Locale.getDefault();
    String path = request.getURI().getPath();

    // 예시: HttpServletRequest 추출도 가능
    // HttpServletRequest servletRequest =
    // ((ServletServerHttpRequest) request).getServletRequest();

    return new ApiResponse<>(HttpStatus.valueOf(statusCode),
        HttpStatus.valueOf(statusCode).getReasonPhrase(), null, // or "Success"/"Error" 등 상황별로
        locale, path, body);
  }
}
