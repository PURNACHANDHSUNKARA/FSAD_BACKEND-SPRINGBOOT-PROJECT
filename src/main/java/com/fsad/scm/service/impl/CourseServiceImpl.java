package com.fsad.scm.service.impl;

import com.fsad.scm.entity.Course;
import com.fsad.scm.repository.CourseRepository;
import com.fsad.scm.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course updateCourse(String code, Course updated) {
        Course existing = courseRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Course not found: " + code));
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getDay() != null) existing.setDay(updated.getDay());
        if (updated.getTime() != null) existing.setTime(updated.getTime());
        if (updated.getCredits() > 0) existing.setCredits(updated.getCredits());
        if (updated.getCapacity() > 0) existing.setCapacity(updated.getCapacity());
        if (updated.getPrerequisites() != null) existing.setPrerequisites(updated.getPrerequisites());
        return courseRepository.save(existing);
    }

    @Override
    public void deleteCourse(String code) {
        courseRepository.deleteById(code);
    }
}
