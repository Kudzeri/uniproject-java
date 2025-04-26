package com.example.movies.controller;

import com.example.movies.model.Role;
import com.example.movies.model.User;
import com.example.movies.repository.RoleRepository;
import com.example.movies.repository.UserRepository;
import com.example.movies.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Operation(summary = "Регистрация нового пользователя", description = "Создание нового пользователя с ролью STUDENT")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        logger.info("Attempting to register new user: {}", registerRequest.getUsername());
        
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            logger.warn("Registration failed: username {} already exists", registerRequest.getUsername());
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            logger.warn("Registration failed: email {} already exists", registerRequest.getEmail());
            return ResponseEntity.badRequest().body("Email is already in use!");
        }

        // Создаем нового пользователя
        User user = new User(
            registerRequest.getUsername(),
            registerRequest.getEmail(),
            passwordEncoder.encode(registerRequest.getPassword()),
            registerRequest.getFirstName(),
            registerRequest.getLastName()
        );

        userRepository.save(user);
        logger.info("User {} successfully registered", registerRequest.getUsername());
        
        return ResponseEntity.ok("User registered successfully!");
    }

    @Operation(summary = "User Login", description = "Authenticates user and returns JWT. Доступен для: все")
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {
        logger.info("Attempting login for user: {}", username);
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return "Неверные данные (user not found)";
            }

            if (!passwordEncoder.matches(password, user.getPassword())) {
                return "Неверный пароль";
            }

            // Генерируем JWT
            String token = jwtUtils.generateToken(user.getUsername());
            logger.info("User {} logged in successfully", username);
            return token;
        } catch(Exception e) {
            logger.error("Error during login for {}: {}", username, e.getMessage(), e);
            return "Login failed: " + e.getMessage();
        }
    }

    @Operation(summary = "Admin Registration", description = "Registers a new admin. Доступен для: все (использовать только при инициализации)")
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRegisterRequest adminRequest) {
        logger.info("Attempting to register admin: {}", adminRequest.getUsername());
        try {
            // Проверка на существование пользователя
            if (userRepository.existsByUsername(adminRequest.getUsername())) {
                return ResponseEntity.badRequest().body("Username is already taken!");
            }
            
            // Получаем или создаём роль ROLE_ADMIN
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            if (adminRole == null) {
                adminRole = new Role("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }
            
            // Создаем нового администратора
            User newUser = new User(
                adminRequest.getUsername(),
                adminRequest.getEmail(),
                passwordEncoder.encode(adminRequest.getPassword()),
                adminRequest.getFirstName(),
                adminRequest.getLastName()
            );
            
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            newUser.setRoles(roles);
            userRepository.save(newUser);

            logger.info("Admin {} registered successfully", adminRequest.getUsername());
            String token = jwtUtils.generateToken(newUser.getUsername());
            return ResponseEntity.ok(token);
        } catch(Exception e) {
            logger.error("Error during admin registration for {}: {}", adminRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.badRequest().body("Admin registration failed: " + e.getMessage());
        }
    }
}

class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

class AdminRegisterRequest {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
