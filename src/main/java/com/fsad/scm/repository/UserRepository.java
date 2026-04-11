package com.fsad.scm.repository;

import com.fsad.scm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPasswordAndRoleIgnoreCase(String email, String password, String role);

    boolean existsByEmail(String email);
}
