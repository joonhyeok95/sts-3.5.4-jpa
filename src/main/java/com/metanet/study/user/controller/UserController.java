package com.metanet.study.user.controller;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.metanet.study.global.domain.PageResponse;
import com.metanet.study.role.entity.Role;
import com.metanet.study.user.dto.UserRequestDto;
import com.metanet.study.user.dto.UserResponseDto;
import com.metanet.study.user.service.UserRoleService;
import com.metanet.study.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserRoleService userRoleService;

  // GlobalResponseWrapper 를 활용하기
  @GetMapping("/all")
  public List<UserResponseDto> getAllUsers(HttpServletRequest request) {
    List<UserResponseDto> users = userService.getAllUsers();
    return users;
  }

  // ResponseEntity 를 활용하기
  @GetMapping
  public ResponseEntity<?> getAllUsers(Pageable pageable, HttpServletRequest request) {

    PageResponse<UserResponseDto> page = userService.getAllUsersPage(pageable);
    return ResponseEntity.ok(page);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> getUser(@PathVariable("id") long id,
      HttpServletRequest request) {
    return userService.getUserById(id).map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public long createUser(@Valid @RequestBody UserRequestDto dto) {
    UserResponseDto created = userService.createUser(dto);
    return created.getId();
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") long id,
      @Valid @RequestBody UserRequestDto dto) {
    try {
      UserResponseDto updated = userService.updateUser(id, dto);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Integer> deleteUser(@PathVariable("id") long id) {
    userService.deleteUser(id);
    return ResponseEntity.ok(1);
  }

  @GetMapping("/{id}/roles")
  public List<Role> getUserRoles(@PathVariable("id") long id) {
    return userRoleService.userFindRoles(id);
  }
}
