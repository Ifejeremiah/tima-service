//package com.tima.processor;
//
//import com.tima.dto.QuizResponse;
//import com.tima.exception.BadRequestException;
//import com.tima.model.Question;
//import com.tima.model.QuestionQuiz;
//import com.tima.model.Quiz;
//import com.tima.service.QuestionQuizService;
//import com.tima.service.QuestionService;
//import com.tima.service.QuizService;
//
//import java.util.List;
//
//public class QuizMarker implements Runnable {
//    List<QuizResponse> responses;
//    QuizService quizService;
//    QuestionService questionService;
//    QuestionQuizService questionQuizService;
//    int quizId;
//
//    public QuizMarker(List<QuizResponse> responses, QuizService quizService, QuestionService questionService, QuestionQuizService questionQuizService, int quizId) {
//        this.responses = responses;
//        this.quizService = quizService;
//        this.questionService = questionService;
//        this.questionQuizService = questionQuizService;
//        this.quizId = quizId;
//    }
//
//    @Override
//    public void run() {
//        int index = 0, score = 0;
//
//        for (QuizResponse response : responses) {
//            if (response.getQuestionId() == null) throw new BadRequestException("Question ID is not provided");
//            if (response.getResponse() == null) throw new BadRequestException("Response is not provided");
//
//            index++;
//
//            QuestionQuiz questionQuizMap = new QuestionQuiz();
//            questionQuizMap.setQuestionId(response.getQuestionId());
//            questionQuizMap.setQuizId(quizId);
//            questionQuizMap.setAnswer(response.getResponse());
//
//            questionQuizService.create(questionQuizMap);
//
//            Question question = questionService.findById(response.getQuestionId());
//            if (response.getResponse().equalsIgnoreCase(question.getAnswer()))
//                score++;
//        }
//
//        Quiz quiz = new Quiz();
//        quiz.setScore(score);
//        quiz.setNumberOfQuestions(index);
//
//        quizService.update(quizId, quiz);
//    }
//}
