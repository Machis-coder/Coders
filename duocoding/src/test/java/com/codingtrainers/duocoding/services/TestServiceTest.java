package com.codingtrainers.duocoding.services;



import com.codingtrainers.duocoding.dto.input.TestRequestDTO;
import com.codingtrainers.duocoding.dto.output.ExamStructureResponseDTO;
import com.codingtrainers.duocoding.dto.output.QuestionResponseDTO;
import com.codingtrainers.duocoding.dto.output.TestResponseDTO;
import com.codingtrainers.duocoding.entities.*;
import com.codingtrainers.duocoding.repositories.ResponseRepository;
import com.codingtrainers.duocoding.repositories.SubjectRepository;
import com.codingtrainers.duocoding.repositories.TestQuestionRepository;
import com.codingtrainers.duocoding.repositories.TestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private TestQuestionRepository testQuestionRepository;

    @Mock
    private ResponseRepository responseRepository;

    @InjectMocks
    private TestService testService;

    @Test
    void getAllTestDTOs_Success() {
        com.codingtrainers.duocoding.entities.Test test1 = mock(com.codingtrainers.duocoding.entities.Test.class);
        when(test1.getId()).thenReturn(1L);
        when(test1.getName()).thenReturn("Test 1");
        when(test1.getDescription()).thenReturn("Description 1");
        when(test1.getActive()).thenReturn(true);

        com.codingtrainers.duocoding.entities.Test test2 = mock(com.codingtrainers.duocoding.entities.Test.class);
        when(test2.getId()).thenReturn(2L);
        when(test2.getName()).thenReturn("Test 2");
        when(test2.getDescription()).thenReturn("Description 2");
        when(test2.getActive()).thenReturn(true);

        when(testRepository.findAll()).thenReturn(Arrays.asList(test1, test2));
        when(testQuestionRepository.findByTestId(anyLong())).thenReturn(Collections.emptyList());

        List<TestResponseDTO> result = testService.getAllTestDTOs();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test 1", result.get(0).getName());
        assertEquals("Test 2", result.get(1).getName());
        verify(testRepository, times(1)).findAll();
        verify(testQuestionRepository, times(2)).findByTestId(anyLong());
    }

    @Test
    void getAllTestDTOs_EmptyList() {
        when(testRepository.findAll()).thenReturn(Collections.emptyList());

        List<TestResponseDTO> result = testService.getAllTestDTOs();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(testRepository, times(1)).findAll();
        verify(testQuestionRepository, never()).findByTestId(anyLong());
    }

    @Test
    void getTestDTOById_Success() {
        com.codingtrainers.duocoding.entities.Test test = mock(com.codingtrainers.duocoding.entities.Test.class);
        when(test.getId()).thenReturn(1L);
        when(test.getName()).thenReturn("Test 1");
        when(test.getDescription()).thenReturn("Description 1");
        when(test.getActive()).thenReturn(true);
        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(testQuestionRepository.findByTestId(1L)).thenReturn(Collections.emptyList());

        Optional<TestResponseDTO> result = testService.getTestDTOById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test 1", result.get().getName());
        assertEquals("Description 1", result.get().getDescription());
        verify(testRepository, times(1)).findById(1L);
        verify(testQuestionRepository, times(1)).findByTestId(1L);
    }

    @Test
    void getTestDTOById_NotFound() {
        when(testRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<TestResponseDTO> result = testService.getTestDTOById(1L);

        assertFalse(result.isPresent());
        verify(testRepository, times(1)).findById(1L);
        verify(testQuestionRepository, never()).findByTestId(anyLong());
    }

    @Test
    void createTest_Success() {
        TestRequestDTO requestDTO = new TestRequestDTO();
        requestDTO.setName("New Test");
        requestDTO.setDescription("New Description");
        requestDTO.setActive(true);
        requestDTO.setSubjectId(1L);

        Subject subject = new Subject();
        subject.setId(1L);
        com.codingtrainers.duocoding.entities.Test savedTest = new com.codingtrainers.duocoding.entities.Test(1L, "New Test", "New Description", subject, true);

        when(subjectRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(subject));
        when(testRepository.save(any(com.codingtrainers.duocoding.entities.Test.class))).thenReturn(savedTest);

        TestResponseDTO result = testService.createTest(requestDTO);

        assertNotNull(result);
        assertEquals("New Test", result.getName());
        assertEquals("New Description", result.getDescription());
        assertEquals(1L, result.getSubjectId());
        assertTrue(result.getActive());
        verify(subjectRepository, times(1)).findByIdAndActiveTrue(1L);
        verify(testRepository, times(1)).save(any(com.codingtrainers.duocoding.entities.Test.class));
    }

    @Test
    void createTest_SubjectNotFound_ThrowsException() {
        TestRequestDTO requestDTO = new TestRequestDTO();
        requestDTO.setName("New Test");
        requestDTO.setDescription("New Description");
        requestDTO.setActive(true);
        requestDTO.setSubjectId(1L);

        when(subjectRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            testService.createTest(requestDTO);
        });
        assertEquals("Subject not found with id: 1", thrown.getMessage());
        verify(subjectRepository, times(1)).findByIdAndActiveTrue(1L);
        verify(testRepository, never()).save(any(com.codingtrainers.duocoding.entities.Test.class));
    }

    @Test
    void updateTest_Success() {
        TestRequestDTO requestDTO = new TestRequestDTO();
        requestDTO.setName("Updated Test");
        requestDTO.setDescription("Updated Description");
        requestDTO.setActive(true);
        requestDTO.setSubjectId(1L);

        Subject subject = new Subject();
        subject.setId(1L);
        com.codingtrainers.duocoding.entities.Test existingTest = new com.codingtrainers.duocoding.entities.Test(1L, "Old Test", "Old Description", null, true);
        com.codingtrainers.duocoding.entities.Test updatedTest = new com.codingtrainers.duocoding.entities.Test(1L, "Updated Test", "Updated Description", subject, true);

        when(testRepository.findActiveById(1L)).thenReturn(Optional.of(existingTest));
        when(subjectRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(subject));
        when(testRepository.save(any(com.codingtrainers.duocoding.entities.Test.class))).thenReturn(updatedTest);
        when(testQuestionRepository.findByTestId(1L)).thenReturn(Collections.emptyList());

        Optional<TestResponseDTO> result = testService.updateTest(1L, requestDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Test", result.get().getName());
        assertEquals("Updated Description", result.get().getDescription());
        assertEquals(1L, result.get().getSubjectId());
        assertTrue(result.get().getActive());
        verify(testRepository, times(1)).findActiveById(1L);
        verify(subjectRepository, times(1)).findByIdAndActiveTrue(1L);
        verify(testRepository, times(1)).save(any(com.codingtrainers.duocoding.entities.Test.class));
        verify(testQuestionRepository, times(1)).findByTestId(1L);
    }

    @Test
    void updateTest_NotFound() {
        TestRequestDTO requestDTO = new TestRequestDTO();
        requestDTO.setSubjectId(1L);

        when(testRepository.findActiveById(1L)).thenReturn(Optional.empty());

        Optional<TestResponseDTO> result = testService.updateTest(1L, requestDTO);

        assertFalse(result.isPresent());
        verify(testRepository, times(1)).findActiveById(1L);
        verify(subjectRepository, never()).findByIdAndActiveTrue(anyLong());
        verify(testRepository, never()).save(any(com.codingtrainers.duocoding.entities.Test.class));
    }

    @Test
    void deleteTestById_Success() {
        com.codingtrainers.duocoding.entities.Test test = new com.codingtrainers.duocoding.entities.Test(1L, "Test 1", "Description 1", null, true);
        when(testRepository.findActiveById(1L)).thenReturn(Optional.of(test));
        when(testRepository.save(any(com.codingtrainers.duocoding.entities.Test.class))).thenReturn(test);

        testService.deleteTestById(1L);

        assertFalse(test.getActive());
        verify(testRepository, times(1)).findActiveById(1L);
        verify(testRepository, times(1)).save(test);
    }

    @Test
    void deleteTestById_NotFound_ThrowsException() {
        when(testRepository.findActiveById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            testService.deleteTestById(1L);
        });
        assertEquals("Test not found with id: 1", thrown.getMessage());
        verify(testRepository, times(1)).findActiveById(1L);
        verify(testRepository, never()).save(any(com.codingtrainers.duocoding.entities.Test.class));
    }

    @Test
    void activateTestById_Success() {
        com.codingtrainers.duocoding.entities.Test test = new com.codingtrainers.duocoding.entities.Test(1L, "Test 1", "Description 1", null, false);
        when(testRepository.findFalseById(1L)).thenReturn(Optional.of(test));
        when(testRepository.save(any(com.codingtrainers.duocoding.entities.Test.class))).thenReturn(test);

        testService.activateTestById(1L);

        assertTrue(test.getActive());
        verify(testRepository, times(1)).findFalseById(1L);
        verify(testRepository, times(1)).save(test);
    }

    @Test
    void activateTestById_NotFound_ThrowsException() {
        when(testRepository.findFalseById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            testService.activateTestById(1L);
        });
        assertEquals("Test not found with id: 1", thrown.getMessage());
        verify(testRepository, times(1)).findFalseById(1L);
        verify(testRepository, never()).save(any(com.codingtrainers.duocoding.entities.Test.class));
    }

    @Test
    void getExamStructure_Success() {
        com.codingtrainers.duocoding.entities.Test test = new com.codingtrainers.duocoding.entities.Test(1L, "Test 1", "Description 1", null, true);
        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        QuestionType type = QuestionType.MULTISELECTION;
        Question question = new Question();
        question.setId(1L);
        question.setDescription("Question 1");
        question.setAnswer("1");
        question.setType(type);

        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setTest(test);
        testQuestion.setQuestion(question);

        Response response = new Response();
        response.setId(1L);
        response.setDescription("Response 1");

        when(testQuestionRepository.findByTestId(1L)).thenReturn(Collections.singletonList(testQuestion));
        when(responseRepository.findByQuestionId(1L)).thenReturn(Collections.singletonList(response));

        ExamStructureResponseDTO result = testService.getExamStructure(1L);

        assertNotNull(result);
        assertEquals(1L, result.getTestId());
        assertEquals("Test 1", result.getTestTitle());
        assertEquals(1, result.getQuestions().size());
        assertEquals(1L, result.getQuestions().get(0).questionId);
        assertEquals("Question 1", result.getQuestions().get(0).content);
        assertEquals(1, result.getQuestions().get(0).responses.size());
        assertEquals(1L, result.getQuestions().get(0).responses.get(0).responseId);
        assertEquals("Response 1", result.getQuestions().get(0).responses.get(0).content);
        assertTrue(result.getQuestions().get(0).responses.get(0).isCorrect());
        verify(testRepository, times(1)).findById(1L);
        verify(testQuestionRepository, times(1)).findByTestId(1L);
        verify(responseRepository, times(1)).findByQuestionId(1L);
    }

    @Test
    void getExamStructure_TestNotFound_ReturnsNull() {
        when(testRepository.findById(1L)).thenReturn(Optional.empty());

        ExamStructureResponseDTO result = testService.getExamStructure(1L);

        assertNull(result);
        verify(testRepository, times(1)).findById(1L);
        verify(testQuestionRepository, never()).findByTestId(anyLong());
        verify(responseRepository, never()).findByQuestionId(anyLong());
    }

    @Test
    void getExamStructure_NoQuestions() {
        com.codingtrainers.duocoding.entities.Test test = mock(com.codingtrainers.duocoding.entities.Test.class);
        when(test.getId()).thenReturn(1L);
        when(test.getName()).thenReturn("Test 1");
        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(testQuestionRepository.findByTestId(1L)).thenReturn(Collections.emptyList());

        ExamStructureResponseDTO result = testService.getExamStructure(1L);

        assertNotNull(result);
        assertEquals(1L, result.getTestId());
        assertEquals("Test 1", result.getTestTitle());
        assertTrue(result.getQuestions().isEmpty());
        verify(testRepository, times(1)).findById(1L);
        verify(testQuestionRepository, times(1)).findByTestId(1L);
        verify(responseRepository, never()).findByQuestionId(anyLong());
    }

    @Test
    void getExamStructure_InvalidAnswerFormat() {
        com.codingtrainers.duocoding.entities.Test test = new com.codingtrainers.duocoding.entities.Test(1L, "Test 1", "Description 1", null, true);
        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        QuestionType type = QuestionType.MULTISELECTION;
        Question question = new Question();
        question.setId(1L);
        question.setDescription("Question 1");
        question.setAnswer("invalid");
        question.setType(type);

        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setTest(test);
        testQuestion.setQuestion(question);

        Response response = new Response();
        response.setId(1L);
        response.setDescription("Response 1");

        when(testQuestionRepository.findByTestId(1L)).thenReturn(Collections.singletonList(testQuestion));
        when(responseRepository.findByQuestionId(1L)).thenReturn(Collections.singletonList(response));

        ExamStructureResponseDTO result = testService.getExamStructure(1L);

        assertNotNull(result);
        assertEquals(1, result.getQuestions().size());
        assertFalse(result.getQuestions().get(0).responses.get(0).isCorrect());
        verify(testRepository, times(1)).findById(1L);
        verify(testQuestionRepository, times(1)).findByTestId(1L);
        verify(responseRepository, times(1)).findByQuestionId(1L);
    }
}