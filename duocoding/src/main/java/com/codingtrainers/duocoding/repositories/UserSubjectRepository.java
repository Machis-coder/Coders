package com.codingtrainers.duocoding.repositories;

import com.codingtrainers.duocoding.entities.UserSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubjectRepository extends JpaRepository<UserSubject, Long> {

    Optional<UserSubject> findByUserIdAndSubjectIdAndActiveTrue(Long userId, Long subjectId);
   
    Optional<UserSubject> findByUserIdAndSubjectId(Long userId, Long subjectId);

    List<UserSubject> findByUserIdAndActiveTrue(Long userId);

    List<UserSubject> findBySubjectIdAndActiveTrue(Long subjectId);

    @Query("SELECT us FROM UserSubject us JOIN FETCH us.user u JOIN FETCH us.subject s WHERE us.subject.id = :subjectId AND us.active = true")
    List<UserSubject> findBySubjectIdAndActiveTrueWithUserAndSubject(@Param("subjectId") Long subjectId);


}
