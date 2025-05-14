package com.adarsh.resumed.DTO;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "is_active")
    private Boolean isActive;

    public Users() {}

    public Users(String username, String password, LocalDateTime createDate, Boolean isActive) {
        this.username = username;
        this.password = password;
        this.createDate = createDate;
        this.isActive = isActive;
    }

    // Getters and Setters
    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
