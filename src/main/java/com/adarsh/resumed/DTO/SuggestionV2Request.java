package com.adarsh.resumed.DTO;

public class SuggestionV2Request {
    private String jobDescription;
    private String resumeFileName;

    // Getters and setters
    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }
}
