package com.fsad.scm.service;

import com.fsad.scm.entity.RegistrationRequest;

import java.util.List;

public interface RegistrationService {

    RegistrationRequest request(String email, String username);

    List<RegistrationRequest> getAll();

    void approve(Long id);

    void reject(Long id);

}
