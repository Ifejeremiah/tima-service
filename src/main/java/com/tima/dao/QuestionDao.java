package com.tima.dao;

import com.tima.dto.QuestionSummary;
import com.tima.model.Page;
import com.tima.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class QuestionDao extends BaseDao<Question> {
    SimpleJdbcCall findAllForQuiz,
            findAllSubjects,
            findAllTopicsBySubject,
            findQuestionSummary;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_question")
                .withReturnValue();
        findById = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_question_by_id")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Question.class));
        findAllPaginated = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_questions")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Question.class))
                .returningResultSet(RESULT_COUNT, new RowCountMapper());
        findAllForQuiz = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_questions_for_quiz")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Question.class));
        findAllSubjects = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_question_subjects")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Question.class));
        findAllTopicsBySubject = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_topics_by_question_subject")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Question.class));
        update = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_question")
                .withReturnValue();
        delete = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_delete_question")
                .withReturnValue();
        findQuestionSummary = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_question_summary")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(QuestionSummary.class));
    }

    public Page<Question> findAll(Integer pageNum, Integer pageSize, String searchQuery, String subject, String mode, String difficultyLevel, String examType, String startDate, String endDate) throws DataAccessException {

        SqlParameterSource in = (new MapSqlParameterSource())
                .addValue(PAGE, pageNum <= 0 ? 1 : pageNum)
                .addValue(PAGE_SIZE, pageSize <= 0 ? 10 : pageSize)
                .addValue(SEARCH_QUERY, searchQuery == null ? "" : searchQuery)
                .addValue("subject", subject == null ? "" : subject)
                .addValue("mode", mode == null ? "" : mode)
                .addValue("difficulty_level", difficultyLevel == null ? "" : difficultyLevel)
                .addValue("exam_type", examType == null ? "" : examType)
                .addValue("start_date", ObjectUtils.isEmpty(startDate) ? null : startDate)
                .addValue("end_date", ObjectUtils.isEmpty(endDate) ? null : endDate);

        Map<String, Object> m = this.findAllPaginated.execute(in);
        List<Question> content = (List<Question>) m.get(MULTIPLE_RESULT);
        List<Long> counts = (List<Long>) m.get(RESULT_COUNT);
        Long count = counts.isEmpty() ? 0 : (Long) counts.get(0);
        return new Page<>(count, content);
    }

    public List<Question> findAllForQuiz(Integer pageSize, String subject, String topic, String difficultyLevel) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("page_size", pageSize <= 0 ? 10 : pageSize).addValue("subject", subject).addValue("topic", topic).addValue("difficulty_level", difficultyLevel);
        Map<String, Object> m = this.findAllForQuiz.execute(in);
        return (List<Question>) m.get(MULTIPLE_RESULT);
    }

    public List<Question> findAllSubjects() throws DataAccessException {
        Map<String, Object> m = this.findAllSubjects.execute();
        return (List<Question>) m.get(MULTIPLE_RESULT);
    }

    public List<Question> findAllTopicsBySubject(String subject) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("subject", subject);
        Map<String, Object> m = this.findAllTopicsBySubject.execute(in);
        return (List<Question>) m.get(MULTIPLE_RESULT);
    }

    public QuestionSummary findQuestionSummary() throws DataAccessException {
        Map<String, Object> m = this.findQuestionSummary.execute();
        List<QuestionSummary> result = (List<QuestionSummary>) m.get(SINGLE_RESULT);
        return result.get(0);
    }
}
