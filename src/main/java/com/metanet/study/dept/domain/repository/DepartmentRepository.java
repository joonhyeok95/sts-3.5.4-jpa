package com.metanet.study.dept.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.metanet.study.dept.domain.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
