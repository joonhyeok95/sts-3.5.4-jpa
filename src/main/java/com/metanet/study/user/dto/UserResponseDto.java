package com.metanet.study.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

  private long id;
  private String name;
  private String email;

  // 일반적인 API스펙으로 변경
  // private DepartmentDto department;
  private long departmentId;
  private String departmentName;

}
