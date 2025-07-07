package com.codingtrainers.duocoding.controllers;

import com.codingtrainers.duocoding.dto.input.TestRequestDTO;
import com.codingtrainers.duocoding.dto.output.ExamStructureResponseDTO;
import com.codingtrainers.duocoding.dto.output.TestDTO;
import com.codingtrainers.duocoding.dto.output.TestResponseDTO;
import com.codingtrainers.duocoding.entities.Test;
import com.codingtrainers.duocoding.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping
    public List<TestDTO> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/{id}")
    public TestDTO getTestById(@PathVariable Long id) {
        return testService.getTestById(id);
    }

    @PostMapping("/")
    public ResponseEntity createTest(@RequestBody TestDTO test) {
        try {
            testService.createTest(test);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.ok().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestResponseDTO> updateTest(@PathVariable Long id, @RequestBody TestRequestDTO testDetails) {
        Optional<TestResponseDTO> updatedTest = testService.updateTest(id, testDetails);
        return updatedTest.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/delete")
    public ResponseEntity<String> deleteTest(@PathVariable Long id) {
        try {
            testService.deleteTestById(id);
            return ResponseEntity.ok("Test marked as inactive successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping("/{id}/activate")
    public ResponseEntity<String> activateTest(@PathVariable Long id) {
        try {
            testService.activateTestById(id);
            return ResponseEntity.ok("Test marked as inactive successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/{testId}/structure")
    public ResponseEntity<ExamStructureResponseDTO> getExamStructure(@PathVariable Long testId) {
        ExamStructureResponseDTO dto = testService.getExamStructure(testId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/available/by-subject/{subjectId}")
    public ResponseEntity<List<TestResponseDTO>> getAvailableTestsBySubject(@PathVariable Long subjectId) {
        List<TestResponseDTO> tests = testService.getAvailableTestsBySubject(subjectId);
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/{id}/perform")
    public TestDTO getTestByIdToPerform(@PathVariable Long id) {
        return testService.getTestByIdToPerform(id);
    }
}

