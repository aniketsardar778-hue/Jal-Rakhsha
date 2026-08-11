package com.demo.jalrakhsa.repository;

import com.demo.jalrakhsa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<User,Long>
{
    Optional<User> findByUsernameIgnoreCase(String username);
        Optional<User> findByEmail(String email);

        boolean existsByEmail(String email);
    }
