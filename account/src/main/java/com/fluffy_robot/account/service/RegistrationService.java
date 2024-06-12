package com.fluffy_robot.account.service;

import com.fluffy_robot.account.domain.ConfirmationToken;
import com.fluffy_robot.account.domain.UserIdentity;
import com.fluffy_robot.account.domain.request.RegistrationRequest;
import com.fluffy_robot.account.domain.UserIdentityRole;
import com.fluffy_robot.account.service.core.ConfirmationTokenService;
import com.fluffy_robot.account.service.core.EmailValidationService;
import com.fluffy_robot.account.service.core.UserIdentityService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserIdentityService userIdentityService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidationService emailValidationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String register(RegistrationRequest request) {
        boolean isValid = emailValidationService.test(request.getEmail());
        if (!isValid) {
            throw new IllegalStateException("Email is not valid.");
        }

        UserIdentity user = new UserIdentity(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                UserIdentityRole.USER
        );

        boolean isPresent = userIdentityService.doesUserExist(user);
        if (isPresent) {
            throw new IllegalStateException("Email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userIdentityService.saveUser(user);

        ConfirmationToken token = new ConfirmationToken(UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user);
        confirmationTokenService.saveConfirmationToken(token);

        // TODO: Send email

        return "http://localhost:8080/api/v1/login/confirm?token=" + token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationToken(token)
                    .orElseThrow(() -> new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmedAt() != null)
                throw new IllegalStateException("Email already confirmed");

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now()))
                throw new IllegalStateException("Token expired");

        confirmationTokenService.setConfirmedAt(token);
        userIdentityService.enableUserIdentity(confirmationToken.getUserIdentity().getEmail());

        return "Token confirmed";
    }
}
