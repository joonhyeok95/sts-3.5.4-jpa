package com.metanet.study.user.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.metanet.study.dept.entity.Department;
import com.metanet.study.dept.mapper.DeptMapper;
import com.metanet.study.dept.repository.DepartmentRepository;
import com.metanet.study.user.dto.UserRequestDto;
import com.metanet.study.user.dto.UserResponseDto;
import com.metanet.study.user.entity.User;
import com.metanet.study.user.reopository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final DepartmentRepository departmentRepository;


  @Transactional(readOnly = true)
  public List<UserResponseDto> getAllUsers() {
    return userRepository.findAll().stream()
        .map(user -> UserResponseDto.builder().id(user.getId()).name(user.getName())
            .email(user.getEmail()).department(DeptMapper.toResponseDto(user.getDepartment()))
            .build())
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Optional<UserResponseDto> getUserById(Long id) {
    return userRepository.findById(id)
        .map(user -> UserResponseDto.builder().id(user.getId()).name(user.getName())
            .email(user.getEmail()).department(DeptMapper.toResponseDto(user.getDepartment()))
            .build());
  }

  @Transactional
  public UserResponseDto createUser(UserRequestDto dto) {
    Department department = null;
    if (dto.getDepartment().getId() != null) {
      department = departmentRepository.findById(dto.getDepartment().getId())
          .orElseThrow(() -> new RuntimeException("Department not found"));
    }
    User user = new User();
    user.setName(dto.getName());
    user.setEmail(dto.getEmail());
    user.setDepartment(department);
    User saved = userRepository.save(user);

    return new UserResponseDto(saved.getId(), saved.getName(), saved.getEmail(),
        DeptMapper.toResponseDto(saved.getDepartment()));
  }

  @Transactional
  public UserResponseDto updateUser(Long id, UserRequestDto dto) {
    User updatedUser = userRepository.findById(id).map(user -> {
      user.setName(dto.getName());
      user.setEmail(dto.getEmail());

      Department department = departmentRepository.findById(dto.getDepartment().getId())
          .orElseThrow(() -> new RuntimeException("Department not found"));
      user.setDepartment(department);
      return userRepository.save(user);
    }).orElseThrow(() -> new RuntimeException("User not found"));
    return new UserResponseDto(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail(),
        DeptMapper.toResponseDto(updatedUser.getDepartment()));
  }

  @Transactional
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
