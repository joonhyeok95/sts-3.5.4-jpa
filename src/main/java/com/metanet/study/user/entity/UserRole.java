package com.metanet.study.user.entity;

import java.time.LocalDateTime;
import com.metanet.study.role.entity.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_role")
public class UserRole {
  @Id
  @GeneratedValue
  private Long id;

  // 중간(조인) 엔티티 패턴으로 구성한다.

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id")
  private Role role;

  // 추가 컬럼: 예시 (부여일자, 상태 등)
  private LocalDateTime grantedAt;
  private String status;

}

