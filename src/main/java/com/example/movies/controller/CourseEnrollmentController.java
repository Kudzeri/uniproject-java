package com.example.movies.controller;

import com.example.movies.service.CourseEnrollmentService;
import com.example.movies.model.User;
import com.example.movies.model.Course;
import com.example.movies.repository.UserRepository;
import com.example.movies.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/enrollment")
public class CourseEnrollmentController {

    private static final Logger logger = LoggerFactory.getLogger(CourseEnrollmentController.class);

    @Autowired
    private CourseEnrollmentService enrollmentService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CourseRepository courseRepository;

    @Operation(
        summary = "Send enrollment notification",
        description = "Sends a notification to a user about their course enrollment."
    )
    @PostMapping("/notify")
    public ResponseEntity<String> sendEnrollmentNotification(
        @Parameter(description = "User ID", schema = @Schema(type = "long")) @RequestParam Long userId,
        @Parameter(description = "Course ID", schema = @Schema(type = "long")) @RequestParam Long courseId
    ) {
        logger.info("Received request to send enrollment notification for user {} and course {}", userId, courseId);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
            
        enrollmentService.sendEnrollmentNotification(user, course);
        return ResponseEntity.ok("Уведомление о записи на курс отправлено");
    }
} 