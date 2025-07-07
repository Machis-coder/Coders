package com.codingtrainers.duocoding.dto.output;

public class ResponseDTO {

    private Long id;
    private String description;
    private Integer order;
    private Long questionId;

    public ResponseDTO(Long id, String description, Integer order, Long questionId) {
        this.id = id;
        this.description = description;
        this.order = order;
        this.questionId = questionId;
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
}
