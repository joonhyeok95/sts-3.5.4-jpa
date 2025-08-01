package com.metanet.study.user.mapper;

import com.metanet.study.dept.mapper.DeptMapper;
import com.metanet.study.user.dto.UserRequestDto;
import com.metanet.study.user.dto.UserResponseDto;
import com.metanet.study.user.entity.User;

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
      dto.setDepartment(DeptMapper.toResponseDto(user.getDepartment())); // 변환 호출
    } else {
      dto.setDepartment(null);
    }
    return dto;
  }
}
