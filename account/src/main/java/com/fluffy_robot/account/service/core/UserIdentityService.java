package com.fluffy_robot.account.service.core;

import com.fluffy_robot.account.domain.UserIdentity;
import com.fluffy_robot.account.repository.UserIdentityRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserIdentityService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "User with email %s not found";
    private final UserIdentityRepository userIdentityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userIdentityRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
    }

    public boolean doesUserExist(UserIdentity userIdentity) {
        return userIdentityRepository.findByEmail(userIdentity.getEmail()).isPresent();
    }

    public void saveUser(UserIdentity userIdentity) {
        userIdentityRepository.save(userIdentity);
    }

    public void enableUserIdentity(String email) {
        userIdentityRepository.enableUserIdentity(email);
    }
}
