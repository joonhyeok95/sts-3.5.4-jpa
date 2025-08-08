package com.metanet.study.user.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.metanet.study.dept.entity.Department;
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
    /*
     * 아래 코드는 모든 유저의 department id in 조건에 모든 데이터가 들어감.
     * 
     * 실행로그: select d1_0.id,d1_0.name from department d1_0 where d1_0.id in
     * (100,200,300,400,404,405,101,102,202,203,303,304,500,505,506,600,601,NULL,NULL,NULL,NULL,NULL
     * ,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,
     * NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,
     * NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,
     * NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,
     * NULL,NULL,NULL,NULL,NULL,NULL);
     */
    // return userRepository.findAll().stream()
    // .map(user -> UserResponseDto.builder().id(user.getId()).name(user.getName())
    // .email(user.getEmail()).departmentId(user.getDepartment().getId())
    // .departmentName(user.getDepartment().getName()).build())
    // .collect(Collectors.toList());

    /*
     * 사용자 데이터에 부서테이블 성능개선
     * 
     * 실행로그: select d1_0.id,d1_0.name from department d1_0 where d1_0.id in
     * (100,200,300,400,404,405,101,102,202,203,303,304,500,505,506,600,601);
     */
    List<User> users = userRepository.findAll();
    List<Long> departmentIds = users.stream().map(User::getDepartment).filter(Objects::nonNull)
        .map(Department::getId).filter(Objects::nonNull).distinct().collect(Collectors.toList());

    List<Department> departments = departmentRepository.findAllById(departmentIds);
    // 1) departmentId를 키로 department 객체를 빠르게 찾기 위한 맵 생성
    Map<Long, Department> departmentMap = departments.stream().filter(Objects::nonNull)
        .collect(Collectors.toMap(Department::getId, Function.identity()));

    // 2) users -> UserResponseDto 변환
    return users.stream().map(user -> {
      Long deptId = user.getDepartment() != null ? user.getDepartment().getId() : null;

      Department dept = deptId != null ? departmentMap.get(deptId) : null;

      return UserResponseDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
          .departmentId(deptId).departmentName(dept != null ? dept.getName() : null).build();
    }).collect(Collectors.toList());
  }

  // 페이징처리
  @Transactional(readOnly = true)
  public PageResponse<UserResponseDto> getAllUsersPage(Pageable pageable) {
    Page<User> page = userRepository.findAll(pageable);
    Page<UserResponseDto> userPage = page.map(UserMapper::toResponseDto);
    // 실제 처리시간 계산을 위해 임의로 타이머처리
    try {
      // 1~10초의 랜덤 정수(1 이상 10 이하)
      int randomSeconds = ThreadLocalRandom.current().nextInt(1, 2);
      long randomMillis = randomSeconds * 1000L;
      log.info(randomSeconds + "초 delay... Now Page:" + pageable.getPageNumber());
      Thread.sleep(randomMillis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return new PageResponse<>(userPage);
  }

  @Transactional(readOnly = true)
  public Optional<UserResponseDto> getUserById(long id) {
    return userRepository.findById(id)
        .map(user -> UserResponseDto.builder().id(user.getId()).name(user.getName())
            .email(user.getEmail()).departmentId(user.getDepartment().getId())
            .departmentName(user.getDepartment().getName()).build());
  }

  @Transactional
  public UserResponseDto createUser(UserRequestDto dto) {
    Department department = null;
    if (dto.getDepartmentId() > -1) {
      department = departmentRepository.findById(dto.getDepartmentId())
          .orElseThrow(() -> new RuntimeException("Department not found"));
    }
    User user = new User();
    user.setName(dto.getName());
    user.setEmail(dto.getEmail());
    user.setDepartment(department);
    User saved = userRepository.save(user);

    return new UserResponseDto(saved.getId(), saved.getName(), saved.getEmail(),
        saved.getDepartment().getId(), saved.getDepartment().getName());
  }

  @Transactional
  public UserResponseDto updateUser(long id, UserRequestDto dto) {
    User updatedUser = userRepository.findById(id).map(user -> {
      user.setName(dto.getName());
      user.setEmail(dto.getEmail());

      Department department = departmentRepository.findById(dto.getDepartmentId())
          .orElseThrow(() -> new RuntimeException("Department not found"));
      user.setDepartment(department);
      return userRepository.save(user);
    }).orElseThrow(() -> new RuntimeException("User not found"));
    return new UserResponseDto(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail(),
        updatedUser.getDepartment().getId(), updatedUser.getDepartment().getName());
  }

  @Transactional
  public int deleteUser(long id) {
    int result = -1;
    userRepository.deleteById(id);
    boolean deleted = userRepository.findById(id).isEmpty();
    if (deleted) {
      result = 1;
    } else {
      // throws BusinessException 처리 (삭제실패)
    }
    return result;
  }

  @Transactional(readOnly = true)
  public void searchUser(long id) {
    userRepository.findByNameContaining("홍길동");
    userRepository.findByNameLike("%길동%");
    userRepository.findByNameStartingWith("홍");
    userRepository.findByNameEndingWith("동");
  }

}
