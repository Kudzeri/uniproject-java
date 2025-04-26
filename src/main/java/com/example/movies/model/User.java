package com.example.movies.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean subscribedToNewsletter;

    // У пользователя может быть несколько ролей
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    // New: courses in which the user (as student) is enrolled
    @ManyToMany(mappedBy = "students")
    @JsonIgnore
    private Set<Course> coursesEnrolled = new HashSet<>();
    
    // New: courses the user (as teacher) teaches
    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    private Set<Course> coursesTaught = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    public User() {
    }

    public User(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // getters/setters
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
    
    public String getPassword() {
       return password;
    }
    
    public void setPassword(String password) {
       this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isSubscribedToNewsletter() {
        return subscribedToNewsletter;
    }

    public void setSubscribedToNewsletter(boolean subscribedToNewsletter) {
        this.subscribedToNewsletter = subscribedToNewsletter;
    }

    public Set<Role> getRoles() {
       return roles;
    }

    public void setRoles(Set<Role> roles) {
       this.roles = roles;
    }

    // New getters/setters for coursesEnrolled
    public Set<Course> getCoursesEnrolled() {
        return coursesEnrolled;
    }

    public void setCoursesEnrolled(Set<Course> coursesEnrolled) {
        this.coursesEnrolled = coursesEnrolled;
    }

    // New getters/setters for coursesTaught
    public Set<Course> getCoursesTaught() {
        return coursesTaught;
    }

    public void setCoursesTaught(Set<Course> coursesTaught) {
        this.coursesTaught = coursesTaught;
    }

    // New getter for createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
