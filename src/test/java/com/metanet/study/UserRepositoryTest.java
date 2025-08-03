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
@DataJpaTest // 내장db가 있을 경우
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void testInsertAndFind() {
    // 저장
    Department dept = new Department();
    dept.setId(100L);
    User user = User.builder().name("John Doe").email("john@example.com").department(dept).build();
    // user.setName("John Doe");
    // user.setEmail("john@example.com");
    User id = userRepository.save(user);

    // 조회
    Optional<User> users = userRepository.findById(id.getId());

    // 검증
    assertThat(users).isNotEmpty();
    assertThat(users.get().getName()).isEqualTo("John Doe");
    assertThat(users.get().getEmail()).isEqualTo("john@example.com");
    Long time = 5000L;
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  void testSearch() {
    List<User> user1 = userRepository.findByNameContaining("동네");
    // List<User> user2 = userRepository.findByNameLike("%준%");
    // List<User> user3 = userRepository.findByNameStartingWith("임");
    // List<User> user4 = userRepository.findByNameEndingWith("혁");
    log.info("user1:{}", user1.get(0).getUserRoles().size());
    Set<Role> roles = user1.get(0).getRoles();
    for (Role role : roles) {
      System.out.println("  Role: " + role.getName());
    }
  }
}
