package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository  extends JpaRepository<QuestionEntity, Integer> {
    QuestionEntity findQuestionByUuid(String uuid);
    List<QuestionEntity> findAllQuestionByUserEntity(UserEntity user);
}
