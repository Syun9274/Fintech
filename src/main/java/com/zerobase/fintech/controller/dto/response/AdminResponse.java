package com.zerobase.fintech.controller.dto.response;

import com.zerobase.fintech.common.enums.RequestType;
import com.zerobase.fintech.entity.AdminRequestEntity;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public class AdminResponse {

    public record RequestListResponse(
            Long requestId,
            Long userId,
            Long accountId,
            RequestType requestType,
            LocalDateTime createdAt
    ) {
        public static Slice<RequestListResponse> of(Slice<AdminRequestEntity> requests) {
            return requests.map(request -> new RequestListResponse(
                    request.getId(),
                    request.getUserId(),
                    request.getAccountId(),
                    request.getRequestType(),
                    request.getCreatedAt()
            ));
        }
    }
}
