package com.zerobase.fintech.controller;

import com.zerobase.fintech.controller.dto.request.AdminRequest;
import com.zerobase.fintech.controller.dto.response.AdminResponse;
import com.zerobase.fintech.entity.AccountRequestEntity;
import com.zerobase.fintech.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasAnyRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/request")
@RestController
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/list")
    public Slice<AdminResponse.RequestListResponse> requestList(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Slice<AccountRequestEntity> requestList = adminService.showRequestList(pageable);

        return AdminResponse.RequestListResponse.of(requestList);
    }

    @GetMapping("/list/all")
    public Slice<AdminResponse.RequestListResponse> allRequestList(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Slice<AccountRequestEntity> requestList = adminService.showAllRequestList(pageable);

        return AdminResponse.RequestListResponse.of(requestList);
    }

    // TODO: 요청 승인 & 거절 기능
    @PostMapping("/{requestId}/approve")
    public ResponseEntity<?> approve(
            @PathVariable Long requestId
    ) {
        adminService.approveRequest(requestId);

        return ResponseEntity.ok("Approved Done");
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<?> reject(
            @PathVariable Long requestId,
            @Valid @RequestBody AdminRequest.RejectRequest request
    ) {
        adminService.rejectRequest(requestId, request);

        return ResponseEntity.ok("Rejected Done");
    }
}
