package com.example.movies.controller;

import com.example.movies.dto.NewsDTO;
import com.example.movies.mapper.NewsMapper;
import com.example.movies.model.News;
import com.example.movies.model.User;
import com.example.movies.service.NewsService;
import com.example.movies.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/news")
@Tag(name = "News Controller", description = "API для управления новостями")
public class NewsController {

    private final NewsService newsService;
    private final NewsMapper newsMapper;
    private final UserService userService;

    @Autowired
    public NewsController(NewsService newsService, NewsMapper newsMapper, UserService userService) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
        this.userService = userService;
    }

    @Operation(summary = "Получить все новости")
    @GetMapping
    public ResponseEntity<List<NewsDTO>> getAllNews() {
        List<News> news = newsService.findAll();
        List<NewsDTO> newsDTOs = news.stream()
                .map(newsMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(newsDTOs);
    }

    @Operation(summary = "Получить новость по ID")
    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable Long id) {
        return newsService.findById(id)
                .map(newsMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать новость")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewsDTO> createNews(@RequestBody NewsDTO newsDTO, Principal principal) {
        String username = principal.getName();
        User author = userService.getUserByUsername(username);
        
        News news = newsMapper.toEntity(newsDTO);
        news.setAuthor(author);
        
        News savedNews = newsService.save(news);
        return ResponseEntity.ok(newsMapper.toDTO(savedNews));
    }

    @Operation(summary = "Обновить новость")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewsDTO> updateNews(@PathVariable Long id, @RequestBody NewsDTO newsDTO, Principal principal) {
        if (!newsService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        String username = principal.getName();
        User author = userService.getUserByUsername(username);
        
        News news = newsMapper.toEntity(newsDTO);
        news.setId(id);
        news.setAuthor(author);
        
        News updatedNews = newsService.save(news);
        return ResponseEntity.ok(newsMapper.toDTO(updatedNews));
    }

    @Operation(summary = "Удалить новость")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        if (!newsService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        newsService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
