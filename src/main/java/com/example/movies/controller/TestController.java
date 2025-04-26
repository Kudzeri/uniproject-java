package com.example.movies.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    
    @Operation(summary = "Hello Endpoint", description = "Accessible by authenticated users. Доступен для: аутентифицированных пользователей")
    @GetMapping("/hello")
    public String hello() {
        logger.info("Accessing hello endpoint");
        try {
            return "Hello, authorized user!";
        } catch(Exception e) {
            logger.error("Error on hello endpoint: {}", e.getMessage(), e);
            return "Error: " + e.getMessage();
        }
    }

    @Operation(summary = "Admin Endpoint", description = "Accessible only by ADMIN. Доступен для: ADMIN")
    @GetMapping("/admin")
    public String adminAccess() {
        logger.info("Accessing admin endpoint");
        try {
            return "Hello, ADMIN!";
        } catch(Exception e) {
            logger.error("Error on admin endpoint: {}", e.getMessage(), e);
            return "Error: " + e.getMessage();
        }
    }
}
