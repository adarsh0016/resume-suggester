package com.adarsh.resumed.repository;

import com.adarsh.resumed.DTO.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByUserId(Long id);
}
