package com.metanet.study.user.dto;

import java.time.LocalDateTime;
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
public class UserRoleDto {
  private long id;
  private Long userId;
  private String userName;
  private Long roleId;
  private String roleName;
  private LocalDateTime grantedAt;
  private String status;


}
