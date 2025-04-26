package com.example.movies.repository;

import com.example.movies.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStartTimeAfter(LocalDateTime dateTime);
    List<Event> findByEndTimeBefore(LocalDateTime dateTime);
}
