package com.metanet.study.user.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.metanet.study.dept.entity.Department;
import com.metanet.study.dept.mapper.DeptMapper;
import com.metanet.study.dept.repository.DepartmentRepository;
import com.metanet.study.global.domain.PageResponse;
import com.metanet.study.user.dto.UserRequestDto;
import com.metanet.study.user.dto.UserResponseDto;
import com.metanet.study.user.entity.User;
import com.metanet.study.user.mapper.UserMapper;
import com.metanet.study.user.reopository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final DepartmentRepository departmentRepository;

  @Transactional(readOnly = true)
  public List<UserResponseDto> getAllUsers() {
    // 대규모 데이터 OOM 발생 처리 개선 필요
    return userRepository.findAll().stream()
        .map(user -> UserResponseDto.builder().id(user.getId()).name(user.getName())
            .email(user.getEmail()).department(DeptMapper.toResponseDto(user.getDepartment()))
            .build())
        .collect(Collectors.toList());
  }

  // 페이징처리
  @Transactional(readOnly = true)
  public PageResponse<UserResponseDto> getAllUsersPage(Pageable pageable) {
    Page<User> page = userRepository.findAll(pageable);
    Page<UserResponseDto> userPage = page.map(UserMapper::toResponseDto);
    // 실제 처리시간 계산을 위해 임의로 타이머처리
    try {
      // 1~10초의 랜덤 정수(1 이상 10 이하)
      int randomSeconds = ThreadLocalRandom.current().nextInt(1, 11);
      long randomMillis = randomSeconds * 1000L;
      log.info(randomSeconds + "초 delay... Now Page:" + pageable.getPageNumber());
      Thread.sleep(randomMillis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return new PageResponse<>(userPage);
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
