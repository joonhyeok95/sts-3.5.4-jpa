package com.metanet.study.global.domain;

import java.time.ZonedDateTime;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

  private ZonedDateTime timestamp; // 응답 생성 시간
  private int status; // HTTP 상태 코드
  private String error; // 상태 에러 메시지(예: "BAD_REQUEST")
  private String message; // 추가 사용자 메시지(선택사항)
  private Locale locale; // Locale 정보
  private String path; // 요청 경로
  private T result; // 실제 응답 데이터(Payload)

  public ApiResponse() {
    this.timestamp = ZonedDateTime.now();
  }

  public ApiResponse(HttpStatus status, String error, String message, Locale locale, String path,
      T result) {
    this.timestamp = ZonedDateTime.now();
    this.status = status.value();
    this.error = error;
    this.message = message;
    this.locale = locale;
    this.path = path;
    this.result = result;
  }
}
