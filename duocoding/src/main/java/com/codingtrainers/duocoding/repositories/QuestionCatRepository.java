package com.codingtrainers.duocoding.repositories;

import com.codingtrainers.duocoding.entities.QuestionCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCatRepository extends JpaRepository<QuestionCat, Long> {

}

