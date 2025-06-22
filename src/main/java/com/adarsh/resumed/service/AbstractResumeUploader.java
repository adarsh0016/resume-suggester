package com.adarsh.resumed.service;

import com.adarsh.resumed.DTO.Resume;
import com.adarsh.resumed.DTO.Users;
import com.adarsh.resumed.exception.ResumeNotUploadedException;
import com.adarsh.resumed.repository.ResumeRepository;
import com.adarsh.resumed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractResumeUploader {

    @Autowired
    protected ResumeRepository resumeRepository;

    @Autowired
    protected UserRepository userRepository;

    @Value("${resumed.s3.api.url}")
    private String resumedS3Url;

    private static final RestTemplate restTemplate = new RestTemplate();

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

    public byte[] download(String username, String fileName) {
        resumedS3Url = resumedS3Url + "?file_name=" + username + "_" + fileName;
        ResponseEntity<Map> response = restTemplate.getForEntity(resumedS3Url, Map.class);

        if(response.getStatusCode().is2xxSuccessful()) {
            Map<?, ?> body = response.getBody();
            String encodedResume = (String) body.get("content");
            return Base64.getDecoder().decode(encodedResume);
        }
        else if (response.getStatusCode().is4xxClientError()) {
            throw new ResumeNotUploadedException("Resumed S3 service not available!");
        } else if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ResumeNotUploadedException("Resumed S3 service returned " + response.getStatusCode() + " status!");
        }

        return new byte[]{};
    }

    public byte[] download(String fileName) {
        String username = getLoggedInUsername();
        return download(username, fileName);
    }

    protected String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    protected abstract void doUpload(String username, String uploadFileName, String encodedFileContent);
}

