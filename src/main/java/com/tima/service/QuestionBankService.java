package com.tima.service;

import com.tima.enums.QuestionBankStatus;
import com.tima.exception.BadRequestException;
import com.tima.model.QuestionBank;
import com.tima.repository.QuestionBankRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class QuestionBankService {
    QuestionBankRepository questionBankRepository;

    public QuestionBankService(QuestionBankRepository questionBankRepository) {
        this.questionBankRepository = questionBankRepository;
    }

    public void create(QuestionBank questionBank) {
        try {
            checkForOptions(questionBank);
            questionBankRepository.save(questionBank);
        } catch (Exception error) {
            log.error("Error creating question", error);
            throw error;
        }
    }

    private void checkForOptions(QuestionBank questionBank) {
        if (questionBank.getOptions().isEmpty()) throw new BadRequestException("Option list is required");
    }

    public List<QuestionBank> fetchQuestionList(String subject, String topic, QuestionBankStatus difficulty, int limit) {
        try {
            return questionBankRepository.findAllBySubjectAndTopicAndDifficulty(subject, topic, difficulty, limit);
        } catch (Exception error) {
            log.error("Error fetching questions for test", error);
            throw error;
        }
    }

//    public Page<QuestionBank> findAll(int page, int size) {
//        try {
//            return questionBankRepository.findAll(PageRequest.of(page, size));
//        } catch (Exception error) {
//            log.error("Error fetching all questions", error);
//            throw error;
//        }
//    }
}
