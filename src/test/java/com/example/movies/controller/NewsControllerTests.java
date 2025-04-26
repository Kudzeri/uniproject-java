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
public class NewsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Тест: GET /news (доступно для всех, но проверяем через аутентификацию)
    @Test
    @WithMockUser
    public void testGetAllNews() throws Exception {
        mockMvc.perform(get("/news"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
    
    // Тест: GET /news/{id} с несуществующим id
    @Test
    @WithMockUser
    public void testGetNewsByIdNotFound() throws Exception {
        mockMvc.perform(get("/news/99999"))
            .andExpect(status().isNotFound());
    }
}
