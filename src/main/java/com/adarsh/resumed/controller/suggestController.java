package com.adarsh.resumed.controller;

import com.adarsh.resumed.DTO.Response;
import com.adarsh.resumed.service.suggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class suggestController {

    private static final Logger log = LoggerFactory.getLogger(suggestController.class);

    @Autowired
    private suggestionService suggestionService;

    @PostMapping("/suggest")
    public ResponseEntity<Response> suggest(@RequestPart("file") MultipartFile file,
                                              @RequestPart("data") String reqBody) throws IOException {
        String jobDescription = reqBody;

        System.out.println("JD: \n" + jobDescription);
        System.out.println("resume: \n" + file.getOriginalFilename());

        if (!file.isEmpty()) {
            String result = suggestionService.getResult(jobDescription, file);
            Response response = new Response(result, "success");
            return ResponseEntity.ok(response);
        }

        return (ResponseEntity<Response>) ResponseEntity.badRequest();
        
    }
}
