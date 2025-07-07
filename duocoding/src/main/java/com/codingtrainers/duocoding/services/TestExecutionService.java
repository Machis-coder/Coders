package com.codingtrainers.duocoding.services;

import com.codingtrainers.duocoding.dto.input.NotesFromTeacherRequestDTO;
import com.codingtrainers.duocoding.dto.input.TestExecutionRequestDTO;
import com.codingtrainers.duocoding.dto.input.TestExecutionResponseRequestDTO;
import com.codingtrainers.duocoding.dto.output.QuestionDTO;
import com.codingtrainers.duocoding.dto.output.QuestionExecutionReview;
import com.codingtrainers.duocoding.dto.output.QuestionResponseDTO;
import com.codingtrainers.duocoding.dto.output.ResponseDTO;
import com.codingtrainers.duocoding.dto.output.ResponseExecutionReview;
import com.codingtrainers.duocoding.dto.output.TestExecutionDTO;
import com.codingtrainers.duocoding.dto.output.TestExecutionResponseDTO;
import com.codingtrainers.duocoding.dto.output.QuestionFullDTO;
import com.codingtrainers.duocoding.dto.output.TestExecutionFullDTO;
import com.codingtrainers.duocoding.dto.output.TestExecutionReview;
import com.codingtrainers.duocoding.entities.*;
import com.codingtrainers.duocoding.repositories.*;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TestExecutionService {

    @Autowired
    private TestExecutionRepository testExecutionRepository;

    @Autowired
    private TestExecutionResponseRepository testExecutionResponseRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private QuestionService questionService;

    public List<TestExecutionDTO> getTestExecutionsDTO() {
        return testExecutionRepository.findAllByActiveTrue().stream()
                .map(TestExecutionDTO::new)
                .collect(Collectors.toList());
    }

    public List<TestExecutionDTO> getDeletedTestExecutionsDTO() {
        return testExecutionRepository.findAllByActiveFalse().stream()
                .map(TestExecutionDTO::new)
                .collect(Collectors.toList());
    }

    public List<TestExecutionDTO> getTestExecutionsByUserId(Long userId) {
        if (userId == null) {
            throw new NullPointerException("userId is null");
        }

        List<TestExecution> testExecutions = testExecutionRepository.findActiveByUserId(userId);
        return testExecutions.stream().map(testExecution -> {
            TestExecutionDTO testExecutionDTO = new TestExecutionDTO();
            testExecutionDTO.setId(testExecution.getId());
            testExecutionDTO.setNotes(testExecution.getNotes());
            testExecutionDTO.setUserId(testExecution.getUser().getId());
            testExecutionDTO.setTestId(testExecution.getTest().getId());
            testExecutionDTO.setResult(testExecution.getResult());
            testExecutionDTO.setStartTime(testExecution.getStartTime());
            testExecutionDTO.setEndTime(testExecution.getFinishTime());
            testExecutionDTO.setDate(testExecution.getDate());
            testExecutionDTO.setNotes(testExecution.getNotes());
            testExecutionDTO.setTestName(testExecution.getTest().getName());
            return testExecutionDTO;
        }).toList();
    }

    public void deleteTestExecution(Long testExecutionId) {
        TestExecution execution = testExecutionRepository.findActiveById(testExecutionId)
                .orElseThrow(() -> new RuntimeException("TestExecution not found"));

        execution.setActive(false);
        testExecutionRepository.save(execution);
    }
    public void activateTestExecution(Long testExecutionId) {
        TestExecution execution = testExecutionRepository.findFalseById(testExecutionId)
                .orElseThrow(() -> new RuntimeException("TestExecution not found"));

        execution.setActive(true);
        testExecutionRepository.save(execution);
    }


    public Optional<TestExecutionDTO> getTestExecutionDTOById(Long id) {
        Optional<TestExecution> optExecution = testExecutionRepository.findById(id);
        if (optExecution.isEmpty()) return Optional.empty();

        TestExecution execution = optExecution.get();

        List<TestExecutionResponseDTO> responses = testExecutionResponseRepository.findActiveByTestExecutionId(id)
                .stream()
                .map(response -> new TestExecutionResponseDTO(response, response.getQuestion()))
                .collect(Collectors.toList());

        TestExecutionDTO dto = new TestExecutionDTO(execution);
        dto.setExecutionResponsesList(responses);

        return Optional.of(dto);
    }

    public String executeCode(String code) {
        return "";
    }

    private boolean evaluateResponse(Question question, TestExecutionResponseRequestDTO response) {
        switch (question.getType()) {
            case QuestionType.FREETEXT:
                return Arrays.stream(question.getAnswer().split(",")).map(q -> q.trim()).allMatch(res -> response.getAnswer().contains(res));
            case QuestionType.MONOSELECTION:
                return question.getAnswer().equals(response.getAnswer());
            case QuestionType.MULTISELECTION:
                List<String> answers = Arrays.stream(response.getAnswer().split(",")).map(q -> q.trim()).toList();
                List<String> expectedAnswers = Arrays.stream(question.getAnswer().split(",")).map(q -> q.trim()).toList();
                return expectedAnswers.size() == answers.size() && expectedAnswers.containsAll(answers);
            case QuestionType.GAPS:
                return Arrays.stream(question.getAnswer().split(",")).map(q -> q.trim()).toList().equals(Arrays.stream(response.getAnswer().split(",")).map(q -> q.trim()).toList());
            case QuestionType.CODE:
                return true; //question.getAnswer().equals(this.executeCode(response.getAnswer()));
            default: return false;
        }

    }

    public void saveTestExecution(TestExecutionRequestDTO dto) {
        User user = new User();
        user.setId(dto.getUserId());
        Test test = new Test();
        test.setId(dto.getTestId());
        TestExecution testExecution = new TestExecution();
        testExecution.setUser(user);
        testExecution.setTest(test);
        testExecution.setDate(LocalDate.now());
        testExecution.setNotes(null);
        testExecution.setFinishTime(dto.getTimeFinish());
        testExecution.setStartTime(dto.getTimeStart());

        testExecution.setActive(true);

        List<Question> questions = questionRepository.findAllByTestId(dto.getTestId());
        Map<Long, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));

        List<TestExecutionResponse> testExecutionResponses = new ArrayList<>();

        for (TestExecutionResponseRequestDTO responseDTO : dto.getResponses()) {
            Question question = questionMap.get(responseDTO.getQuestionId());

            if (question == null) {
                throw new RuntimeException("Question not found: " + responseDTO.getQuestionId());
            }
            TestExecutionResponse testExecutionResponse = new TestExecutionResponse();
            testExecutionResponse.setQuestion(question);
            testExecutionResponse.setAnswer(responseDTO.getAnswer());
            testExecutionResponse.setTestExecution(testExecution);
            testExecutionResponse.setActive(true);

            testExecutionResponse.setCorrect(evaluateResponse(question, responseDTO));
            testExecutionResponses.add(testExecutionResponse);
        }
        Float result = testExecutionResponses.stream().filter(r -> r.getCorrect()).count() * 100 / (float) testExecutionResponses.size() ;
        testExecution.setResult(result);
        testExecutionRepository.save(testExecution);
        testExecutionResponseRepository.saveAll(testExecutionResponses);
    }


    public void saveNotesFromTeacher(NotesFromTeacherRequestDTO notes) {
        TestExecution testExecution = testExecutionRepository.findActiveById(notes.getTestExecutionId())
                .orElseThrow(() -> new EntityNotFoundException("TestExecution not found with id: " + notes.getTestExecutionId()));

        if (notes.getTestExecutionResponseId() != null && notes.getTestExecutionResponseId() != 0) {
            TestExecutionResponse testExecutionResponse = testExecutionResponseRepository.findActiveById(notes.getTestExecutionResponseId())
                    .orElseThrow(() -> new EntityNotFoundException("TestExecutionResponse not found with id: " + notes.getTestExecutionResponseId()));
            testExecutionResponse.setNotes(notes.getTestExecutionResponseNotes());
            testExecutionResponseRepository.save(testExecutionResponse);
        }

        testExecution.setNotes(notes.getTestExecutionNotes());
        testExecutionRepository.save(testExecution);
    }

    public List<TestExecutionDTO> findActiveByUserIdAndSubjectId(Long userId, Long subjectId) {
        List<TestExecution> executions = testExecutionRepository
                .findActiveByUserIdAndSubjectId(userId, subjectId);

        return executions.stream()
                .map(TestExecutionDTO::new)
                .collect(Collectors.toList());
    }

    private boolean calculateCorrect(ResponseDTO response, String expected, String answer) {
        return Arrays.stream(expected.split(",")).toList().contains(response.getOrder().toString())
                && Arrays.stream(answer.split(",")).toList().contains(response.getOrder().toString());
    }

    private boolean calculateChecked(ResponseDTO response, String answer) {
        return Arrays.stream(answer.split(",")).toList().contains(response.getOrder().toString());
    }

    public TestExecutionReview getTestExecutionForReview(Long id) {
        TestExecution testExecution = testExecutionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Test test = testExecution.getTest();

        TestExecutionReview testExecutionReview = new TestExecutionReview();
        testExecutionReview.setId(id);
        testExecutionReview.setNotes(testExecution.getNotes());
        testExecutionReview.setUserId(testExecution.getUser().getId());
        testExecutionReview.setUserName(testExecution.getUser().getName());
        testExecutionReview.setTestId(test.getId());
        testExecutionReview.setResult(testExecution.getResult());
        testExecutionReview.setStartTime(testExecution.getStartTime());
        testExecutionReview.setEndTime(testExecution.getFinishTime());
        testExecutionReview.setDate(testExecution.getDate());
        testExecutionReview.setTestName(test.getName());
        testExecutionReview.setTestDescription(test.getDescription());
        testExecutionReview.setSubjectName(test.getSubject().getName());
        testExecutionReview.setTestId(test.getId());
        testExecutionReview.setQuestions(new ArrayList<>());


        List<QuestionDTO> questions = questionService.getQuestionsByTestId(test.getId());
        List<TestExecutionResponse> testExecutionResponses = testExecutionResponseRepository.findByTestExecution(testExecution);

        Map<Long, QuestionDTO> questionMap = questions.stream().collect(Collectors.toMap(QuestionDTO::getId, Function.identity()));


        for (TestExecutionResponse testExecutionResponse : testExecutionResponses) {
            QuestionDTO questionDTO = questionMap.get(testExecutionResponse.getQuestion().getId());
            QuestionExecutionReview questionExecutionReview = new QuestionExecutionReview();
            questionExecutionReview.setId(testExecutionResponse.getId());
            questionExecutionReview.setDescription(questionDTO.getDescription());
            questionExecutionReview.setOrder(questionDTO.getOrder());
            questionExecutionReview.setType(questionDTO.getType());
            questionExecutionReview.setAnswer(questionDTO.getAnswer());
            questionExecutionReview.setUserAnswer(testExecutionResponse.getAnswer());
            questionExecutionReview.setResponses(new ArrayList<>());
            if (questionDTO.getResponses() != null && !questionDTO.getResponses().isEmpty()) {
                List<ResponseExecutionReview> responses = questionDTO.getResponses().stream().map(r -> {
                    return new ResponseExecutionReview(r.getId(), r.getDescription(), r.getOrder(), r.getQuestionId(),
                            calculateCorrect(r, questionExecutionReview.getAnswer(), questionExecutionReview.getUserAnswer()),
                            calculateChecked(r, questionExecutionReview.getUserAnswer()));

                }).toList();
                questionExecutionReview.setResponses(responses);
            }
            questionExecutionReview.setCorrect(testExecutionResponse.getCorrect());
            testExecutionReview.getQuestions().add(questionExecutionReview);
        }

        return testExecutionReview;

    }

    //NO FUNCIONA
 /*   public TestExecutionFullDTO getTestExecution(Long testExecutionId) {
        TestExecution testExecution = testExecutionRepository.findActiveById(testExecutionId)
                .orElseThrow(() -> new EntityNotFoundException("Test Execution not found"));

        Test test = testExecution.getTest();

        TestExecutionFullDTO dto = new TestExecutionFullDTO();
        dto.setTestId(test.getId());
        dto.setTestTitle(test.getName());
        // TODO dto.setTestDescription(test.getDescription());

        List<TestQuestion> testQuestions = questionService.findTestQuestionsByTestId(test.getId());
        List<TestExecutionResponse> testExecutionResponses = testExecutionResponseRepository.findActiveAllByTestExecutionId(testExecutionId);
        List<Response> responses = questionService.findAllResponsesByQuestionIds(
                testQuestions.stream()
                        .map(it -> it.getQuestion().getId())
                        .toList()
        );

        Map<Long, List<Response>> responseMap = new HashMap<>();

        for (Response response : responses) {
            responseMap.computeIfAbsent(response.getQuestion().getId(), k -> new ArrayList<>()).add(response);
        }

        dto.setQuestions(testExecutionResponses.stream().map(tq -> {
            com.codingtrainers.duocoding.entities.Question question = tq.getQuestion();

            QuestionFullDTO questionDTO = new QuestionFullDTO();
            questionDTO.setQuestionId(question.getId());
            questionDTO.setDescription(question.getDescription());
            questionDTO.setAnswer(tq.getAnswer());
            questionDTO.setCorrect(tq.getCorrect());
            questionDTO.setResponses(responseMap.get(questionDTO.getQuestionId()));

            return questionDTO;
        }).toList());

        return dto;
    }


//todo Arreglar este método

    public TestExecutionDTO gesTestExecutionById(Long testExecutionId) {
        TestExecution execution = testExecutionRepository.findActiveById(testExecutionId)
                .orElseThrow(() -> new RuntimeException("TestExecution not found"));

        List<TestExecutionResponse> responseList =
                testExecutionResponseRepository.findActiveByTestExecutionId(testExecutionId);


        Map<Long, List<Response>> allResponsesByQuestion = new HashMap<>();
        List<TestExecutionResponseDTO> executionResponseDTOs = new ArrayList<>();

        for (TestExecutionResponse execResponse : responseList) {
            Question question = execResponse.getQuestion();


            allResponsesByQuestion.computeIfAbsent(question.getId(),
                    id -> responseRepository.findActiveByQuestionId(id));


            TestExecutionResponseDTO respDTO = new TestExecutionResponseDTO();
            respDTO.setId(execResponse.getId());
            respDTO.setQuestionId(question.getId());
            respDTO.setAnswer(execResponse.getAnswer());
            respDTO.setCorrect(execResponse.getCorrect());
            respDTO.setNotes(execResponse.getNotes());

            executionResponseDTOs.add(respDTO);
        }


        List<QuestionResponseDTO> questionResponseDTOList = responseList.stream().map(execResp -> {
            Question question = execResp.getQuestion();
            List<ResponseDTO> responseDTOs = allResponsesByQuestion.getOrDefault(question.getId(), new ArrayList<>())
                    .stream()
                    .map(resp -> new ResponseDTO(resp.getId(), resp.getDescription(), resp.getOrder()))
                    .collect(Collectors.toList());

            QuestionResponseDTO questionResponseDTO = new QuestionResponseDTO();
            questionResponseDTO.setDescription(question.getDescription());
            questionResponseDTO.setType(question.getType());
            questionResponseDTO.setAnswer(execResp.getAnswer());
            questionResponseDTO.setResponses(responseDTOs);

            return questionResponseDTO;
        }).toList();


        TestExecutionDTO dto = new TestExecutionDTO();
        dto.setId(execution.getId());
        dto.setTestId(execution.getTest().getId());
        dto.setUserId(execution.getUser().getId());
        dto.setDate(LocalDate.now());
        dto.setStartTime(execution.getStartTime());
        dto.setEndTime(execution.getFinishTime());
        dto.setResult(execution.getResult());
        dto.setNotes(execution.getNotes());
        dto.setExecutionResponsesList(executionResponseDTOs);

        return dto;
    }
*/
}
