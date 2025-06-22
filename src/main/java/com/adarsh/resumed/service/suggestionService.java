package com.adarsh.resumed.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Service
public class suggestionService {

    @Autowired
    private geminiService LLMService;

    private String createPrompt(String jobDescription, String resume) {
        String prompt = "Consider yourself as ATS.\nBelow is my resume:\n" + resume + "\nBelow is the Job Description:\n" + jobDescription + "\n suggest what changes I need to make per section.";

        return prompt;
    }


    public String getResult(String jobDescription, Path resumePath) throws IOException {
        String fileText;
        String fileName = resumePath.getFileName().toString();

        if (fileName.endsWith(".pdf")) {
            try (PDDocument document = PDDocument.load(Files.newInputStream(resumePath))) {
                PDFTextStripper stripper = new PDFTextStripper();
                fileText = stripper.getText(document);
            }
        } else {
            fileText = Files.readString(resumePath);
        }

        if (fileText.length() > 8000) {
            fileText = fileText.substring(0, 8000);
        }

        String prompt = createPrompt(jobDescription, fileText);

        return LLMService.getSuggestion(prompt);
    }
}
