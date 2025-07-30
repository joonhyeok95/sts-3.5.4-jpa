package com.metanet.study.user.reopository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metanet.study.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
