package com.example.movies.service;

import com.example.movies.model.News;
import com.example.movies.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<News> findAll() {
        return newsRepository.findAll();
    }

    public Optional<News> findById(Long id) {
        return newsRepository.findById(id);
    }

    public News save(News news) {
        return newsRepository.save(news);
    }

    public boolean existsById(Long id) {
        return newsRepository.existsById(id);
    }

    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }
} 