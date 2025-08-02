package com.metanet.study.mail.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.metanet.study.mail.dto.Product;
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
    // Product 상품 리스트
    List<Product> products =
        Arrays.asList(new Product("사과", 3000), new Product("바나나", 2000), new Product("오렌지", 2500));

    String to = "test@example.com";
    String subject = "Test Subject";
    Map<String, Object> variables = new HashMap<>();
    variables.put("name", "Tester");
    variables.put("message", "Mail Send Testing...");
    variables.put("products", products);

    // 실제 호출
    mailService.mailTemplateBinding(to, subject, variables);
    return ResponseEntity.ok(null);
  }
}
