package com.example.movies.controller;

import com.example.movies.dto.CourseDTO;
import com.example.movies.exception.ResourceNotFoundException;
import com.example.movies.mapper.CourseMapper;
import com.example.movies.model.Course;
import com.example.movies.model.User;
import com.example.movies.service.CourseService;
import com.example.movies.service.NotificationService;
import com.example.movies.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Course API", description = "API для управления курсами")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Получить все курсы", description = "Возвращает список всех курсов")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        List<CourseDTO> courseDTOs = courses.stream()
                .map(courseMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courseDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить курс по ID", description = "Возвращает курс с указанным ID")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(courseMapper.toDTO(course));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создать курс", description = "Создает новый курс. Доступно только для администраторов")
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        Course course = courseMapper.toEntity(courseDTO);
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.ok(courseMapper.toDTO(createdCourse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить курс", description = "Обновляет существующий курс. Доступно только для администраторов")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
        Course course = courseMapper.toEntity(courseDTO);
        Course updatedCourse = courseService.updateCourse(id, course);
        return ResponseEntity.ok(courseMapper.toDTO(updatedCourse));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить курс", description = "Удаляет курс с указанным ID. Доступно только для администраторов")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseId}/students/{studentId}")
    @Operation(summary = "Записать студента на курс", 
               description = "Записывает студента на курс. Доступно для: ADMIN, TEACHER (только для своих курсов)")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<?> enrollStudent(
            @Parameter(description = "ID курса") @PathVariable Long courseId,
            @Parameter(description = "ID студента") @PathVariable Long studentId,
            Principal principal) {
        try {
            logger.info("Attempting to enroll student {} to course {}", studentId, courseId);
            
            // Get authenticated user
            User currentUser = userService.getUserByUsername(principal.getName());
            logger.info("Current user: {}", currentUser.getUsername());
            
            // Check if user is teacher of this course
            Course course = courseService.getCourseById(courseId);
            if (course == null) {
                logger.error("Course not found: {}", courseId);
                throw new ResourceNotFoundException("Course not found");
            }
            logger.info("Found course: {}", course.getTitle());
            
            if (!currentUser.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN")) &&
                !course.getTeacher().getId().equals(currentUser.getId())) {
                logger.error("User {} is not authorized to enroll students in course {}", 
                           currentUser.getUsername(), courseId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only course teacher or admin can enroll students"));
            }

            // Enroll student
            courseService.enrollStudent(courseId, studentId);
            logger.info("Successfully enrolled student {} to course {}", studentId, courseId);
            
            // Send notification
            User student = userService.getUserById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
            notificationService.createNotification(
                student,
                "Зачисление на курс",
                String.format("Вы были зачислены на курс \"%s\"", course.getTitle())
            );
            
            return ResponseEntity.ok(Map.of("message", "Student enrolled successfully"));
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error enrolling student: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to enroll student: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{courseId}/students/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Отчислить студента с курса", 
               description = "Отчисляет студента с курса. Доступно только для администраторов")
    public ResponseEntity<CourseDTO> removeStudentFromCourse(
            @Parameter(description = "ID курса") @PathVariable Long courseId,
            @Parameter(description = "ID студента") @PathVariable Long studentId) {
        Course course = courseService.removeStudentFromCourse(courseId, studentId);
        return ResponseEntity.ok(courseMapper.toDTO(course));
    }

    @PostMapping("/{courseId}/teacher/{teacherId}")
    @Operation(summary = "Назначить преподавателя на курс", 
               description = "Назначает преподавателя на курс. Доступно только для: ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignTeacher(
            @Parameter(description = "ID курса") @PathVariable Long courseId,
            @Parameter(description = "ID преподавателя") @PathVariable Long teacherId) {
        try {
            courseService.assignTeacher(courseId, teacherId);
            return ResponseEntity.ok(Map.of("message", "Teacher assigned successfully"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to assign teacher"));
        }
    }
} 