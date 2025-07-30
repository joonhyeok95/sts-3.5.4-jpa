package com.metanet.study.dept.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metanet.study.dept.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
