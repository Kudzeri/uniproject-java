package com.example.movies.repository;

import com.example.movies.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE " +
           "(:title IS NULL OR c.title = :title) AND " +
           "(:description IS NULL OR c.description LIKE %:description%) AND " +
           "(:teacherName IS NULL OR c.teacher.username LIKE %:teacherName%)")
    Page<Course> findByFilters(String title, String description, String teacherName, Pageable pageable);
}
