package com.adarsh.resumed.deprecated.controller;

import com.adarsh.resumed.DTO.Response;
import com.adarsh.resumed.deprecated.service.ResumeServiceV2;
import com.adarsh.resumed.service.suggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/apiV2")
public class suggestControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(suggestControllerV2.class);

    @Autowired
    private suggestionService suggestionService;

    @Autowired
    private ResumeServiceV2 resumeServiceV2;

    @PostMapping("/suggest")
    public ResponseEntity<Response> suggest(@RequestPart("file") MultipartFile file,
                                            @RequestPart("data") String jobDescription) throws IOException {
        log.info("V2 Received Request");

        if (!file.isEmpty()) {
            resumeServiceV2.upload(file);

            Path tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile);
            String result = suggestionService.getResult(jobDescription, tempFile);
            Response response = new Response(result, "success");

            log.info("V2 Response Sent");

            return ResponseEntity.ok(response);
        }

        log.error("V2 File is Empty");

        return ResponseEntity.badRequest().build();
    }
}
