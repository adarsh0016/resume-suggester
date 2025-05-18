package com.adarsh.resumed.DTO;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "resume")
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true, name = "user_id")
    private Long userId;

    @Column(name = "file_name")
    private String fileName;

    @Column(unique = true, name = "uploaded_file_name")
    private String uploadedFileName;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    public String getUploadedFileName() {
        return uploadedFileName;
    }

    public void setUploadedFileName(String uploadedFileName) {
        this.uploadedFileName = uploadedFileName;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Resume(Long userId, String fileName, String uploadedFileName, LocalDateTime createDate) {
        this.userId = userId;
        this.fileName = fileName;
        this.uploadedFileName = uploadedFileName;
        this.createDate = createDate;
    }

    public Resume() {
    }
}
