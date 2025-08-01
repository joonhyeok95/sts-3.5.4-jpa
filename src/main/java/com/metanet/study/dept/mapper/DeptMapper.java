package com.metanet.study.dept.mapper;

import com.metanet.study.dept.dto.DepartmentDto;
import com.metanet.study.dept.entity.Department;

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
