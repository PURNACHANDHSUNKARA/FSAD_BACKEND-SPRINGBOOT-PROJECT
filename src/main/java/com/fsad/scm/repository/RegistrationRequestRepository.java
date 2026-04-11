package com.fsad.scm.repository;

import com.fsad.scm.entity.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long> {

    Optional<RegistrationRequest> findByEmail(String email);

    List<RegistrationRequest> findByStatus(String status);
}
