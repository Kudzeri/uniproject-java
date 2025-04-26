package com.example.movies.controller;

import com.example.movies.model.Role;
import com.example.movies.model.User;
import com.example.movies.repository.RoleRepository;
import com.example.movies.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.v3.oas.annotations.Parameter;
import com.example.movies.dto.UserDTO;
import com.example.movies.mapper.UserMapper;
import com.example.movies.service.UserService;
import com.example.movies.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    // Просмотр списка пользователей доступен ADMIN и TEACHER
    @Operation(summary = "Get All Users", description = "Returns all users. Доступен для: ADMIN, TEACHER")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @Parameter(description = "Номер страницы (0..N)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size) {
        Page<User> users = userService.findAll(page, size);
        Page<UserDTO> userDTOs = users.map(userMapper::toDTO);
        return ResponseEntity.ok(userDTOs);
    }

    // Получение данных конкретного пользователя, доступно ADMIN и TEACHER
    @Operation(summary = "Get User By ID", description = "Returns user by ID. Доступен для: ADMIN, TEACHER")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("Fetching user with id: {}", id);
        try {
            Optional<User> user = userRepository.findById(id);
            return user.map(ResponseEntity::ok)
                       .orElseGet(() -> {
                           logger.warn("User id {} not found", id);
                           return ResponseEntity.notFound().build();
                       });
        } catch(Exception e) {
            logger.error("Error fetching user with id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // Создание пользователя, доступно только для ADMIN
    @Operation(summary = "Create User", description = "Creates a new user. Доступен для: ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public User createUser(@RequestBody User user) {
        logger.info("Creating user: {}", user.getUsername());
        try {
            if(userRepository.findByUsername(user.getUsername()) != null) {
                throw new RuntimeException("Пользователь с таким именем уже существует!");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // По умолчанию назначаем роль ROLE_USER
            Role userRole = roleRepository.findByName("ROLE_USER");
            if(userRole == null) {
                userRole = new Role("ROLE_USER");
                roleRepository.save(userRole);
            }
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);
            return userRepository.save(user);
        } catch(Exception e) {
            logger.error("Error creating user {}: {}", user.getUsername(), e.getMessage(), e);
            throw e;
        }
    }

    // Обновление данных пользователя, доступно только для ADMIN
    @Operation(summary = "Update User", description = "Updates user data. Доступен для: ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        logger.info("Updating user with id: {}", id);
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if(optionalUser.isPresent()){
                User user = optionalUser.get();
                user.setUsername(userDetails.getUsername());
                if(userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()){
                    user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                }
                return ResponseEntity.ok(userRepository.save(user));
            }
            logger.warn("User id {} not found for update", id);
            return ResponseEntity.notFound().build();
        } catch(Exception e) {
            logger.error("Error updating user with id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // Удаление пользователя, доступно только для ADMIN
    @Operation(summary = "Delete User", description = "Deletes a user. Доступен для: ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with id: {}", id);
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if(optionalUser.isPresent()){
                userRepository.delete(optionalUser.get());
                return ResponseEntity.ok().build();
            }
            logger.warn("User id {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        } catch(Exception e) {
            logger.error("Error deleting user with id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // New endpoint: получение студентов (ROLE_USER) с пагинацией и сортировкой
    @GetMapping("/students")
    @Operation(summary = "Получить всех студентов", description = "Возвращает список всех студентов с пагинацией")
    public ResponseEntity<Page<UserDTO>> getStudents(
            @Parameter(description = "Номер страницы (0..N)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Фильтр по имени пользователя") @RequestParam(required = false) String nameLike) {
        Page<User> students = userService.findStudents(page, nameLike);
        Page<UserDTO> studentDTOs = students.map(userMapper::toDTO);
        return ResponseEntity.ok(studentDTOs);
    }

    @GetMapping("/debug/roles")
    @Operation(summary = "Debug: List all roles", description = "Lists all available roles in the system")
    public ResponseEntity<List<Map<String, String>>> listAllRoles() {
        List<Role> roles = roleRepository.findAll();
        List<Map<String, String>> roleInfo = roles.stream()
            .map(role -> Map.of(
                "id", role.getId().toString(),
                "name", role.getName(),
                "users", String.valueOf(role.getUsers().size())
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(roleInfo);
    }

    @GetMapping("/debug/user/{id}/roles")
    @Operation(summary = "Debug: Get user roles", description = "Lists all roles for a specific user")
    public ResponseEntity<Map<String, Object>> getUserRoles(@PathVariable Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("roles", user.getRoles().stream()
            .map(role -> Map.of(
                "id", role.getId(),
                "name", role.getName()
            ))
            .collect(Collectors.toList()));
        
        return ResponseEntity.ok(response);
    }
}