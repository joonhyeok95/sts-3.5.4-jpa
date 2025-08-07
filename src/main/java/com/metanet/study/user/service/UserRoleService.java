package com.metanet.study.user.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.metanet.study.role.entity.Role;
import com.metanet.study.role.repository.RoleRepository;
import com.metanet.study.user.entity.User;
import com.metanet.study.user.entity.UserRole;
import com.metanet.study.user.reopository.UserRepository;
import com.metanet.study.user.reopository.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRoleService {

  private final UserRepository userRepository;
  private final UserRoleRepository userRoleRepository;
  private final RoleRepository roleRepository;

  @Transactional(readOnly = true)
  public List<Role> userFindRoles(Long userId) {
    List<Role> roles = new ArrayList<>();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    for (UserRole userRole : user.getUserRoles()) {
      Long roleId = userRole.getRole().getId();
      Role role = roleRepository.findById(roleId)
          .orElseThrow(() -> new EntityNotFoundException("Role not found with id " + roleId));

      roles.add(role);
    }
    return roles;
  }


}
