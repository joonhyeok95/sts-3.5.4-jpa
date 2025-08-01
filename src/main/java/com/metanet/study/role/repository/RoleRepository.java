package com.metanet.study.role.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.metanet.study.role.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);

}
