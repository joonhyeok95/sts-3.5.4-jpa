package com.metanet.study.user.dto;

import com.metanet.study.dept.dto.DepartmentDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

  private Long id;

  @NotBlank(message = "Name is mandatory")
  private String name;

  @Email(message = "Email should be valid")
  private String email;

  private DepartmentDto department;

}
