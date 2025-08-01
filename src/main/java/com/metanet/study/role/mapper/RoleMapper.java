package com.metanet.study.role.mapper;

import com.metanet.study.role.dto.RoleDto;
import com.metanet.study.role.entity.Role;

public class RoleMapper {

  public static RoleDto toRoleDto(Role role) {
    if (role == null)
      return null;
    return new RoleDto(role.getId(), role.getName());
  }
}
