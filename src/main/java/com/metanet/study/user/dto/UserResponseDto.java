package com.metanet.study.user.dto;

import com.metanet.study.dept.dto.DepartmentDto;
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

  private Long id;
  private String name;
  private String email;
  private DepartmentDto department;

}
