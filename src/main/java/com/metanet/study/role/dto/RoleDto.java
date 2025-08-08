package com.metanet.study.role.dto;

import com.metanet.study.role.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
  private Long id;
  private String name;

  public RoleDto(Role role) {
    this.id = role.getId();
    this.name = role.getName();
  }
}
