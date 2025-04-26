package com.example.movies.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Set;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    
    @JsonIgnore
    private String password;
    
    @JsonIgnore
    private Boolean subscribedToNewsletter;
    
    @JsonIgnore
    private LocalDateTime createdAt;
    
    private Set<String> roles;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getSubscribedToNewsletter() {
        return subscribedToNewsletter;
    }

    public void setSubscribedToNewsletter(Boolean subscribedToNewsletter) {
        this.subscribedToNewsletter = subscribedToNewsletter;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
} 