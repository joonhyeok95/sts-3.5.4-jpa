package com.metanet.study.user.domain.service.dto;

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

  private long id;

  @NotBlank(message = "Name is mandatory")
  private String name;

  @Email(message = "Email should be valid")
  private String email;

  // 부서 null 허용;
  private Long departmentId;

}
