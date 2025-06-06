package com.adarsh.resumed.service;

import com.adarsh.resumed.DTO.ResumedS3UploadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service("kafkaUploadService")
public class ResumeServiceKafka extends AbstractResumeUploader {

    @Autowired
    private KafkaTemplate<String, ResumedS3UploadRequest> kafkaTemplate;

    private static final String TOPIC = "resume-upload";

    @Override
    protected void doUpload(String username, String uploadFileName, String encodedFileContent) {
        ResumedS3UploadRequest event = new ResumedS3UploadRequest(
                uploadFileName, encodedFileContent);
        kafkaTemplate.send(TOPIC, event);
    }
}
