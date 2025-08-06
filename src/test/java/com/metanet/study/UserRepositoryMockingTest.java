package com.metanet.study;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.metanet.study.dept.entity.Department;
import com.metanet.study.user.entity.User;
import com.metanet.study.user.reopository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Transactional
public class UserRepositoryMockingTest {

  @Test
  void testInsertAndFind() {
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    // given
    Department dept = new Department();
    dept.setId(100L);
    User user = User.builder().name("John Doe").email("john@example.com").department(dept).build();
    List<User> dummyList = new ArrayList<>();
    dummyList.add(user);

    Mockito.when(userRepository.findByNameContaining(user.getName())).thenReturn(dummyList); // 임시데이터

    // when
    userRepository.save(user);
    List<User> users = userRepository.findByNameContaining(user.getName());

    // then
    assertThat(users).isNotEmpty();
    assertThat(users.get(0).getName()).isEqualTo("John Doe");
    assertThat(users.get(0).getEmail()).isEqualTo("john@example.com");
  }
  //
  // @Test
  // void testSearch() {
  // List<User> user1 = userRepository.findByNameContaining("동네");
  // // List<User> user2 = userRepository.findByNameLike("%준%");
  // // List<User> user3 = userRepository.findByNameStartingWith("임");
  // // List<User> user4 = userRepository.findByNameEndingWith("혁");
  // log.info("user1:{}", user1.get(0).getUserRoles().size());
  // Set<Role> roles = user1.get(0).getRoles();
  // for (Role role : roles) {
  // System.out.println(" Role: " + role.getName());
  // }
  // }
}
