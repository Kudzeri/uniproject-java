package com.example.movies.service;

import com.example.movies.model.User;
import com.example.movies.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CourseEnrollmentService {
    
    private static final Logger logger = LoggerFactory.getLogger(CourseEnrollmentService.class);
    
    @Autowired
    private NotificationService notificationService;
    
    public void sendEnrollmentNotification(User user, Course course) {
        String message = String.format("Вы успешно записались на курс: %s", course.getTitle());
        notificationService.createNotification(user, "Запись на курс", message);
        logger.info("Sent enrollment notification to user {} for course {}", user.getUsername(), course.getTitle());
    }
} 