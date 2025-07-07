package com.codingtrainers.duocoding.services;

import com.codingtrainers.duocoding.dto.output.QuestionDTO;
import com.codingtrainers.duocoding.dto.output.ResponseDTO;
import com.codingtrainers.duocoding.entities.QuestionCat;
import com.codingtrainers.duocoding.entities.ResponseCat;
import com.codingtrainers.duocoding.repositories.QuestionCatRepository;
import com.codingtrainers.duocoding.repositories.ResponseCatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionCatService {

    @Autowired
    private QuestionCatRepository questionCatRepository;

    @Autowired
    private ResponseCatRepository responseCatRepository;

    @Autowired
    private ResponseService responseService;

    public List<QuestionCat> getAllQuestions() {
        return questionCatRepository.findAll();
    }

    public QuestionCat findById(Long id) {
        return questionCatRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found by id"));
    }

    public QuestionDTO getQuestionById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        QuestionCat question = questionCatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question Not Found"));
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setAnswer(question.getAnswer());
        questionDTO.setDescription(question.getDescription());
        questionDTO.setType(question.getType());
        questionDTO.setResponses(new ArrayList<>());


        List<ResponseCat> responses = responseCatRepository.findByQuestionId(id);
        if (responses != null && responses.size() > 0) {
            for (ResponseCat response : responses) {
                ResponseDTO responseDTO = new ResponseDTO(response.getId(),response.getDescription(), response.getOrder(), response.getQuestion().getId());
                questionDTO.getResponses().add(responseDTO);
            }
        }
        return questionDTO;
    }

    public QuestionCat createQuestion(QuestionDTO question) {
        if (question == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }

        QuestionCat newQuestion = new QuestionCat();
        newQuestion.setId(question.getId());
        newQuestion.setActive(true);
        newQuestion.setAnswer(question.getAnswer());
        newQuestion.setDescription(question.getDescription());
        newQuestion.setType(question.getType());

        newQuestion = questionCatRepository.save(newQuestion);
        if (question.getResponses() != null && !question.getResponses().isEmpty()) {
            List<ResponseCat> responses = new ArrayList<>();
            for (ResponseDTO r: question.getResponses())  {
                ResponseCat response = new ResponseCat();
                response.setId(r.getId());
                response.setDescription(r.getDescription());
                response.setOrder(r.getOrder());
                response.setQuestion(newQuestion);
                responses.add(response);
            }
            responseCatRepository.saveAll(responses);
        }
        return newQuestion;
    }



    public QuestionCat updateQuestion(QuestionDTO question) {
        if (question == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }

        QuestionCat updatedQuestion = this.findById(question.getId());

        updatedQuestion.setActive(true);
        updatedQuestion.setAnswer(question.getAnswer());
        updatedQuestion.setDescription(question.getDescription());
        updatedQuestion.setType(question.getType());

        responseCatRepository.deleteAllByQuestion(updatedQuestion);

        updatedQuestion = questionCatRepository.save(updatedQuestion);
        if (question.getResponses() != null && !question.getResponses().isEmpty()) {
            List<ResponseCat> responses = new ArrayList<>();
            for (ResponseDTO r: question.getResponses())  {
                ResponseCat response = new ResponseCat();
                response.setId(r.getId());
                response.setDescription(r.getDescription());
                response.setOrder(r.getOrder());
                response.setQuestion(updatedQuestion);
                responses.add(response);
            }
            responseCatRepository.saveAll(responses);
        }
        return updatedQuestion;
    }

    public void deleteQuestionById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        QuestionCat question = findById(id);
        question.setActive(false);
        questionCatRepository.save(question);
    }
}
