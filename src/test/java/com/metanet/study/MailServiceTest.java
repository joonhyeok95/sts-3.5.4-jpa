package com.metanet.study;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import com.metanet.study.mail.service.MailService;
import jakarta.mail.internet.MimeMessage;

@SpringBootTest
public class MailServiceTest {

  @Autowired
  private JavaMailSender mailSender;
  @Autowired
  private SpringTemplateEngine templateEngine;
  @Autowired
  private MailService mailService;
  private MimeMessage mimeMessage;

  /*
   * Mock 주입시 Template 를 가져올 수 없음
   */
  // @BeforeEach
  // public void setUp() {
  // mailSender = mock(JavaMailSender.class);
  // templateEngine = mock(SpringTemplateEngine.class);
  // mailService = new MailService(mailSender, templateEngine);
  //
  // mimeMessage = mock(MimeMessage.class);
  // when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
  // }
  /*
   * Mock 주입으로 기능테스트하는 경우 활용
   */
  // @Test
  // public void testSendMail() throws MessagingException {
  // // Thymeleaf templateEngine.process가 호출되면 간단한 HTML 반환하도록 mocking
  // when(templateEngine.process(eq("email/email-template"), any(Context.class)))
  // .thenReturn("No Template!!");
  //
  // String to = "test@example.com";
  // String subject = "Test Subject";
  // Map<String, Object> variables = new HashMap<>();
  // variables.put("name", "Tester");
  // variables.put("message", "Mail Send Testing...");
  //
  // // 실제 호출
  // mailService.sendMail(to, subject, variables);
  //
  // // MimeMessageHelper 생성 시 MimeMessage 객체가 그대로 넘어갔는지 확인
  // verify(mailSender).send(mimeMessage);
  //
  // }

  /*
   * 타임리프 엔진 로딩 체크
   */
  @Test
  public void testTemplateRendering() {
    // 실제 variables 세팅 (이전에 쓰던 values 사용)
    Map<String, Object> variables = new HashMap<>();
    variables.put("name", "Tester");
    variables.put("message", "Mail Send Testing...");

    Context context = new Context();
    context.setVariables(variables);

    // templateEngine은 @Autowired 혹은 테스트에서 주입받은 실제 빈이어야 함
    String renderedHtml = templateEngine.process("email/email-template", context);

    System.out.println("Rendered Template HTML:\n" + renderedHtml);
  }

  /*
   * 템플릿 파일 조회
   */
  @Test
  public void testListTemplateFiles() throws IOException {
    ClassPathResource templatesDir = new ClassPathResource("templates/email/");

    if (templatesDir.exists() && templatesDir.getFile().isDirectory()) {
      File[] files = templatesDir.getFile().listFiles();
      System.out.println("Templates in email folder:");
      if (files != null) {
        for (File file : files) {
          System.out.println("- " + file.getName());
        }
      }
    } else {
      System.out.println("Template folder does not exist or is not a directory.");
    }
  }

}
