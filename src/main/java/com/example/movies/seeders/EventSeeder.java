package com.example.movies.seeders;

import com.example.movies.model.Event;
import com.example.movies.model.User;
import com.example.movies.repository.EventRepository;
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
@Order(3)
@Tag(name = "Event Seeder", description = "API для инициализации тестовых событий")
public class EventSeeder implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(EventSeeder.class);
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Operation(summary = "Инициализация тестовых событий", 
              description = "Создает тестовые события для преподавателей, если база данных пуста")
    @Override
    public void run(String... args) {
        logger.info("Starting event seeding...");
        
        if (eventRepository.count() > 0) {
            logger.info("Events already exist in the database. Skipping seeding.");
            return;
        }
        
        try {
            List<User> teachers = userRepository.findByRoles_Name("ROLE_TEACHER");
            if (teachers.isEmpty()) {
                logger.warn("No teachers found in the database. Cannot create events.");
                return;
            }
            
            LocalDateTime now = LocalDateTime.now();
            
            // Создаем лекцию
            Event lecture = new Event();
            lecture.setTitle("Введение в программирование");
            lecture.setDescription("Базовые концепции программирования");
            lecture.setStartTime(now.plusDays(1).withHour(10).withMinute(0));
            lecture.setEndTime(now.plusDays(1).withHour(11).withMinute(30));
            lecture.setType("LECTURE");
            lecture.setLocation("Аудитория 101");
            lecture.setTeacher(teachers.get(0));
            eventRepository.save(lecture);
            
            // Создаем практическое занятие
            Event practice = new Event();
            practice.setTitle("Практика по программированию");
            practice.setDescription("Решение практических задач");
            practice.setStartTime(now.plusDays(2).withHour(14).withMinute(0));
            practice.setEndTime(now.plusDays(2).withHour(15).withMinute(30));
            practice.setType("PRACTICE");
            practice.setLocation("Компьютерный класс 1");
            practice.setTeacher(teachers.get(1));
            eventRepository.save(practice);
            
            // Создаем экзамен
            Event exam = new Event();
            exam.setTitle("Экзамен по программированию");
            exam.setDescription("Итоговый экзамен по курсу");
            exam.setStartTime(now.plusDays(7).withHour(9).withMinute(0));
            exam.setEndTime(now.plusDays(7).withHour(12).withMinute(0));
            exam.setType("EXAM");
            exam.setLocation("Актовый зал");
            exam.setTeacher(teachers.get(2));
            eventRepository.save(exam);
            
            logger.info("Event seeding completed successfully");
        } catch (Exception e) {
            logger.error("Error during event seeding: {}", e.getMessage());
            throw e;
        }
    }
} 