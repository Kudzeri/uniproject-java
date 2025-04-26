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

import com.example.movies.model.News;
import com.example.movies.model.User;
import com.example.movies.repository.NewsRepository;
import com.example.movies.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Order(4)
@Tag(name = "News Seeder", description = "API для инициализации тестовых новостей")
public class NewsSeeder implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(NewsSeeder.class);

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    @Operation(
        summary = "Инициализация тестовых новостей",
        description = "Создает тестовые новости с разными авторами"
    )
    @Override
    public void run(String... args) {
        if (newsRepository.count() == 0) {
            logger.info("Starting news seeding...");
            List<User> admins = userRepository.findByRoles_Name("ROLE_ADMIN");
            if (admins.isEmpty()) {
                logger.warn("No admin users found. Please run UserSeeder first.");
                return;
            }

            // Создаем новости о начале семестра
            News semesterStart = new News();
            semesterStart.setTitle("Начало нового семестра");
            semesterStart.setContent("Уважаемые студенты! Напоминаем, что новый семестр начинается 1 сентября. " +
                    "Расписание занятий уже доступно в личном кабинете.");
            semesterStart.setAuthor(admins.get(0));
            semesterStart.setCreatedAt(LocalDateTime.now().minusDays(5));
            newsRepository.save(semesterStart);

            // Создаем новости о мероприятиях
            News events = new News();
            events.setTitle("Спортивные соревнования");
            events.setContent("В следующую субботу состоятся ежегодные спортивные соревнования между факультетами. " +
                    "Регистрация участников открыта до конца недели.");
            events.setAuthor(admins.get(0));
            events.setCreatedAt(LocalDateTime.now().minusDays(3));
            newsRepository.save(events);

            // Создаем новости о достижениях
            News achievements = new News();
            achievements.setTitle("Наши студенты на олимпиаде");
            achievements.setContent("Команда нашего университета заняла первое место на международной олимпиаде " +
                    "по программированию. Поздравляем победителей!");
            achievements.setAuthor(admins.get(0));
            achievements.setCreatedAt(LocalDateTime.now().minusDays(1));
            newsRepository.save(achievements);

            logger.info("News seeding completed successfully");
        } else {
            logger.info("News already exist in the database. Skipping seeding.");
        }
    }
} 