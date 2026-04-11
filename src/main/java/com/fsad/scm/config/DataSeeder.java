package com.fsad.scm.config;

import com.fsad.scm.entity.User;
import com.fsad.scm.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdmin();
        seedStudent();
    }

    private void seedAdmin() {
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            User admin = new User();
            admin.setUsername("Super Admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("admin");
            admin.setRegistrationApproved(1);
            userRepository.save(admin);
            System.out.println("✅ Admin created: admin@gmail.com / admin123");
        } else {
            System.out.println("✅ Admin already exists: admin@gmail.com / admin123");
        }
    }

    private void seedStudent() {
        if (userRepository.findByEmail("student@gmail.com").isEmpty()) {
            User student = new User();
            student.setUsername("Test Student");
            student.setEmail("student@gmail.com");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setRole("student");
            student.setRegistrationApproved(1);
            userRepository.save(student);
            System.out.println("✅ Student created: student@gmail.com / student123");
        } else {
            System.out.println("✅ Student already exists: student@gmail.com / student123");
        }
    }
}
