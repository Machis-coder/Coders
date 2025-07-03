package com.codingtrainers.duocoding.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.codingtrainers.duocoding.entities.Question;
import com.codingtrainers.duocoding.entities.QuestionType;
import com.codingtrainers.duocoding.entities.Response;
import com.codingtrainers.duocoding.entities.TestQuestion;
import com.codingtrainers.duocoding.repositories.QuestionRepository;
import com.codingtrainers.duocoding.repositories.ResponseRepository;
import com.codingtrainers.duocoding.repositories.TestQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    private Question sampleQuestion;

    @Mock
    private TestQuestionRepository testQuestionRepository;

    @Mock
    private ResponseRepository responseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllQuestions_ShouldReturnOnlyActiveQuestions() {
        List<Question> activeQuestions = Arrays.asList(
                new Question(1L, null, "Desc 1", "Answer 1", true),
                new Question(2L, null, "Desc 2", "Answer 2", true)
        );
        when(questionRepository.findAllByActiveTrue()).thenReturn(activeQuestions);

        List<Question> result = questionService.getAllQuestions();

        assertEquals(2, result.size());
        verify(questionRepository).findAllByActiveTrue();
    }

    @Test
    void getById_WhenExists_ShouldReturnQuestion() {
        Question q = new Question(1L, null, "Desc", "Answer", true);
        when(questionRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(q));

        Question result = questionService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(questionRepository).findByIdAndActiveTrue(1L);
    }

    @Test
    void getById_WhenNotExists_ShouldThrow() {
        when(questionRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> questionService.getById(1L));
        assertEquals("Question not found", ex.getMessage());
    }

    @Test
    void createQuestion_NullQuestion_ShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> questionService.createQuestion(null));
        assertEquals("Question cannot be null", ex.getMessage());
    }

    @Test
    void createQuestion_ValidQuestion_ShouldSave() {
        Question q = new Question(null, null, "Desc", "Answer", true);
        when(questionRepository.save(q)).thenReturn(q);

        Question result = questionService.createQuestion(q);

        assertEquals(q, result);
        verify(questionRepository).save(q);
    }

    @Test
    void updateQuestion_NullQuestion_ShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> questionService.updateQuestion(null));
        assertEquals("Question cannot be null", ex.getMessage());
    }

    @Test
    void updateQuestion_ValidQuestion_ShouldSave() {
        Question q = new Question(1L, null, "Desc", "Answer", true);
        when(questionRepository.save(q)).thenReturn(q);

        Question result = questionService.updateQuestion(q);

        assertEquals(q, result);
        verify(questionRepository).save(q);
    }

    @Test
    void deleteQuestionById_NullId_ShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> questionService.deleteQuestionById(null));
        assertEquals("ID cannot be null", ex.getMessage());
    }

    @Test
    void deleteQuestionById_NotFound_ShouldThrow() {
        when(questionRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> questionService.deleteQuestionById(1L));
        assertEquals("Question not found", ex.getMessage());
    }

    @Test
    void deleteQuestionById_Found_ShouldSetInactiveAndSave() {
        Question q = new Question(1L, null, "Desc", "Answer", true);
        when(questionRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(q));
        when(questionRepository.save(any())).thenReturn(q);

        String result = questionService.deleteQuestionById(1L);

        assertEquals("Question marked as inactive successfully", result);
        assertFalse(q.isActive());
        verify(questionRepository).save(q);
    }


    @Test
    void findTestQuestionsByTestId_ShouldReturnList() {
        List<TestQuestion> list = Collections.singletonList(new TestQuestion());
        when(testQuestionRepository.findByTestId(10L)).thenReturn(list);

        List<TestQuestion> result = questionService.findTestQuestionsByTestId(10L);

        assertEquals(list, result);
    }

    @Test
    void findAllByIds_ShouldReturnQuestions() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<Question> questions = Arrays.asList(
                new Question(1L, null, "Desc1", "Answer1", true),
                new Question(2L, null, "Desc2", "Answer2", true)
        );
        when(questionRepository.findAllActiveByIdIn(ids)).thenReturn(questions);

        List<Question> result = questionService.findAllByIds(ids);

        assertEquals(2, result.size());
    }

    @Test
    void findAllResponsesByQuestionIds_ShouldReturnResponses() {
        List<Long> questionIds = Arrays.asList(1L, 2L);
        List<Response> responses = Collections.singletonList(new Response());
        when(responseRepository.findAllActiveByQuestionIdIn(questionIds)).thenReturn(responses);

        List<Response> result = questionService.findAllResponsesByQuestionIds(questionIds);

        assertEquals(responses, result);
    }
}
