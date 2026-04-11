package com.fsad.scm.service;

import com.fsad.scm.entity.Course;

import java.util.List;

public interface CourseService {

    Course addCourse(Course course);

    List<Course> getAllCourses();

    Course updateCourse(String code, Course course);

    void deleteCourse(String code);
}
