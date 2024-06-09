package com.fluffy_robot.account.repository;

import com.fluffy_robot.account.domain.Identity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface IdentityRepository {

    Optional<Identity> findByEmail(String email);
}
