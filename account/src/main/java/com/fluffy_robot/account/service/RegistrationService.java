package com.fluffy_robot.account.service;

import com.fluffy_robot.account.domain.UserIdentity;
import com.fluffy_robot.account.domain.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserIdentityService userIdentityService;
    private final EmailValidationService emailValidationService;

    public String register(RegistrationRequest request) {
        boolean isValid = emailValidationService.test(request.getEmail());

        if (!isValid) {
            throw new IllegalStateException("Email is not valid.");
        }

        return userIdentityService.saveIdentity(new UserIdentity(request.getName(), request.getEmail(), request.getPassword()));
    }
}
