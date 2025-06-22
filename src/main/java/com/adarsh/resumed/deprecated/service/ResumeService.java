package com.adarsh.resumed.deprecated.service;

import com.adarsh.resumed.DTO.Resume;
import com.adarsh.resumed.DTO.Users;
import com.adarsh.resumed.repository.ResumeRepository;
import com.adarsh.resumed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ResumeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private S3Service s3Service;

    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    public void upload(String username, String fileName, Path filePath) {
        StringBuilder o_fileName = new StringBuilder();
        o_fileName.append(username).append("_").append(fileName);
        Resume resume = null;

        Users user = userRepository.findByUsername(username);
        if(user != null) {
            Optional<Resume> OptionalResume = resumeRepository.findByUserId(user.getId());
            if(OptionalResume.isPresent()) {
                resume = OptionalResume.get();
                resume.setFileName(fileName);
                resume.setUploadedFileName(o_fileName.toString());
                resume.setCreateDate(LocalDateTime.now());
            } else {
                resume = new Resume(user.getId(), fileName, o_fileName.toString(), LocalDateTime.now());
            }

            resumeRepository.save(resume);
            s3Service.uploadFile(o_fileName.toString(), filePath);
        }

    }

    public void upload(String fileName, Path filePath) {
        String username = getLoggedInUsername();
        upload(username, fileName, filePath);
    }

    public byte[] download(String username, String fileName) {
        return s3Service.downloadFile(username + "_" + fileName);
    }

    public byte[] download(String fileName) {
        String username = getLoggedInUsername();
        return download(username, fileName);
    }

    public String getResume() {
        String username = getLoggedInUsername();
        Users user = userRepository.findByUsername(username);
        Optional<Resume> OptionalResume = resumeRepository.findByUserId(user.getId());
        if(OptionalResume.isPresent()) {
            Resume resume = OptionalResume.get();

            return resume.getFileName();
        }
        return null;
    }
}
