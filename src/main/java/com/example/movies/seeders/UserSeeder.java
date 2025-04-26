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
package com.example.movies.seeders;

import com.example.movies.model.Role;
import com.example.movies.model.User;
import com.example.movies.repository.RoleRepository;
import com.example.movies.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@Order(1)
@Tag(name = "User Seeder", description = "API для инициализации тестовых пользователей")
public class UserSeeder implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(UserSeeder.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private final String[] firstNames = {
        "Ivan", "Petr", "Sergey", "Alexey", "Dmitry", "Andrey", "Mikhail", "Vladimir", "Nikolay", "Konstantin",
        "Anna", "Maria", "Elena", "Olga", "Natalia", "Tatiana", "Ekaterina", "Irina", "Svetlana", "Yulia"
    };
    
    private final String[] lastNames = {
        "Ivanov", "Petrov", "Sidorov", "Smirnov", "Kuznetsov", "Popov", "Vasiliev", "Mikhailov", "Novikov", "Fedorov",
        "Yakovlev", "Morozov", "Volkov", "Sokolov", "Bogdanov", "Zaitsev", "Pavlov", "Semenov", "Golubev", "Vinogradov"
    };
    
    @Operation(summary = "Инициализация тестовых пользователей", 
              description = "Создает тестовых пользователей с разными ролями, если база данных пуста")
    @Override
    public void run(String... args) {
        logger.info("Starting user seeding...");
        
        if (userRepository.count() > 0) {
            logger.info("Users already exist in the database. Skipping seeding.");
            return;
        }
        
        try {
            // Создаем роли, если их нет
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            if (adminRole == null) {
                adminRole = new Role("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }
                
            Role teacherRole = roleRepository.findByName("ROLE_TEACHER");
            if (teacherRole == null) {
                teacherRole = new Role("ROLE_TEACHER");
                roleRepository.save(teacherRole);
            }
                
            Role studentRole = roleRepository.findByName("ROLE_STUDENT");
            if (studentRole == null) {
                studentRole = new Role("ROLE_STUDENT");
                roleRepository.save(studentRole);
            }
            
            // Создаем админов
            for (int i = 0; i < 10; i++) {
                String firstName = firstNames[i % firstNames.length];
                String lastName = lastNames[i % lastNames.length];
                String email = String.format("admin%d.%s.%s@example.com", i + 1, firstName.toLowerCase(), lastName.toLowerCase());
                String username = "admin" + (i + 1);
                
                User admin = new User(username, email, passwordEncoder.encode("password123"), firstName, lastName);
                admin.setRoles(new HashSet<>(Arrays.asList(adminRole)));
                userRepository.save(admin);
            }
            
            // Создаем преподавателей
            for (int i = 0; i < 20; i++) {
                String firstName = firstNames[i % firstNames.length];
                String lastName = lastNames[i % lastNames.length];
                String email = String.format("teacher%d.%s.%s@example.com", i + 1, firstName.toLowerCase(), lastName.toLowerCase());
                String username = "teacher" + (i + 1);
                
                User teacher = new User(username, email, passwordEncoder.encode("password123"), firstName, lastName);
                teacher.setRoles(new HashSet<>(Arrays.asList(teacherRole)));
                userRepository.save(teacher);
            }
            
            // Создаем студентов с годами в email
            for (int i = 0; i < 100; i++) {
                String firstName = firstNames[i % firstNames.length];
                String lastName = lastNames[i % lastNames.length];
                int year = 2002 + (i % 5); // Годы от 2002 до 2006
                String email = String.format("student%d.%s.%s.%d@example.com", i + 1, firstName.toLowerCase(), lastName.toLowerCase(), year);
                String username = "student" + (i + 1);
                
                User student = new User(username, email, passwordEncoder.encode("password123"), firstName, lastName);
                student.setRoles(new HashSet<>(Arrays.asList(studentRole)));
                userRepository.save(student);
            }
            
            logger.info("User seeding completed successfully");
        } catch (Exception e) {
            logger.error("Error during user seeding: {}", e.getMessage());
            throw e;
        }
    }
} 