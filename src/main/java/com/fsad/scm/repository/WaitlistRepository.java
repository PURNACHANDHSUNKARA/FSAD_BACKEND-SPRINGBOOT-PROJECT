package com.fsad.scm.repository;

import com.fsad.scm.entity.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {

    List<Waitlist> findByCourse_Code(String code);

    List<Waitlist> findByStudentEmail(String email);
}
