package com.metanet.study.user.reopository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.metanet.study.user.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
  List<UserRole> findAllByUserId(Long userId);
}
