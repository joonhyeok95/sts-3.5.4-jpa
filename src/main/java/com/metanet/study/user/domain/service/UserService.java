package com.metanet.study.user.domain.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import com.metanet.study.dept.domain.model.Department;
import com.metanet.study.dept.domain.repository.DepartmentRepository;
import com.metanet.study.global.domain.PageResponse;
import com.metanet.study.user.domain.model.User;
import com.metanet.study.user.domain.repository.UserRepository;
import com.metanet.study.user.domain.service.dto.UserRequestDto;
import com.metanet.study.user.domain.service.dto.UserResponseDto;
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
    /*
     * 1.사용자 정보를 조회. 2.사용자 정보에서 부서아이디 목록 추출. 3.부서아이디 목록으로 조회. 4.부서Map 생성으로 성능 개선(List 탐색보다 성능 우위).
     * 5.리턴DTO 로 데이터 조합.
     */
    Page<User> userPage = userRepository.findAll(pageable);
    // Page 객체는 getContent() 로 안전하게 리스트 추출, null 필터링 포함
    List<Long> departmentIds = userPage.getContent().stream().map(User::getDepartment)
        .filter(Objects::nonNull).map(Department::getId).filter(Objects::nonNull).distinct()
        .collect(Collectors.toList());
    List<Department> departments = departmentRepository.findAllById(departmentIds);
    log.info("departments size: {}", departments.size());
    Map<Long, Department> departmentMap =
        departments.stream().filter(Objects::nonNull).collect(Collectors.toMap(Department::getId,
            Function.identity(), (existing, replacement) -> existing));

    Page<UserResponseDto> userDtoPage = userPage.map(user -> {
      Long deptId = null;
      String deptName = null;
      if (!ObjectUtils.isEmpty(user.getDepartment())) {
        deptId = user.getDepartment().getId();
        if (deptId != null) {
          Department dept = departmentMap.get(deptId);
          deptName = (dept != null) ? dept.getName() : null;
        }
      }

      return UserResponseDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
          .departmentId(deptId).departmentName(deptName).build();
    });

    return new PageResponse<>(userDtoPage);
  }

  @Transactional(readOnly = true)
  public Optional<Object> getUserById(long id) {
    return userRepository.findById(id).map(user -> {
      Long deptId = null;
      String deptName = null;

      if (user.getDepartment() != null) {
        deptId = user.getDepartment().getId();
        deptName = user.getDepartment().getName();
      }
      return UserResponseDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
          .departmentId(deptId).departmentName(deptName).build();
    });
  }

  @Transactional
  public UserResponseDto createUser(UserRequestDto dto) {
    Department department = null;
    if (!ObjectUtils.isEmpty(dto.getDepartmentId())) {
      department = departmentRepository.findById(dto.getDepartmentId())
          .orElseThrow(() -> new RuntimeException("Department not found"));
    }
    User user = new User();
    user.setName(dto.getName());
    user.setEmail(dto.getEmail());
    user.setDepartment(department);
    User saved = userRepository.save(user);

    Long departmentId = null;
    String departmentName = null;
    if (saved.getDepartment() != null) {
      departmentId = saved.getDepartment().getId();
      departmentName = saved.getDepartment().getName();
    }

    return new UserResponseDto(saved.getId(), saved.getName(), saved.getEmail(), departmentId,
        departmentName);
  }

  @Transactional
  public UserResponseDto updateUser(long id, UserRequestDto dto) {
    User updatedUser = userRepository.findById(id).map(user -> {
      user.setName(dto.getName());
      user.setEmail(dto.getEmail());

      if (dto.getDepartmentId() == null) {
        // 부서 해제
        user.setDepartment(null);
      } else {
        Department department = departmentRepository.findById(dto.getDepartmentId())
            .orElseThrow(() -> new RuntimeException("Department not found"));
        user.setDepartment(department);
      }

      return userRepository.save(user);
    }).orElseThrow(() -> new RuntimeException("User not found"));

    Long deptId = null;
    String deptName = null;
    if (updatedUser.getDepartment() != null) {
      deptId = updatedUser.getDepartment().getId();
      deptName = updatedUser.getDepartment().getName();
    }

    return new UserResponseDto(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail(),
        deptId, deptName);
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
