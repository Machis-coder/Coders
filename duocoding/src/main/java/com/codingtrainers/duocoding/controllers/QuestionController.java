package com.codingtrainers.duocoding.controllers;

import com.codingtrainers.duocoding.entities.Question;
import com.codingtrainers.duocoding.services.QuestionService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin("*")
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        if (questions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<Question>> getAllInactiveQuestions() {
        List<Question> questions = questionService.getAllInactiveQuestions();
        if (questions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable("id") Long id) {
        try {
            Optional<Question> optionalQuestion = Optional.ofNullable(questionService.getById(id));
            return optionalQuestion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        try {
            Question created = questionService.createQuestion(question);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/by-ids")
    public ResponseEntity<List<Question>> getQuestionsByIds(@RequestBody List<Long> ids) {
        try {
            List<Question> questions = questionService.findAllByIds(ids);

            if (questions.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(questions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/")
    public ResponseEntity<Question> updateQuestion(@RequestBody Question question) {
        try {
            Question updated = questionService.updateQuestion(question);
            return ResponseEntity.ok(updated);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/{id}/delete")
    public ResponseEntity<String> deleteQuestionById(@PathVariable("id") Long id) {
        try {
            String message = questionService.deleteQuestionById(id);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ID provided");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
