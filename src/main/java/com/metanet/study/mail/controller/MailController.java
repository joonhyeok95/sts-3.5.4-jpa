package com.metanet.study.mail.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.metanet.study.mail.service.MailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {
  private final MailService mailService;

  // ResponseEntity 를 활용하기
  @GetMapping
  public ResponseEntity<?> getAllUsers(HttpServletRequest request) {

    String to = "test@example.com";
    String subject = "Test Subject";
    Map<String, Object> variables = new HashMap<>();
    variables.put("name", "Tester");
    variables.put("message", "Mail Send Testing...");

    // 실제 호출
    mailService.sendMail(to, subject, variables);
    return ResponseEntity.ok(null);
  }
}
