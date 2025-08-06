package com.metanet.study;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Jdk17UtilTest {

  @Test
  public void stringUtilTest() {
    String a = "me ta";
    String multiline = """
        안녕하시오.
        두번째 줄
        """;
    String str1 = "Hello";
    String str2 = "hello";
    String s = " Hello World \nThis is Java ";

    // 문자열 길이 및 비어있는지
    log.info("length(): {}", str1.length());
    log.info("isEmpty(): {}", str1.isEmpty());
    log.info("isEmpty(\"\"): {}", "".isEmpty());

    // 특정 위치 문자 및 유니코드 코드포인트
    log.info("charAt(1): {}", str1.charAt(1)); // 'e'
    log.info("codePointAt(1): {}", str1.codePointAt(1));
    log.info("codePoints():");
    str1.codePoints().forEach(cp -> log.info("  codePoint: {}, char: {}", cp, (char) cp));
    log.info("offsetByCodePoints(0,3): {}", str1.offsetByCodePoints(0, 3));

    // 비교 및 확인
    log.info("equals(str2): {}", str1.equals(str2));
    log.info("equalsIgnoreCase(str2): {}", str1.equalsIgnoreCase(str2));
    log.info("compareTo(str2): {}", str1.compareTo(str2));
    log.info("compareToIgnoreCase(str2): {}", str1.compareToIgnoreCase(str2));

    log.info("startsWith(\"He\"): {}", str1.startsWith("He"));
    log.info("endsWith(\"lo\"): {}", str1.endsWith("lo"));
    log.info("contains(\"ell\"): {}", str1.contains("ell"));
    log.info("matches(\"[A-Za-z]+\") : {}", str1.matches("[A-Za-z]+"));
    log.info("matches(\"H.*o\") : {}", str1.matches("H.*o"));

    // 위치 탐색
    log.info("indexOf('o'): {}", s.indexOf('o'));
    log.info("indexOf(\"World\"): {}", s.indexOf("World"));
    log.info("lastIndexOf('l'): {}", s.lastIndexOf('l'));
    log.info("lastIndexOf(\"is\"): {}", s.lastIndexOf("is"));
    log.info("regionMatches(6, \"World\", 0, 5): {}", s.regionMatches(6, "World", 0, 5));

    // 부분 문자열/조작
    log.info("substring(1, 6): {}", s.substring(1, 6));
    log.info("subSequence(1, 6): {}", s.subSequence(1, 6));
    log.info("concat(\"!!!\"): {}", s.concat("!!!"));
    log.info("repeat(2): {}", "abc".repeat(2));
    log.info("lines():");
    s.lines().forEach(line -> log.info(" - {}", line));

    // 대/소문자 변환 및 트림
    log.info("toLowerCase(): {}", s.toLowerCase());
    log.info("toUpperCase(Locale.US): {}", s.toUpperCase(Locale.US));
    log.info("trim(): [{}]", s.trim());
    log.info("strip(): [{}]", s.strip());
    log.info("stripLeading(): [{}]", s.stripLeading());
    log.info("stripTrailing(): [{}]", s.stripTrailing());
    log.info("isBlank(): {}", "   ".isBlank());

    // 분할/조합
    log.info("split(\" \") limit 3:");
    for (String part : s.split(" ", 3)) {
      log.info("  > {}", part);
    }
    log.info("join('-', \"Java\", \"is\", \"fun\"): {}", String.join("-", "Java", "is", "fun"));

    // 치환
    log.info("replace('l', 'L'): {}", s.replace('l', 'L'));
    log.info("replaceAll(\"\\s+\", \"-\"): {}", s.replaceAll("\\s+", "-"));
    log.info("replaceFirst(\"\\s+\", \"-\"): {}", s.replaceFirst("\\s+", "-"));

    if (System.getProperty("java.version").compareTo("15") >= 0) {
      log.info("translateEscapes(): {}", "Hello\\nWorld".translateEscapes());
    }

    // 변환/출력
    char[] chars = s.toCharArray();
    log.info("toCharArray()[0]: {}", chars[0]);
    log.info("getBytes(StandardCharsets.UTF_8).length: {}",
        s.getBytes(StandardCharsets.UTF_8).length);

    log.info("format(\"Hello %s!\", \"JUnit\"): {}", String.format("Hello %s!", "JUnit"));
    log.info("formatted(\"JUnit\"): {}", "Hello %s!".formatted("JUnit"));
  }

  @Test
  public void objectUtilsMethodsTest() {
    Object obj1 = null;
    Object obj2 = "Hello";
    Object obj3 = "Hello";
    Object obj4 = "World";

    // Objects.isNull / nonNull
    log.info("Objects.isNull(obj1): {}", Objects.isNull(obj1)); // true
    log.info("Objects.nonNull(obj2): {}", Objects.nonNull(obj2)); // true

    // Objects.equals (null-safe)
    log.info("Objects.equals(obj2, obj3): {}", Objects.equals(obj2, obj3)); // true
    log.info("Objects.equals(obj2, obj4): {}", Objects.equals(obj2, obj4)); // false
    log.info("Objects.equals(obj1, obj2): {}", Objects.equals(obj1, obj2)); // false
    log.info("Objects.equals(obj1, null): {}", Objects.equals(obj1, null)); // true

    // Objects.requireNonNull / requireNonNullElse / requireNonNullElseGet
    try {
      Objects.requireNonNull(obj1, "obj1 is null!"); // 예외 발생
    } catch (NullPointerException e) {
      log.error("Caught exception: {}", e.getMessage());
    }

    // 기본값 대체
    String result1 = Objects.requireNonNullElse((String) obj1, "default");
    log.info("requireNonNullElse(obj1, \"default\"): {}", result1);

    String result2 = Objects.requireNonNullElseGet((String) obj1, () -> "generatedDefault");
    log.info("requireNonNullElseGet(obj1, supplier): {}", result2);

    // hashCode (null-safe)
    log.info("Objects.hashCode(obj2): {}", Objects.hashCode(obj2));
    log.info("Objects.hashCode(obj1): {}", Objects.hashCode(obj1)); // 0

    // toString (null-safe)
    log.info("Objects.toString(obj2): {}", Objects.toString(obj2));
    log.info("Objects.toString(obj1): {}", Objects.toString(obj1)); // "null"
    log.info("Objects.toString(obj1, \"empty\"): {}", Objects.toString(obj1, "empty"));
  }

  @Test
  public void dateTest() {
    LocalDate today = LocalDate.now();
    LocalDate nextWeek = today.plusWeeks(1);
    log.info("오늘:{}, 다음주{}", today, nextWeek.format(DateTimeFormatter.ISO_DATE));
    long daysBetween = ChronoUnit.DAYS.between(today, nextWeek);
    log.info("오늘 다음주 요일 차이:{}", String.valueOf(daysBetween)); // 항상 7
  }
}
