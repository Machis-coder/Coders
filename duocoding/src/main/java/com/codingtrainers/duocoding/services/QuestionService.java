package com.codingtrainers.duocoding.services;

import com.codingtrainers.duocoding.dto.output.QuestionDTO;
import com.codingtrainers.duocoding.dto.output.ResponseDTO;
import com.codingtrainers.duocoding.entities.Question;
import com.codingtrainers.duocoding.entities.Response;
import com.codingtrainers.duocoding.entities.Test;
import com.codingtrainers.duocoding.repositories.QuestionCatRepository;
import com.codingtrainers.duocoding.repositories.QuestionRepository;
import com.codingtrainers.duocoding.repositories.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private ResponseService responseService;
    @Autowired
    private QuestionCatRepository questionCatRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found by id"));
    }

    public List<QuestionDTO> getQuestionsByTestId(Long id) {

        List<Question> questions = questionRepository.findAllByTestId(id);


        List<Long> questionIdList = new ArrayList<>();
        Map<Long, QuestionDTO> questionDTOMap = new HashMap<>();
        List<QuestionDTO> questionDTOList = questions.stream().map(tq -> {
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setId(tq.getId());
            questionDTO.setType(tq.getType());
            questionDTO.setDescription(tq.getDescription());
            questionDTO.setAnswer(tq.getAnswer());
            questionDTO.setOrder(tq.getOrder());
            questionDTO.setResponses(new ArrayList<>());
            questionDTOMap.put(questionDTO.getId(), questionDTO);
            questionIdList.add(questionDTO.getId());
            return questionDTO;
        }).toList();

        List<ResponseDTO> responseDTOList = responseService.getResponsesByQuesionIdIn(questionIdList);

        responseDTOList.forEach(responseDTO -> {
            QuestionDTO questionDTO = questionDTOMap.get(responseDTO.getQuestionId());
            questionDTO.getResponses().add(responseDTO);
        });
        return questionDTOList;
    }

    public QuestionDTO getQuestionById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question Not Found"));
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setAnswer(question.getAnswer());
        questionDTO.setDescription(question.getDescription());
        questionDTO.setType(question.getType());
        questionDTO.setResponses(new ArrayList<>());


        List<Response> responses = responseRepository.findByQuestionId(id);
        if (responses != null && responses.size() > 0) {
            for (Response response : responses) {
                ResponseDTO responseDTO = new ResponseDTO(response.getId(),response.getDescription(), response.getOrder(), response.getQuestion().getId());
                questionDTO.getResponses().add(responseDTO);
            }
        }
        return questionDTO;
    }

    public void saveAll(Long id, List<QuestionDTO> questions) {

        Map<Long, List<ResponseDTO>> map = new HashMap<>();
        List<Question> questionList = new ArrayList<>();

        Test test = new Test();
        test.setId(id);

        for (QuestionDTO questionDto : questions) {
            map.put(questionDto.getOrder(), questionDto.getResponses());
            Question question = new Question();
            question.setType(questionDto.getType());
            question.setOrder(questionDto.getOrder());
            question.setAnswer(questionDto.getAnswer());
            question.setDescription(questionDto.getDescription());
            question.setActive(true);
            question.setTest(test);
            questionList.add(question);
            map.put(questionDto.getOrder(), questionDto.getResponses());
        }

        questionRepository.saveAll(questionList);

        Map<Long, Long> orderIdMap = questionList.stream().collect(Collectors.toMap( k -> k.getOrder(), v -> v.getId()));

        List<ResponseDTO> responsesList = new ArrayList<>();

        map.keySet().stream().forEach(order -> {
            map.get(order).stream().forEach(res -> {
                res.setQuestionId(orderIdMap.get(order));
                responsesList.add(res);
            });
        });

        responseService.saveList(responsesList);
    }

    public List<Question>getAllInactiveQuestions() {

        return questionRepository.findAllByActiveFalse();
    }

    public Question getById(Long id) {
        return questionRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    public Question createQuestion(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }
        question.setActive(true);
        return questionRepository.save(question);
    }

    public List<Question> findAllByIds(List<Long> ids) {

        return questionRepository.findAllActiveByIdIn(ids);
    }

    public Question updateQuestion(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }
        return questionRepository.save(question);
    }

    public String deleteQuestionById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Question question = questionRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        question.setActive(false);
        questionRepository.save(question);

        return "Question marked as inactive successfully";
    }
}
