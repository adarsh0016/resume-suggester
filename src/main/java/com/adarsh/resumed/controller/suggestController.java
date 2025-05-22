package com.adarsh.resumed.controller;

import com.adarsh.resumed.DTO.Response;
import com.adarsh.resumed.DTO.Resume;
import com.adarsh.resumed.DTO.SuggestionV2Request;
import com.adarsh.resumed.service.ResumeService;
import com.adarsh.resumed.service.S3Service;
import com.adarsh.resumed.service.suggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class suggestController {

    private static final Logger log = LoggerFactory.getLogger(suggestController.class);

    @Autowired
    private suggestionService suggestionService;

    @Autowired
    private ResumeService resumeService;

    @GetMapping("/resume")
    public ResponseEntity<Response> resume() {
        String resumeName = resumeService.getResume();
        if(resumeName != null) {
            Response response = new Response(resumeName, "success");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/suggestV2")
    public ResponseEntity<Response> suggestV2(@RequestBody SuggestionV2Request request) throws IOException {
        log.info("Received Request V2");

        byte[] resume = resumeService.download(request.getResumeFileName());

        Path tempFile = Files.createTempFile("s3-", request.getResumeFileName());
        Files.write(tempFile, resume);

        String result = suggestionService.getResult(request.getJobDescription(), tempFile);
        Response response = new Response(result, "success");

        log.info("Response Sent V2");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/suggest")
    public ResponseEntity<Response> suggest(@RequestPart("file") MultipartFile file,
                                              @RequestPart("data") String jobDescription) throws IOException {

        log.info("Received Request");

        if (!file.isEmpty()) {
            Path tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile);
            resumeService.upload(file.getOriginalFilename(), tempFile);

            String result = suggestionService.getResult(jobDescription, tempFile);
            Response response = new Response(result, "success");

            log.info("Response Sent");

            return ResponseEntity.ok(response);
        }

        log.info("Response Sent");

        return ResponseEntity.badRequest().build();
        
    }
}
