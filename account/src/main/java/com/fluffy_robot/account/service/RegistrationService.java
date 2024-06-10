package com.fluffy_robot.account.service;

import com.fluffy_robot.account.domain.ConfirmationToken;
import com.fluffy_robot.account.domain.UserIdentity;
import com.fluffy_robot.account.domain.RegistrationRequest;
import com.fluffy_robot.account.domain.UserIdentityRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserIdentityService userIdentityService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidationService emailValidationService;

    public String register(RegistrationRequest request) {
        boolean isValid = emailValidationService.test(request.getEmail());

        if (!isValid) {
            throw new IllegalStateException("Email is not valid.");
        }

        String token = userIdentityService.saveUserIdentity(
                new UserIdentity(
                        request.getName(),
                        request.getEmail(),
                        request.getPassword(),
                        UserIdentityRole.USER
                ));

        return "http://localhost:8080/api/v1/login/confirm?token=" + token;
    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.confirmToken(token);
        userIdentityService.enableUserIdentity(confirmationToken.getUserIdentity().getEmail());

        return "Token confirmed";
    }
}
