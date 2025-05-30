package com.adarsh.resumed.service;

import com.adarsh.resumed.DTO.Resume;
import com.adarsh.resumed.DTO.ResumedS3UploadRequest;
import com.adarsh.resumed.DTO.Users;
import com.adarsh.resumed.exception.ResumeNotUploadedException;
import com.adarsh.resumed.repository.ResumeRepository;
import com.adarsh.resumed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Service
public class ResumeServiceV2 {

    @Value("${resumed.s3.api.url}")
    private String resumedS3Url;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    private static final RestTemplate restTemplate = new RestTemplate();

    public void upload(String username, MultipartFile file) throws IOException {
        String resumeFileContent = Base64.getEncoder().encodeToString(file.getBytes());
        String resumeFileName = file.getOriginalFilename();
        StringBuilder upload_resumeFileName = new StringBuilder();

        upload_resumeFileName.append(username).append("_").append(resumeFileName);
        Resume resume = null;

        Users user = userRepository.findByUsername(username);
        if(user != null) {
            Optional<Resume> OptionalResume = resumeRepository.findByUserId(user.getId());
            if (OptionalResume.isPresent()) {
                resume = OptionalResume.get();
                resume.setFileName(resumeFileName);
                resume.setUploadedFileName(upload_resumeFileName.toString());
                resume.setCreateDate(LocalDateTime.now());
            } else {
                resume = new Resume(user.getId(), resumeFileName, upload_resumeFileName.toString(), LocalDateTime.now());
            }

            resumeRepository.save(resume);
        }

        ResumedS3UploadRequest resumedS3UploadRequest = new ResumedS3UploadRequest(upload_resumeFileName.toString(), resumeFileContent);

        ResponseEntity<Map> response = restTemplate.postForEntity(resumedS3Url, resumedS3UploadRequest, Map.class);

        if (response.getStatusCode().is4xxClientError()) {
            throw new ResumeNotUploadedException("Resumed S3 service not available!");
        } else if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ResumeNotUploadedException("Resumed S3 service returned " + response.getStatusCode() + " status!");
        }
    }

    public void upload(MultipartFile file) throws IOException {
        String username = getLoggedInUsername();
        upload(username, file);
    }

    private String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }
}
