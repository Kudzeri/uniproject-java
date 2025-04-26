package com.example.movies.mapper;

import com.example.movies.dto.CourseDTO;
import com.example.movies.model.Course;
import com.example.movies.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    public CourseDTO toDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        
        if (course.getTeacher() != null) {
            dto.setTeacherId(course.getTeacher().getId());
        }
        
        if (course.getStudents() != null) {
            Set<Long> studentIds = course.getStudents().stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());
            dto.setStudentIds(studentIds);
        }
        
        return dto;
    }

    public Course toEntity(CourseDTO dto) {
        Course course = new Course();
        course.setId(dto.getId());
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        return course;
    }
} 