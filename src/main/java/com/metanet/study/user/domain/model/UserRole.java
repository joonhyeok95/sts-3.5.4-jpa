package com.metanet.study.user.domain.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_role")
public class UserRole {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  // 중간(조인) 엔티티 패턴으로 구성한다.

  // @ManyToOne(fetch = FetchType.LAZY)
  // 연관관계 매핑 없이 FK 필드만 사용
  @Column(name = "user_id", nullable = false)
  private Long userId;

  // @ManyToOne(fetch = FetchType.LAZY)
  @Column(name = "role_id", nullable = false)
  private Long roleId;

  // 추가 컬럼: 예시 (부여일자, 상태 등)
  private LocalDateTime grantedAt;
  private String status;

}

