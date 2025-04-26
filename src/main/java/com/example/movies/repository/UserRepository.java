package com.example.movies.repository;

import com.example.movies.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRoles_Name(String roleName);
    User findByUsername(String username);
    
    // New method for pagination by role name (например, для студентов)
    Page<User> findByRoles_Name(String name, Pageable pageable);

    @Query("SELECT u FROM User u WHERE (:username IS NULL OR u.username = :username)")
    Page<User> findByUsername(String username, Pageable pageable);

    @Query("SELECT u FROM User u WHERE (:usernameLike IS NULL OR u.username LIKE %:usernameLike%)")
    Page<User> findByUsernameLike(String usernameLike, Pageable pageable);

    User findByEmail(String email);

    List<User> findBySubscribedToNewsletter(boolean subscribed);

    Page<User> findByRoles_NameAndUsernameContainingIgnoreCase(String roleName, String username, Pageable pageable);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
