package com.metanet.study.user.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.metanet.study.user.domain.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
  List<UserRole> findAllByUserId(Long userId);
}
