package com.fsad.scm.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_email", nullable = false)
    private String studentEmail;

    @Column(name = "student_username")
    private String studentUsername;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_code", nullable = false)
    @JsonIgnoreProperties({"enrollments", "waitlistEntries"})
    private Course course;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getStudentUsername() { return studentUsername; }
    public void setStudentUsername(String studentUsername) { this.studentUsername = studentUsername; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}
