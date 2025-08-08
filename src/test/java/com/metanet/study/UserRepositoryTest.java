package com.metanet.study;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.metanet.study.dept.entity.Department;
import com.metanet.study.role.entity.Role;
import com.metanet.study.user.entity.User;
import com.metanet.study.user.reopository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest // JPA 관련 컴포넌트 로딩, @Transactional 적용
// @SpringBootTest // 통합적인 테스트 시 사용
// @Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

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
  void testSearch() {
    // given
    List<User> user1 = userRepository.findByNameContaining("임준혁23");
    // List<User> user2 = userRepository.findByNameLike("%준%");
    // List<User> user3 = userRepository.findByNameStartingWith("임");
    // List<User> user4 = userRepository.findByNameEndingWith("혁");

    // when
    log.info("user1:{}", user1.get(0).getUserRoles().size());
    Set<Role> roles = user1.get(0).getRoles();
    for (Role role : roles) {
      System.out.println("  Role: " + role.getName());
    }
    // when
    assertThat(roles.stream().count()).isEqualTo(3);
  }
}
