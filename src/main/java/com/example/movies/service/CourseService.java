/*
 * MIT License
 *
 * Copyright (c) 2025 Kudzeri
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.movies.service;

import com.example.movies.exception.ResourceNotFoundException;
import com.example.movies.model.Course;
import com.example.movies.model.User;
import com.example.movies.model.Role;
import com.example.movies.repository.CourseRepository;
import com.example.movies.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course courseDetails) {
        Course course = getCourseById(id);
        course.setTitle(courseDetails.getTitle());
        course.setDescription(courseDetails.getDescription());
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }

    @Transactional
    public Course addStudentToCourse(Long courseId, Long studentId) {
        Course course = getCourseById(courseId);
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + studentId));
        
        course.getStudents().add(student);
        return courseRepository.save(course);
    }

    @Transactional
    public Course removeStudentFromCourse(Long courseId, Long studentId) {
        Course course = getCourseById(courseId);
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + studentId));
        
        course.getStudents().remove(student);
        return courseRepository.save(course);
    }

    @Transactional
    public Course assignTeacherToCourse(Long courseId, Long teacherId) {
        Course course = getCourseById(courseId);
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + teacherId));
        
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    @Transactional
    public void enrollStudent(Long courseId, Long studentId) {
        logger.info("Enrolling student {} to course {}", studentId, courseId);
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    logger.error("Course not found: {}", courseId);
                    return new ResourceNotFoundException("Course not found");
                });
        
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("Student not found: {}", studentId);
                    return new ResourceNotFoundException("Student not found");
                });
        
        // Log user details and roles
        logger.info("User details - ID: {}, Username: {}, Roles: {}", 
                   student.getId(), 
                   student.getUsername(),
                   student.getRoles().stream()
                          .map(Role::getName)
                          .collect(Collectors.joining(", ")));
        
        // Check if user is a student
        boolean isStudent = student.getRoles().stream()
                .anyMatch(r -> r.getName().equalsIgnoreCase("ROLE_STUDENT"));
        
        if (!isStudent) {
            logger.error("User {} is not a student. Current roles: {}", 
                        student.getUsername(),
                        student.getRoles().stream()
                               .map(Role::getName)
                               .collect(Collectors.joining(", ")));
            throw new IllegalArgumentException("User is not a student");
        }
        
        // Check if student is already enrolled
        if (course.getStudents().contains(student)) {
            logger.error("Student {} is already enrolled in course {}", studentId, courseId);
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }
        
        try {
            course.getStudents().add(student);
            courseRepository.save(course);
            logger.info("Successfully enrolled student {} to course {}", studentId, courseId);
        } catch (Exception e) {
            logger.error("Error saving course enrollment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save course enrollment", e);
        }
    }

    public void assignTeacher(Long courseId, Long teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        
        // Check if user is a teacher
        if (!teacher.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_TEACHER"))) {
            throw new IllegalArgumentException("User is not a teacher");
        }
        
        course.setTeacher(teacher);
        courseRepository.save(course);
    }
} 