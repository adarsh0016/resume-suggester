package com.adarsh.resumed.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OllamaService implements LLMService{

    private String apiUrl = "http://localhost:11434/api/generate";

    private static final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getSuggestion(String promptText) {
        System.out.println("Calling URL:" + apiUrl);

        Map<String, Object> request = Map.of("model", "gemma:2b",
                "prompt", promptText,
                "stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // Parse the response
            Map body = response.getBody();
            if(body != null) {
                return (String) body.get("response");
            } else {
                return "No response from Ollama.";
            }

        }
        return "Failed response from Ollama.";
    }
}
