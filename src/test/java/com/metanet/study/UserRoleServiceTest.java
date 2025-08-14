package com.metanet.study;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import com.metanet.study.role.domain.service.dto.RoleDto;
import com.metanet.study.user.domain.service.UserRoleService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @DataJpaTest // JPA 관련 컴포넌트 로딩, @Transactional 적용
@SpringBootTest // 통합적인 테스트 시 사용
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRoleServiceTest {

  @Autowired
  private UserRoleService userRoleService;

  @Test
  void testSearch() {
    // given
    // 이미 id:5 사용자가 등록되어있음

    // when
    List<RoleDto> roleDtos = userRoleService.userFindRoles(5L);

    // then
    assertThat(roleDtos.size()).isEqualTo(3);
  }
}
