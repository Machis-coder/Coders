package com.codingtrainers.duocoding.dto.output;


import java.time.LocalDateTime;
import java.util.List;

public class TestDTO {
    private Long id;
    private Long subjectId;
    private String subject;
    private String name;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;
    private List<QuestionDTO> questions;

    public TestDTO(Long id, String name, String description, String subject, Long subjectId, LocalDateTime createdAt, boolean active, List<QuestionDTO> questions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.subject = subject;
        this.subjectId = subjectId;
        this.createdAt = createdAt;
        this.active = active;
        this.questions = questions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
