package com.metanet.study.role.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import com.metanet.study.user.entity.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role")
// @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // 직렬화 대상에서 Lazy, 또는 DTO 로 예외처리
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  // @JsonIgnore
  @OneToMany(mappedBy = "role")
  private Set<UserRole> users = new HashSet<>();


  /*
   * equals와 hashCode는 ID 기준으로 재정의
   * 
   * Why? Set, Map 같은 컬렉션에 엔티티를 올바르게 비교하고 중복을 제거하려면 필요하다 기본 Object, equals()는 단지 객체 메모리 주소 비교라서 안정성과
   * 일관성을 위해 구현해야 한다. 복합키를 가진 키 클래스는 반드시 구현해야 하며, 단일키 엔티티도 가능한 한 구현하는게 좋다 이를 통해 JAP 환경에서 컬렉션 처리, 캐싱,
   * 프록시 비교 등 다양한 문제를 예방할 수 있다.
   */

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Role))
      return false;
    Role role = (Role) o;
    return Objects.equals(id, role.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
