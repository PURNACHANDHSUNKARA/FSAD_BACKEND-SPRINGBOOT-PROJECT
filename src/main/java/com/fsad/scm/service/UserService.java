package com.fsad.scm.service;

import com.fsad.scm.entity.User;

import java.util.Optional;

public interface UserService {

    User register(User user);

    User login(String email, String password, String role);

    Optional<User> findByEmail(String email);
}
