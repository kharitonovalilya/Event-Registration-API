package com.kharitonovalilya.event_registration_api.repository;

import com.kharitonovalilya.event_registration_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
