package com.starboost.starboost_backend_demo.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
