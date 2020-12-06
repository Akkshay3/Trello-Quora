package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerRepository;
import com.upgrad.quora.service.dao.QuestionRepository;
import com.upgrad.quora.service.dao.UserAuthRepository;
import com.upgrad.quora.service.dao.UserRepository;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

public class AnswerService {

    @Autowired
    private UserAuthRepository authRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;


    /**
     * Add answer into the database
     *
     * @param questionId   : questionid that you want to answer
     * @param answerEntity : the answer body
     * @param accessToken  : access-token for authentication
     * @return returns created response for the answer
     * @throws AuthorizationFailedException : if authentication is failed
     * @throws InvalidQuestionException     : if question id is invalid
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(AnswerEntity answerEntity,
                                     final String accessToken,
                                     final String questionId) throws AuthorizationFailedException,
            InvalidQuestionException {
        UserAuthEntity userAuthEntity = authRepository.findByAccessToken(accessToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException(
                    "ATHR-002", "User is signed out.Sign in first to post an answer");
        }
        QuestionEntity questionEntity = questionRepository.findQuestionByUuid(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setQuestionEntity(questionEntity);
        answerEntity.setUserEntity(userAuthEntity.getUserEntity());
        return answerRepository.save(answerEntity);
    }
}
