package com.adarsh.resumed.repository;

import com.adarsh.resumed.DTO.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}
