package com.fluffy_robot.account.service;

import com.fluffy_robot.account.domain.request.LoginRequest;
import com.fluffy_robot.account.domain.UserIdentity;
import com.fluffy_robot.account.service.core.UserIdentityService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

    private final UserIdentityService userIdentityService;
    private final BCryptPasswordEncoder passwordEncoder;

    public String login(LoginRequest request) {
        UserIdentity user = (UserIdentity) userIdentityService.loadUserByUsername(request.getEmail());

        if (user != null) {
            if (request.getEmail().equalsIgnoreCase(user.getEmail()) &&
                    passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return "Login successful";
            }
        }

        return "Login failed";
    }
}
