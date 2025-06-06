package com.adarsh.resumed.service;

import com.adarsh.resumed.DTO.ResumedS3UploadRequest;
import com.adarsh.resumed.exception.ResumeNotUploadedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service("directUploadService")
public class ResumeServiceV2 extends AbstractResumeUploader {

    @Value("${resumed.s3.api.url}")
    private String resumedS3Url;

    private static final RestTemplate restTemplate = new RestTemplate();

    @Override
    protected void doUpload(String username, String uploadFileName, String encodedFileContent) {
        ResumedS3UploadRequest request = new ResumedS3UploadRequest(uploadFileName, encodedFileContent);

        ResponseEntity<Map> response = restTemplate.postForEntity(resumedS3Url, request, Map.class);

        if (response.getStatusCode().is4xxClientError()) {
            throw new ResumeNotUploadedException("Resumed S3 service not available!");
        } else if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ResumeNotUploadedException("Resumed S3 service returned " + response.getStatusCode() + " status!");
        }
    }
}
