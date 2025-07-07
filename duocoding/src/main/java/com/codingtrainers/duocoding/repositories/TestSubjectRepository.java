package com.codingtrainers.duocoding.repositories;

import com.codingtrainers.duocoding.entities.Test;
import com.codingtrainers.duocoding.entities.TestSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestSubjectRepository extends JpaRepository<TestSubject, Long> {
    @Query("SELECT ts.test FROM TestSubject ts WHERE ts.subject.id = :subjectId AND ts.test.active = true")
    List<Test> findActiveTestsBySubjectId(@Param("subjectId") Long subjectId);

}
