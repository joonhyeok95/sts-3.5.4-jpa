package com.metanet.study.user.domain.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.metanet.study.role.domain.model.Role;
import com.metanet.study.role.domain.repository.RoleRepository;
import com.metanet.study.role.domain.service.dto.RoleDto;
import com.metanet.study.user.domain.model.User;
import com.metanet.study.user.domain.model.UserRole;
import com.metanet.study.user.domain.repository.UserRepository;
import com.metanet.study.user.domain.repository.UserRoleRepository;
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
  public List<RoleDto> userFindRoles(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    List<Long> roleIds = user.getUserRoles().stream().map(UserRole::getRole)
        .filter(Objects::nonNull).map(Role::getId).filter(Objects::nonNull).distinct() // 중복 제거
        .collect(Collectors.toList());
    log.info("roleIds size: {}, values: {}", roleIds.size(), roleIds);
    List<Role> roles = roleRepository.findAllById(roleIds);

    return roles.stream().map(RoleDto::new).collect(Collectors.toList());
  }


}
