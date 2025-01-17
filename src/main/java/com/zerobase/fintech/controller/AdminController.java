package com.zerobase.fintech.controller;

import com.zerobase.fintech.common.enums.UserRole;
import com.zerobase.fintech.controller.dto.request.UserRequest;
import com.zerobase.fintech.controller.dto.response.AdminResponse;
import com.zerobase.fintech.controller.dto.response.UserResponse;
import com.zerobase.fintech.entity.AdminRequestEntity;
import com.zerobase.fintech.entity.UserEntity;
import com.zerobase.fintech.service.AdminService;
import com.zerobase.fintech.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasAnyRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    // 관계자만 접근 가능하도록 특수한 URL 사용
    @PostMapping("/YWRtaW4K")
    public UserResponse.SignUpResponse register(
            @Valid @RequestBody UserRequest.SignUpRequest request
    ) {
        UserEntity user = userService.register(request, UserRole.ROLE_ADMIN);

        return UserResponse.SignUpResponse.of(user);
    }

    @PostMapping("request-list")
    public Slice<AdminResponse.RequestListResponse> requestList(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Slice<AdminRequestEntity> requestList = adminService.showRequestList(pageable);

        return AdminResponse.RequestListResponse.of(requestList);
    }

    @PostMapping("request-list/all")
    public Slice<AdminResponse.RequestListResponse> allRequestList(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Slice<AdminRequestEntity> requestList = adminService.showAllRequestList(pageable);

        return AdminResponse.RequestListResponse.of(requestList);
    }

    // TODO: 요청 승인 & 거절 기능

}
