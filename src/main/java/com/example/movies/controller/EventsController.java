package com.example.movies.controller;

import com.example.movies.model.Event;
import com.example.movies.model.User;
import com.example.movies.repository.EventRepository;
import com.example.movies.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Events", description = "API для управления событиями")
public class EventsController {

    private static final Logger logger = LoggerFactory.getLogger(EventsController.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Operation(
        summary = "Получить все события",
        description = "Возвращает список всех событий. Доступно для аутентифицированных пользователей"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Список событий успешно получен",
        content = @Content(schema = @Schema(implementation = Event.class))
    )
    @GetMapping
    public List<Event> getAllEvents() {
        logger.info("Fetching all events");
        return eventRepository.findAll();
    }

    @Operation(
        summary = "Получить событие по ID",
        description = "Возвращает событие по указанному идентификатору. Доступно для аутентифицированных пользователей"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Событие успешно найдено",
            content = @Content(schema = @Schema(implementation = Event.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Событие не найдено"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(
        @Parameter(description = "ID события", required = true)
        @PathVariable Long id
    ) {
        logger.info("Fetching event with id: {}", id);
        Optional<Event> event = eventRepository.findById(id);
        return event.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Создать новое событие",
        description = "Создает новое событие. Доступно только для администраторов"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Событие успешно создано",
            content = @Content(schema = @Schema(implementation = Event.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос или пользователь не является преподавателем"
        )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Event> createEvent(
        @Parameter(description = "Данные нового события", required = true)
        @RequestBody Event event
    ) {
        logger.info("Creating event: {}", event.getTitle());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User teacher = userRepository.findByUsername(username);
        
        if (teacher == null || !teacher.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_TEACHER"))) {
            return ResponseEntity.badRequest().build();
        }

        event.setTeacher(teacher);
        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }

    @Operation(
        summary = "Обновить событие",
        description = "Обновляет существующее событие. Доступно только для администраторов"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Событие успешно обновлено",
            content = @Content(schema = @Schema(implementation = Event.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Событие не найдено"
        )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(
        @Parameter(description = "ID события", required = true)
        @PathVariable Long id,
        @Parameter(description = "Обновленные данные события", required = true)
        @RequestBody Event eventDetails
    ) {
        logger.info("Updating event with id: {}", id);
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        event.setTitle(eventDetails.getTitle());
        event.setDescription(eventDetails.getDescription());
        event.setStartTime(eventDetails.getStartTime());
        event.setEndTime(eventDetails.getEndTime());
        event.setLocation(eventDetails.getLocation());
        event.setType(eventDetails.getType());
        event.setTeacher(eventDetails.getTeacher());

        Event updatedEvent = eventRepository.save(event);
        return ResponseEntity.ok(updatedEvent);
    }

    @Operation(
        summary = "Удалить событие",
        description = "Удаляет событие по указанному идентификатору. Доступно только для администраторов"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Событие успешно удалено"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Событие не найдено"
        )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
        @Parameter(description = "ID события", required = true)
        @PathVariable Long id
    ) {
        logger.info("Deleting event with id: {}", id);
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Получить предстоящие события",
        description = "Возвращает список предстоящих событий. Доступно для аутентифицированных пользователей"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Список предстоящих событий успешно получен",
        content = @Content(schema = @Schema(implementation = Event.class))
    )
    @GetMapping("/upcoming")
    public List<Event> getUpcomingEvents() {
        logger.info("Fetching upcoming events");
        return eventRepository.findByStartTimeAfter(LocalDateTime.now());
    }

    @Operation(
        summary = "Получить прошедшие события",
        description = "Возвращает список прошедших событий. Доступно для аутентифицированных пользователей"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Список прошедших событий успешно получен",
        content = @Content(schema = @Schema(implementation = Event.class))
    )
    @GetMapping("/past")
    public List<Event> getPastEvents() {
        logger.info("Fetching past events");
        return eventRepository.findByEndTimeBefore(LocalDateTime.now());
    }
}