package com.fsad.scm.service.impl;

import com.fsad.scm.entity.User;
import com.fsad.scm.repository.UserRepository;
import com.fsad.scm.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password, String role) {

        Optional<User> optionalUser =
                userRepository.findByEmail(email.trim().toLowerCase());

        if (optionalUser.isEmpty()) return null;

        User user = optionalUser.get();

        // password check
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        // role check
        if (!user.getRole().equalsIgnoreCase(role)) {
            return null;
        }

        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase());
    }
}