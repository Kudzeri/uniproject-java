package com.example.movies.mapper;

import com.example.movies.dto.NewsDTO;
import com.example.movies.model.News;
import org.springframework.stereotype.Component;

@Component
public class NewsMapper {
    
    public NewsDTO toDTO(News news) {
        if (news == null) {
            return null;
        }
        
        NewsDTO dto = new NewsDTO();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setContent(news.getContent());
        dto.setCreatedAt(news.getCreatedAt());
        
        if (news.getAuthor() != null) {
            dto.setAuthorId(news.getAuthor().getId());
            dto.setAuthorUsername(news.getAuthor().getUsername());
        }
        
        return dto;
    }
    
    public News toEntity(NewsDTO dto) {
        if (dto == null) {
            return null;
        }
        
        News news = new News();
        news.setId(dto.getId());
        news.setTitle(dto.getTitle());
        news.setContent(dto.getContent());
        
        // Note: We don't set the author here as it should be handled by the service layer
        // to ensure proper user existence and permissions
        
        return news;
    }
} 