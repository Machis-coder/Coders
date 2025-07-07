package com.codingtrainers.duocoding.dto.output;

public class ResponseExecutionReview {

    private Long id;
    private String description;
    private Integer order;
    private Long questionId;
    private boolean correct;
    private boolean checked;

    public ResponseExecutionReview(Long id, String description, Integer order, Long questionId, boolean correct, boolean checked) {
        this.id = id;
        this.description = description;
        this.order = order;
        this.questionId = questionId;
        this.correct = correct;
        this.checked = checked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public boolean isCorrect() {
        return correct;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
