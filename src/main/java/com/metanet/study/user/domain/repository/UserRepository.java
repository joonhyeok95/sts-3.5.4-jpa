package com.metanet.study.user.domain.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.metanet.study.user.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Page<User> findAll(Pageable pageable);

  List<User> findByNameContaining(String keyword); // 1. 포함 검색 (LIKE %keyword%)

  List<User> findByNameLike(String likePattern); // 2. 직접 LIKE 조건 (와일드카드 직접 넣어야 함)

  List<User> findByNameStartingWith(String prefix); // 3. 특정 시작 문자열

  List<User> findByNameEndingWith(String suffix); // 4. 특정 끝 문자열
}
