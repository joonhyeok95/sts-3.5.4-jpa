package com.metanet.study.dept.entity;

import java.util.List;
import org.hibernate.annotations.BatchSize;
import com.metanet.study.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  // 양방향 연관 시 (선택사항)
  @OneToMany(mappedBy = "department")
  @BatchSize(size = 100)
  private List<User> users;

}
