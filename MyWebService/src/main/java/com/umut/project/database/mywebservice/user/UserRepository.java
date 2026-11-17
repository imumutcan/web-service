package com.umut.project.database.mywebservice.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);

    Optional<User> findByActivationToken(String activationToken);

    Optional<User> findByEmail(String email);

    Page<User> findByIdNot(Long id, Pageable pageable);
}
