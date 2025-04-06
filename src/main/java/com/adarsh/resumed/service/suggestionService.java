package com.adarsh.resumed.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class suggestionService {

    private geminiService geminiService = new geminiService();

    private String createPrompt(String jobDescription, String resume) {
        String prompt = "Consider yourself as ATS.\nBelow is my resume:\n" + resume + "\nBelow is the Job Description:\n" + jobDescription + "\n suggest what changes I need to make per section.";

        return prompt;
    }

    public String getResult(String jobDescription, MultipartFile resume) throws IOException {
        String fileText;

        if (Objects.requireNonNull(resume.getOriginalFilename()).endsWith(".pdf")) {
            PDDocument document = PDDocument.load(resume.getInputStream());
            PDFTextStripper stripper = new PDFTextStripper();
            fileText = stripper.getText(document);
            document.close();
        } else {
            fileText = new String(resume.getBytes());
        }

        if (fileText.length() > 8000) {
            fileText = fileText.substring(0, 8000);
        }

        String prompt = createPrompt(jobDescription, fileText);

        return geminiService.getSuggestion(prompt);
    }
}
