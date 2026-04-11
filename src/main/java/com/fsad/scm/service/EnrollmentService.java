package com.fsad.scm.service;

import com.fsad.scm.entity.Enrollment;

import java.util.List;

public interface EnrollmentService {

    Enrollment enroll(String email, String username, String courseCode);

    void unenroll(String email, String courseCode);

    List<Enrollment> getStudentEnrollments(String email);
}
