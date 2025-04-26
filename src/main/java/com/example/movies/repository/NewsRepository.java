package com.example.movies.repository;

import com.example.movies.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("SELECT n FROM News n WHERE " +
           "(:title IS NULL OR n.title = :title) AND " +
           "(:likeTitle IS NULL OR n.title LIKE %:likeTitle%)")
    Page<News> findByFilters(@Param("title") String title, 
                             @Param("likeTitle") String likeTitle, 
                             Pageable pageable);
}
