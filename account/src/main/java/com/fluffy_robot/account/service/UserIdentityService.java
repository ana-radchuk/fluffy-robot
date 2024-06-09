package com.fluffy_robot.account.service;

import com.fluffy_robot.account.domain.ConfirmationToken;
import com.fluffy_robot.account.domain.UserIdentity;
import com.fluffy_robot.account.repository.UserIdentityRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserIdentityService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "User with email %s not found";
    private final UserIdentityRepository userIdentityRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userIdentityRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
    }

    public String saveIdentity(UserIdentity userIdentity) {
        boolean isPresent = userIdentityRepository.findByEmail(userIdentity.getEmail()).isPresent();

        if (isPresent) {
            throw new IllegalStateException("Email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(userIdentity.getPassword());
        userIdentity.setPassword(encodedPassword);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                userIdentity
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        userIdentityRepository.save(userIdentity);

        // TODO: Send email

        return token;
    }
}
