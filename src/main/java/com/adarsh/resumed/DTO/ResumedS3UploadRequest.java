package com.adarsh.resumed.DTO;

public class ResumedS3UploadRequest {
    private String resumeFileName;
    private String resumeFileContent;

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    public String getResumeFileContent() {
        return resumeFileContent;
    }

    public void setResumeFileContent(String resumeFileContent) {
        this.resumeFileContent = resumeFileContent;
    }

    public ResumedS3UploadRequest(String resumeFileName, String resumeFileContent) {
        this.resumeFileName = resumeFileName;
        this.resumeFileContent = resumeFileContent;
    }
}
