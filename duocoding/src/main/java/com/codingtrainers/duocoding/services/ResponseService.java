package com.codingtrainers.duocoding.services;

import com.codingtrainers.duocoding.dto.output.ResponseDTO;
import com.codingtrainers.duocoding.entities.Question;
import com.codingtrainers.duocoding.entities.Response;
import com.codingtrainers.duocoding.repositories.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ResponseService {

    @Autowired
    private ResponseRepository responseRepository;

    public void saveAll(List<Response> list) {
        responseRepository.saveAll(list);
    }

    public void saveList(List<ResponseDTO> list) {
        List<Response> responseList = list.stream().map(r -> {
            Question question = new Question();
            question.setId(r.getQuestionId());
            Response response = new Response();
            response.setId(r.getId());
            response.setDescription(r.getDescription());
            response.setOrder(r.getOrder());
            response.setQuestion(question);
            response.setActive(true);
            return response;
        }).toList();
        responseRepository.saveAll(responseList);
    }

    public List<ResponseDTO> getResponsesByQuesionIdIn(List<Long> questionIds) {
        List<Response> responseList =responseRepository.findAllByQuestionIdIn(questionIds);
        return responseList.stream().map(r ->
             new ResponseDTO(r.getId(), r.getDescription(), r.getOrder(), r.getQuestion().getId())
        ).toList();
    }
}
