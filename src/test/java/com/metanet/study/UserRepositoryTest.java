package com.metanet.study;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.metanet.study.dept.domain.model.Department;
import com.metanet.study.role.domain.model.Role;
import com.metanet.study.role.domain.repository.RoleRepository;
import com.metanet.study.user.domain.model.User;
import com.metanet.study.user.domain.model.UserRole;
import com.metanet.study.user.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest // JPA 관련 컴포넌트 로딩, @Transactional 적용
// @SpringBootTest // 통합적인 테스트 시 사용
// @Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;


  @Test
  void testInsertAndFind() {
    // given
    Department dept = new Department();
    dept.setId(100L);
    User user = User.builder().name("John Doe").email("john@example.com").department(dept).build();
    User id = userRepository.save(user);

    // when
    Optional<User> users = userRepository.findById(id.getId());

    // then
    assertThat(users).isNotEmpty();
    assertThat(users.get().getName()).isEqualTo("John Doe");
    assertThat(users.get().getEmail()).isEqualTo("john@example.com");
  }

  @Test
  void testSearchCases() {
    // given
    List<User> user1 = userRepository.findByNameContaining("임준혁23");
    List<User> user2 = userRepository.findByNameLike("%준%");
    List<User> user3 = userRepository.findByNameStartingWith("임");
    List<User> user4 = userRepository.findByNameEndingWith("혁");

    // when

    // then
  }

  /*
   * 특정 사용자를 검색하여 할당된 role의 명칭을 구한다.
   * 
   * select r1_0.id,r1_0.name from role r1_0 where r1_0.id in
   * (3,1,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,
   * NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,
   * NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,
   * NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,
   * NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,
   * NULL,NULL,NULL,NULL);
   */
  @Test
  void testSearchBadCase() {
    // given
    Optional<User> optionalUser = userRepository.findById(5L);
    Set<UserRole> userRoleSet = null;
    Set<Role> roleSet = new HashSet<Role>();
    // when
    userRoleSet = optionalUser.get().getUserRoles();
    for (UserRole ur : userRoleSet) {
      if (ur.getRole() != null) {
        roleSet.add(ur.getRole());
        log.info("user:{}, role:{}", optionalUser.get().getName(), ur.getRole().getName());
      }
    }
    // when
    assertThat(roleSet.stream().count()).isEqualTo(3);
    assertThat(roleSet).extracting(Role::getName).contains("admin"); // Role 이름 추출
    assertThat(roleSet).extracting(Role::getName).contains("user"); // Role 이름 추출
    assertThat(roleSet).extracting(Role::getName).contains("etc"); // Role 이름 추출
  }

  @Test
  void testSearchBadCase2() {
    // given
    Set<UserRole> userRoles = null;
    Set<Role> roleSet = null;
    Optional<User> optionalUser = userRepository.findById(5L);

    // when
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      userRoles = user.getUserRoles();
      roleSet = user.getRoles();
      for (Role r : roleSet) {
        log.info("BADCASE2 ::: user:{}, role:{}", user.getName(), r.getName());
      }
    }
    // when
    assertThat(userRoles.stream().count()).isEqualTo(3);
    assertThat(roleSet).extracting(Role::getName).contains("admin"); // Role 이름 추출
    assertThat(roleSet).extracting(Role::getName).contains("user"); // Role 이름 추출
    assertThat(roleSet).extracting(Role::getName).contains("etc"); // Role 이름 추출
  }

  /*
   * 특정 사용자를 검색하여 할당된 role의 명칭을 구한다.
   * 
   * select r1_0.id,r1_0.name from role r1_0 where r1_0.id in (2,3,1);
   */
  @Test
  void testSearchGoodCase() {
    // given
    List<Long> roleIds = null;
    List<Role> roles = null;
    Optional<User> optionalUser = userRepository.findById(5L);

    // when
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      roleIds = user.getUserRoles().stream().map(UserRole::getRole).filter(Objects::nonNull)
          .map(Role::getId).filter(Objects::nonNull).distinct() // 중복 제거
          .collect(Collectors.toList());
      log.info("Good Case : roleIds size: {}, values: {}", roleIds.size(), roleIds);
      roles = roleRepository.findAllById(roleIds);
      for (Role r : roles) {
        log.info("user:{}, role:{}", user.getName(), r.getName());
      }
    }

    // then
    assertThat(roleIds.size()).isEqualTo(3);
    assertThat(roles.stream().map(Role::getName).anyMatch(name -> "admin".equals(name)));
    assertThat(roles.stream().map(Role::getName).anyMatch(name -> "user".equals(name)));
    assertThat(roles.stream().map(Role::getName).anyMatch(name -> "etc".equals(name)));
  }

}
