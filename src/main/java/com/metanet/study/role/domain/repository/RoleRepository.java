package com.metanet.study.role.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.metanet.study.role.domain.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);

  List<Role> findAllById(Long id);

}
