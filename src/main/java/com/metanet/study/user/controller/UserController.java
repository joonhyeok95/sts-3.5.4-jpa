package com.metanet.study.user.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import com.metanet.study.global.domain.ApiResponse;
import com.metanet.study.global.domain.PageResponse;
import com.metanet.study.global.model.ResponseEntityUtil;
import com.metanet.study.role.dto.RoleDto;
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
    return ResponseEntityUtil.buildResponse(page, HttpStatus.OK, "User retrieved successfully",
        request);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<Optional<Object>>> getUser(@PathVariable("id") long id,
      HttpServletRequest request) {
    return ResponseEntityUtil.buildResponse(userService.getUserById(id), HttpStatus.OK,
        "User retrieved successfully", request);
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Long>> createUser(@Valid @RequestBody UserRequestDto dto,
      HttpServletRequest request) {
    UserResponseDto created = userService.createUser(dto);
    return ResponseEntityUtil.buildResponse(created.getId(), HttpStatus.CREATED,
        "User created successfully", request);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@PathVariable("id") long id,
      @Valid @RequestBody UserRequestDto dto, HttpServletRequest request) {
    UserResponseDto updated = userService.updateUser(id, dto);
    return ResponseEntityUtil.buildResponse(updated, HttpStatus.OK, "User Modify successfully",
        request);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable("id") long id,
      HttpServletRequest request) {
    userService.deleteUser(id);
    return ResponseEntityUtil.buildResponse("User deleted successfully", HttpStatus.OK,
        "Delete success", request);
  }

  @GetMapping("/{id}/roles")
  public ResponseEntity<ApiResponse<List<RoleDto>>> getUserRoles(@PathVariable("id") long id,
      HttpServletRequest request) {
    return ResponseEntityUtil.buildResponse(userRoleService.userFindRoles(id), HttpStatus.OK,
        "User Roles retrieved successfully", request);
  }
}
