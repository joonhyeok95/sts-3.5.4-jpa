package com.metanet.study.dept.domain.service.dto;

import com.metanet.study.dept.domain.model.Department;

public class DeptMapper {

  public static Department toEntity(DepartmentDto dto) {
    if (dto == null) {
      return null;
    }
    Department dept = new Department();
    dept.setId(dto.getId());
    dept.setName(dto.getName());
    return dept;
  }

  public static DepartmentDto toResponseDto(Department dept) {
    if (dept == null) {
      return null;
    }
    DepartmentDto dto = new DepartmentDto();
    dto.setId(dept.getId());
    dto.setName(dept.getName());
    return dto;
  }
}
