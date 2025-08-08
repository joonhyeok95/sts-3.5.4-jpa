package com.metanet.study;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LombdaCaseTest {

  @Test
  public void case1() {

    // given
    List<String> users = List.of("Alice", "Bob", "Charlie");

    // when
    users.forEach(u -> System.out.println("User: " + u));
    users.stream().filter(u -> u.startsWith("A")).collect(Collectors.toList());

    // then

  }

  // @Test
  // public void case2() {
  // Department dept = new Department(1L, "1", null);
  // Role admin = new Role(1L, "ADMIN", null);
  // // UserRole 객체 생성 (중간조인 테이블 역할)
  // UserRole userRoleAdmin = new UserRole(); // 아래 User 생성 후
  // userRoleAdmin.setId(1L);
  // userRoleAdmin.setRole(admin);
  // User user = new User(1L, "Alice", "aem@email.com", dept, null); // new
  // userRoleAdmin.setUser(user);
  // Set<UserRole> userRoles = new HashSet<UserRole>();
  // userRoles.add(userRoleAdmin);
  // user.setUserRoles(userRoles);
  // List<User> users = new ArrayList<>();
  // // // 람다/스트림을 이용한 중간조인으로 ADMIN 권한 유저만 추출
  // List<String> adminUserNames = users.stream()
  // .filter(u -> userRoles.stream().filter(ur -> ur.getUserId().equals(user.getId()))
  // .map(ur -> roles.stream().filter(role -> role.getId().equals(ur.getRoleId()))
  // .findFirst().map(Role::getRoleName).orElse(""))
  // .anyMatch(roleName -> roleName.equals("ADMIN")))
  // .map(User::getName).collect(Collectors.toList());
  //
  // adminUserNames.forEach(name -> System.out.println("ADMIN: " + name));
  // }
}
