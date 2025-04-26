package com.example.movies.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Тест: GET /events (обычный эндпоинт для аутентифицированных пользователей)
    @Test
    @WithMockUser // можно добавить роли, если требуется
    public void testGetAllEvents() throws Exception {
        mockMvc.perform(get("/events"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").exists());
    }
    
    // Тест: GET /events/upcoming
    @Test
    @WithMockUser
    public void testGetUpcomingEvents() throws Exception {
        mockMvc.perform(get("/events/upcoming"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
    
    // Тест: GET /events/past
    @Test
    @WithMockUser
    public void testGetPastEvents() throws Exception {
        mockMvc.perform(get("/events/past"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
}
