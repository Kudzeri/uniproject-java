package com.example.movies.service;

import com.example.movies.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void createNotification(User user, String title, String message) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            logger.warn("Cannot send email: User {} has no email address", user.getUsername());
            return;
        }
        
        try {
            logger.info("Attempting to send email to {}", user.getEmail());
            
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(user.getEmail());
            email.setSubject("Запись на курс");
            email.setText(message);
            
            mailSender.send(email);
            logger.info("Email sent successfully to {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
            logger.error("Error details: {}", e.getClass().getName());
            if (e.getCause() != null) {
                logger.error("Caused by: {}", e.getCause().getMessage());
            }
        }
    }
} 