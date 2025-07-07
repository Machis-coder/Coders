package com.codingtrainers.duocoding.dto.output;

import com.codingtrainers.duocoding.entities.Question;
import com.codingtrainers.duocoding.entities.QuestionType;
import com.codingtrainers.duocoding.entities.TestExecutionResponse;

public class TestExecutionResponseDTO {

    private Long id;
    private Long questionId;
    private String answer;
    private Boolean isCorrect;
    private String notes;
    private String questionDescription;
    private String questionAnswer;
    private QuestionType questionType;

    public TestExecutionResponseDTO(TestExecutionResponse response, Question question) {
        this.id = response.getId();
        this.questionId = response.getQuestion().getId();
        this.answer = response.getAnswer();
        this.isCorrect = response.getCorrect();
        this.notes = response.getNotes();
        this.questionDescription = question.getDescription();
        this.questionAnswer = question.getAnswer();
        this.questionType = question.getType();
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public TestExecutionResponseDTO() {}

    public Long getId() {
        return id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
