package com.fsad.scm.service.impl;

import com.fsad.scm.entity.Course;
import com.fsad.scm.entity.Enrollment;
import com.fsad.scm.entity.User;
import com.fsad.scm.entity.Waitlist;
import com.fsad.scm.repository.CourseRepository;
import com.fsad.scm.repository.EnrollmentRepository;
import com.fsad.scm.repository.UserRepository;
import com.fsad.scm.repository.WaitlistRepository;
import com.fsad.scm.service.EnrollmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final WaitlistRepository waitlistRepository;
    private final UserRepository userRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 CourseRepository courseRepository,
                                 WaitlistRepository waitlistRepository,
                                 UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.waitlistRepository = waitlistRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Enrollment enroll(String email, String username, String courseCode) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        if (user.getRegistrationApproved() == null || user.getRegistrationApproved() != 1) {
            throw new RuntimeException("Registration not approved. Please wait for admin approval.");
        }

        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseCode));

        boolean alreadyEnrolled = enrollmentRepository.findByStudentEmail(email)
                .stream().anyMatch(e -> e.getCourse().getCode().equals(courseCode));
        if (alreadyEnrolled) {
            throw new RuntimeException("Already enrolled in: " + courseCode);
        }

        if (course.getEnrolled() >= course.getCapacity()) {
            boolean alreadyWaitlisted = waitlistRepository.findByStudentEmail(email)
                    .stream().anyMatch(w -> w.getCourse().getCode().equals(courseCode));
            if (alreadyWaitlisted) {
                throw new RuntimeException("Already on waitlist for: " + courseCode);
            }
            Waitlist waitlist = new Waitlist();
            waitlist.setStudentEmail(email);
            waitlist.setStudentUsername(username);
            waitlist.setCourse(course);
            waitlist.setTimestamp(LocalDateTime.now());
            waitlistRepository.save(waitlist);
            return null;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentEmail(email);
        enrollment.setStudentUsername(username);
        enrollment.setCourse(course);

        course.setEnrolled(course.getEnrolled() + 1);
        courseRepository.save(course);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional
    public void unenroll(String email, String courseCode) {

        List<Enrollment> enrollments = enrollmentRepository.findByStudentEmail(email);
        Enrollment target = enrollments.stream()
                .filter(e -> e.getCourse().getCode().equals(courseCode))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Enrollment not found for: " + email + " in " + courseCode));

        Course course = target.getCourse();
        enrollmentRepository.delete(target);

        if (course.getEnrolled() > 0) {
            course.setEnrolled(course.getEnrolled() - 1);
        }

        List<Waitlist> waiting = waitlistRepository.findByCourse_Code(courseCode);
        if (!waiting.isEmpty()) {
            Waitlist first = waiting.stream()
                    .min(java.util.Comparator.comparing(Waitlist::getTimestamp))
                    .get();

            Enrollment promoted = new Enrollment();
            promoted.setStudentEmail(first.getStudentEmail());
            promoted.setStudentUsername(first.getStudentUsername());
            promoted.setCourse(course);
            enrollmentRepository.save(promoted);

            course.setEnrolled(course.getEnrolled() + 1);
            waitlistRepository.delete(first);
        }

        courseRepository.save(course);
    }

    @Override
    public List<Enrollment> getStudentEnrollments(String email) {
        return enrollmentRepository.findByStudentEmail(email);
    }
}
