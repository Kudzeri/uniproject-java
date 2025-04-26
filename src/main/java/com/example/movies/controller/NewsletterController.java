package com.example.movies.controller;

import com.example.movies.model.User;
import com.example.movies.repository.UserRepository;
import com.example.movies.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/newsletter")
public class NewsletterController {

    private static final Logger logger = LoggerFactory.getLogger(NewsletterController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Operation(summary = "Subscribe to Newsletter", description = "Subscribe a user to the newsletter")
    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestParam String email) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }
            user.setSubscribedToNewsletter(true);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Successfully subscribed to newsletter"));
        } catch (Exception e) {
            logger.error("Error subscribing to newsletter: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to subscribe"));
        }
    }

    @Operation(summary = "Unsubscribe from Newsletter", description = "Unsubscribe a user from the newsletter")
    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@RequestParam String email) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }
            user.setSubscribedToNewsletter(false);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Successfully unsubscribed from newsletter"));
        } catch (Exception e) {
            logger.error("Error unsubscribing from newsletter: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to unsubscribe"));
        }
    }

    @Operation(summary = "Send Newsletter", description = "Send newsletter to all subscribed users. Доступен для: ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/send")
    public ResponseEntity<?> sendNewsletter(@RequestParam String subject, @RequestParam String message) {
        try {
            List<User> subscribers = userRepository.findBySubscribedToNewsletter(true);
            for (User subscriber : subscribers) {
                emailService.sendNewsletterEmail(subscriber.getEmail(), subject, message);
            }
            return ResponseEntity.ok(Map.of("message", "Newsletter sent successfully", "recipients", subscribers.size()));
        } catch (Exception e) {
            logger.error("Error sending newsletter: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to send newsletter"));
        }
    }
} 