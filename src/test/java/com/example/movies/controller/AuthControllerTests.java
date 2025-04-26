package com.example.movies.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Тест: Регистрация нового пользователя
    @Test
    public void testRegister() throws Exception {
        mockMvc.perform(post("/auth/register")
                .param("username", "testuser")
                .param("password", "testpass"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("eyJ"))); // проверка что возвращается JWT (начинается с eyJ)
    }
    
    // Тест: Логин пользователя (успешно)
    @Test
    public void testLoginSuccess() throws Exception {
        // Для логина необходимо, чтобы пользователь с таким именем уже был создан.
        // В данном тесте предполагается, что тестовые данные уже доступны.
        mockMvc.perform(post("/auth/login")
                .param("username", "testuser")
                .param("password", "testpass"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("eyJ")));
    }
    
    // Тест: Регистрация админа
    @Test
    public void testRegisterAdmin() throws Exception {
        mockMvc.perform(post("/auth/register-admin")
                .param("username", "testadmin")
                .param("password", "adminpass"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("eyJ")));
    }
}
