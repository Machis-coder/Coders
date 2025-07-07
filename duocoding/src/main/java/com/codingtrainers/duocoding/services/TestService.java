package com.codingtrainers.duocoding.services;


import com.codingtrainers.duocoding.dto.input.TestRequestDTO;
import com.codingtrainers.duocoding.dto.output.ExamStructureResponseDTO;
import com.codingtrainers.duocoding.dto.output.QuestionDTO;
import com.codingtrainers.duocoding.dto.output.QuestionResponseDTO;
import com.codingtrainers.duocoding.dto.output.TestDTO;
import com.codingtrainers.duocoding.dto.output.TestResponseDTO;
import com.codingtrainers.duocoding.entities.*;

import com.codingtrainers.duocoding.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TestQuestionRepository testQuestionRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    TestSubjectRepository testSubjectRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public List<TestDTO> getAllTests() {

        return testRepository.findAll().stream().map(t ->
            new TestDTO(t.getId(), t.getName(), t.getDescription(), t.getSubject().getName(), t.getSubject().getId(), null, t.getActive(), new ArrayList<>())
        ).toList();
    }

    public List<TestResponseDTO> getAllTestDTOs() {
        List<Test> tests = testRepository.findAllActive();
        List<TestResponseDTO> dtos = new ArrayList<>();

        for (Test test : tests) {
            List<QuestionResponseDTO> questions = getQuestionsForTest(test.getId());
            dtos.add(new TestResponseDTO(
                    test.getId(),
                    test.getName(),
                    test.getDescription(),
                    test.getActive(),
                    test.getSubject() != null ? test.getSubject().getId() : null,
                    questions
            ));
        }
        return dtos;
    }

    public Optional<TestResponseDTO> getTestDTOById(Long id) {
        return testRepository.findActiveById(id).map(test -> {
            List<QuestionResponseDTO> questions = getQuestionsForTest(test.getId());
            return new TestResponseDTO(
                    test.getId(),
                    test.getName(),
                    test.getDescription(),
                    test.getActive(),
                    test.getSubject() != null ? test.getSubject().getId() : null,
                    questions
            );
        });
    }

    private List<QuestionResponseDTO> getQuestionsForTest(Long testId) {
        List<Question> testQuestions = questionRepository.findAllByTestId(testId);
        List<QuestionResponseDTO> questions = new ArrayList<>();

        for (Question q : testQuestions) {

            QuestionResponseDTO dto = new QuestionResponseDTO();
            dto.setId(q.getId());
            dto.setAnswer(q.getAnswer());
            dto.setType(q.getType());
            dto.setDescription(q.getDescription());
            dto.setResponses(null); // Puedes adaptarlo si necesitas respuestas

            questions.add(dto);
        }

        return questions;
    }

    public TestDTO getTestById(Long id) {

        Test test = testRepository.findById(id).orElseThrow(() -> new RuntimeException("Test not found"));
        List<QuestionDTO> questionDTOList = questionService.getQuestionsByTestId(id);
        TestDTO testDTO = new TestDTO(test.getId(), test.getName(), test.getDescription(), test.getSubject().getName(), test.getSubject().getId(), null, test.getActive(), questionDTOList);
        return testDTO;
    }

    public TestDTO getTestByIdToPerform(Long id) {
        TestDTO testDTO = getTestById(id);
        testDTO.getQuestions().stream().forEach(q -> q.setAnswer(""));
        return testDTO;
    }

    public void createTest(TestDTO test) {
        Test newTest = new Test();
        newTest.setName(test.getName());
        newTest.setDescription(test.getDescription());
        newTest.setActive(true);
        newTest.setDescription(test.getDescription());
        Subject subject = new Subject();
        subject.setId(test.getSubjectId());
        newTest.setSubject(subject);
        newTest = testRepository.save(newTest);

        questionService.saveAll(newTest.getId(), test.getQuestions());
    }

    public Optional<TestResponseDTO> updateTest(Long id, TestRequestDTO dto) {
        return testRepository.findActiveById(id).map(test -> {
            test.setName(dto.getName());
            test.setDescription(dto.getDescription());
            test.setActive(dto.getActive());

            Subject subject = subjectRepository.findByIdAndActiveTrue(dto.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Subject not found with id: " + dto.getSubjectId()));

            test.setSubject(subject);

            Test updated = testRepository.save(test);

            List<QuestionResponseDTO> questions = getQuestionsForTest(updated.getId());

            return new TestResponseDTO(
                    updated.getId(),
                    updated.getName(),
                    updated.getDescription(),
                    updated.getActive(),
                    updated.getSubject() != null ? updated.getSubject().getId() : null,
                    questions
            );
        });
    }

    public void deleteTestById(Long id) {
        Test test = testRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("Test not found with id: " + id));
        test.setActive(false); // borrado lógico
        testRepository.save(test);
    }

    public void activateTestById(Long id) {
        Test test = testRepository.findFalseById(id)
                .orElseThrow(() -> new RuntimeException("Test not found with id: " + id));
        test.setActive(true);
        testRepository.save(test);
    }

    public ExamStructureResponseDTO getExamStructure(Long testId) {
        Optional<Test> optionalTest = testRepository.findById(testId);
        if (optionalTest.isEmpty()) {
            return null;
        }

        Test test = optionalTest.get();
        ExamStructureResponseDTO dto = new ExamStructureResponseDTO();
        dto.setTestId(test.getId());
        dto.setTestTitle(test.getName());

        List<TestQuestion> testQuestions = testQuestionRepository.findByTestId(testId);

        dto.setQuestions(testQuestions.stream().map(tq -> {
            Question question = tq.getQuestion();
            ExamStructureResponseDTO.QuestionDTO questionDTO = new ExamStructureResponseDTO.QuestionDTO();
            questionDTO.questionId = question.getId();
            questionDTO.content = question.getDescription();

            Set<Long> correctAnswerIds = new HashSet<>();
            try {
                if (question.getAnswer() != null && !question.getAnswer().isEmpty()) {
                    String[] parts = question.getAnswer().split(",");
                    for (String part : parts) {
                        correctAnswerIds.add(Long.parseLong(part.trim()));
                    }
                }
            } catch (NumberFormatException ignored) {}

            List<Response> responses = responseRepository.findActiveByQuestionId(question.getId());
            questionDTO.responses = responses.stream().map(r -> {
                ExamStructureResponseDTO.ResponseDTO responseDTO = new ExamStructureResponseDTO.ResponseDTO();
                responseDTO.responseId = r.getId();
                responseDTO.content = r.getDescription();
                responseDTO.setCorrect(correctAnswerIds.contains(r.getId()));
                return responseDTO;
            }).toList();

            return questionDTO;
        }).toList());

        return dto;
    }

    public List<TestResponseDTO> getAvailableTestsBySubject(Long subjectId) {
        List<Test> tests = testRepository.findBySubjectIdAndActiveTrue(subjectId);

        return tests.stream().map(test -> {
            List<QuestionResponseDTO> questions = getQuestionsForTest(test.getId());
            return new TestResponseDTO(
                    test.getId(),
                    test.getName(),
                    test.getDescription(),
                    test.getActive(),
                    test.getSubject() != null ? test.getSubject().getId() : null,
                    questions
            );
        }).collect(Collectors.toList());
    }

}

    

