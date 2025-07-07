package com.codingtrainers.duocoding.repositories;

import com.codingtrainers.duocoding.entities.QuestionCat;
import com.codingtrainers.duocoding.entities.ResponseCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseCatRepository extends JpaRepository<ResponseCat, Long> {
    List<ResponseCat> findByQuestionId(Long questionId);

    @Query("delete from ResponseCat r where r.question.id = :questionId")
    void deleteAllByQuestionId(@Param("questionId") Long questionId);


    void deleteAllByQuestion(QuestionCat question);

    @Query("select r from ResponseCat r where r.question.id in (:questionIds)")
    List<ResponseCat> findAllByQuestionIdIn(@Param("questionIds") List<Long> questionIds);
}
