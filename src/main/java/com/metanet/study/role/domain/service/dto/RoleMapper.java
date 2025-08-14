package com.metanet.study.role.domain.service.dto;

import com.metanet.study.role.domain.model.Role;

public class RoleMapper {

  public static RoleDto toRoleDto(Role role) {
    if (role == null)
      return null;
    return new RoleDto(role.getId(), role.getName());
  }
}
