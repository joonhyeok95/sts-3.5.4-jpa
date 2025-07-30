package com.metanet.study.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

  private Long id;
  private String name;
  private String email;

  public UserResponseDto() {}

  public UserResponseDto(Long id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }

}
