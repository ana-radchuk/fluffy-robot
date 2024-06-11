package com.fluffy_robot.account.domain.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
