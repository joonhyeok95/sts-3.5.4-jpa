package com.metanet.study.user.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.metanet.study.dept.entity.Department;
import com.metanet.study.dept.repository.DepartmentRepository;
import com.metanet.study.user.dto.UserRequestDto;
import com.metanet.study.user.dto.UserResponseDto;
import com.metanet.study.user.entity.User;
import com.metanet.study.user.reopository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final DepartmentRepository departmentRepository;

  public List<UserResponseDto> getAllUsers() {
    return userRepository.findAll().stream()
        .map(user -> new UserResponseDto(user.getId(), user.getName(), user.getEmail()))
        .collect(Collectors.toList());
  }

  public Optional<UserResponseDto> getUserById(Long id) {
    return userRepository.findById(id)
        .map(user -> new UserResponseDto(user.getId(), user.getName(), user.getEmail()));
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

    return new UserResponseDto(saved.getId(), saved.getName(), saved.getEmail());
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
    return new UserResponseDto(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail());
  }

  @Transactional
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
