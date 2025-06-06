package com.adarsh.resumed.service;

import com.adarsh.resumed.DTO.Resume;
import com.adarsh.resumed.DTO.Users;
import com.adarsh.resumed.repository.ResumeRepository;
import com.adarsh.resumed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

public abstract class AbstractResumeUploader {

    @Autowired
    protected ResumeRepository resumeRepository;

    @Autowired
    protected UserRepository userRepository;

    public final void upload(String username, MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String uploadFileName = username + "_" + originalFilename;
        String encodedResume = Base64.getEncoder().encodeToString(file.getBytes());

        saveToDB(username, originalFilename, uploadFileName);
        doUpload(username, uploadFileName, encodedResume);
    }

    public final void upload(MultipartFile file) throws IOException {
        String username = getLoggedInUsername();
        upload(username, file);
    }

    protected void saveToDB(String username, String resumeFileName, String uploadFileName) {
        Resume resume;
        Users user = userRepository.findByUsername(username);
        if (user != null) {
            Optional<Resume> optionalResume = resumeRepository.findByUserId(user.getId());
            if (optionalResume.isPresent()) {
                resume = optionalResume.get();
                resume.setFileName(resumeFileName);
                resume.setUploadedFileName(uploadFileName);
                resume.setCreateDate(LocalDateTime.now());
            } else {
                resume = new Resume(user.getId(), resumeFileName, uploadFileName, LocalDateTime.now());
            }
            resumeRepository.save(resume);
        }
    }

    protected String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    protected abstract void doUpload(String username, String uploadFileName, String encodedFileContent);
}

