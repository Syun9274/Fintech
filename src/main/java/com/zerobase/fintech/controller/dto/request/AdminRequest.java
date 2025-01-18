package com.zerobase.fintech.controller.dto.request;

import lombok.Data;

public class AdminRequest {

    @Data
    public static class RejectRequest {
        String reason;
    }
}
