package com.metanet.study.user.domain.service.dto;

import com.metanet.study.user.domain.model.User;
import com.metanet.study.user.domain.model.UserRole;

public class UserMapper {

  public static User toEntity(UserRequestDto dto) {
    if (dto == null) {
      return null;
    }
    User user = new User();
    user.setId(dto.getId());
    user.setName(dto.getName());
    user.setEmail(user.getEmail());
    user.setDepartment(user.getDepartment());
    return user;
  }

  public static UserResponseDto toResponseDto(User user) {
    if (user == null) {
      return null;
    }
    UserResponseDto dto = new UserResponseDto();
    dto.setId(user.getId());
    dto.setName(user.getName());
    dto.setEmail(user.getEmail());

    if (user.getDepartment() != null) {
      // dto.setDepartment(DeptMapper.toResponseDto(user.getDepartment())); // 변환 호출
      dto.setDepartmentId(user.getDepartment().getId());
      dto.setDepartmentName(user.getDepartment().getName());
    }

    return dto;
  }

  public static UserRoleDto toUserRoleDto(UserRole userRole) {
    if (userRole == null) {
      return null;
    }

    Long userId = userRole.getUser() != null ? userRole.getUser().getId() : null;
    String userName = userRole.getUser() != null ? userRole.getUser().getName() : null;

    Long roleId = userRole.getRole() != null ? userRole.getRole().getId() : null;
    String roleName = userRole.getRole() != null ? userRole.getRole().getName() : null;

    return new UserRoleDto(userRole.getId(), userId, userName, roleId, roleName,
        userRole.getGrantedAt(), userRole.getStatus());
  }

}
