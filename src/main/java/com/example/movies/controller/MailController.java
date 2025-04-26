package com.example.movies.controller;

import com.example.movies.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/mail")
public class MailController {

    private static final Logger logger = LoggerFactory.getLogger(MailController.class);

    @Autowired
    private EmailService emailService;

    @Operation(summary = "Send welcome email", description = "Sends a welcome email to the university.")
    @GetMapping("/welcome")
    public ResponseEntity<String> sendWelcome() {
        logger.info("Received request to send welcome email");
        emailService.sendWelcomeEmail();
        return ResponseEntity.ok("Welcome email sent to university");
    }

    @Operation(
        summary = "Submit application",
        description = "Accepts name and email, and sends an application confirmation email."
    )
    @PostMapping("/apply")
    public ResponseEntity<String> submitApplication(
        @Parameter(description = "Name of the applicant", schema = @Schema(type = "string")) @RequestParam String name,
        @Parameter(description = "Email of the applicant", schema = @Schema(type = "string")) @RequestParam String email
    ) {
        logger.info("Received application from {} with email {}", name, email);
        emailService.sendApplicationSubmissionEmail(name, email);
        return ResponseEntity.ok("Заявка подана, " + name);
    }
}
