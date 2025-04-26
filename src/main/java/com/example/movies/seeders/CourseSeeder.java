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

import com.example.movies.model.Course;
import com.example.movies.model.User;
import com.example.movies.repository.CourseRepository;
import com.example.movies.repository.UserRepository;
import com.example.movies.util.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

@Component
@Order(2)
@Tag(name = "Course Seeder", description = "API для инициализации тестовых курсов")
public class CourseSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CourseSeeder.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Operation(
        summary = "Инициализация тестовых курсов",
        description = "Создает тестовые курсы и назначает им случайных преподавателей если база данных пуста"
    )
    @Override
    public void run(String... args) {
        if (courseRepository.count() == 0) {
            logger.info("Starting course seeding");
            List<User> teachers = userRepository.findByRoles_Name("ROLE_TEACHER");
            if (teachers.isEmpty()) {
                logger.warn("No teachers found in the database. Please seed users first.");
                return;
            }

            Random random = new Random();
            for (int i = 0; i < 20; i++) {
                Course course = new Course();
                course.setTitle(DataGenerator.generateCourseTitle());
                course.setDescription(DataGenerator.generateCourseDescription());
                
                // Назначаем случайного преподавателя
                User teacher = teachers.get(random.nextInt(teachers.size()));
                course.setTeacher(teacher);
                
                courseRepository.save(course);
            }
            logger.info("Course seeding completed successfully");
        }
    }
} 