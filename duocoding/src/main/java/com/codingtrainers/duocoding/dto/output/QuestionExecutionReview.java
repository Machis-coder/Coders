package com.codingtrainers.duocoding.dto.output;

import com.codingtrainers.duocoding.entities.QuestionType;

import java.util.List;

public class QuestionExecutionReview {
    private Long id;
    private Long order;
    private String answer;
    private String userAnswer;
    private QuestionType type;
    private String description;
    private boolean correct;
    private List<ResponseExecutionReview> responses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public List<ResponseExecutionReview> getResponses() {
        return responses;
    }

    public void setResponses(List<ResponseExecutionReview> responses) {
        this.responses = responses;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
