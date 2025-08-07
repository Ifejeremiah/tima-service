package com.tima.dao;

import com.tima.dto.QuizResultSet;
import com.tima.model.Page;
import com.tima.model.Question;
import com.tima.model.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class QuizDao extends BaseDao<Quiz> {
    SimpleJdbcCall start,
            submit,
            findAllByStudentId;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        start = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_start_quiz")
                .withReturnValue()
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Question.class));
        findById = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_quiz_by_id")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Quiz.class));
        findAllPaginated = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_quizzes")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Quiz.class))
                .returningResultSet(RESULT_COUNT, new RowCountMapper());
        findAllByStudentId = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_quizzes_by_student_id")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Quiz.class))
                .returningResultSet(RESULT_COUNT, new RowCountMapper());
        submit = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_submit_quiz")
                .withReturnValue();
    }

    public QuizResultSet start(Quiz model) throws DataAccessException {
        SqlParameterSource in = new BeanPropertySqlParameterSource(model);
        Map<String, Object> m = this.start.execute(in);
        List<Question> content = (List<Question>) m.get(MULTIPLE_RESULT);
        QuizResultSet resultSet = new QuizResultSet();
        resultSet.setQuizId((int) m.get("quizId"));
        resultSet.setCount(content.size());
        resultSet.setQuestionList(content);
        return resultSet;
    }

    public void submit(int quizId, int score) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("id", quizId).addValue("score", score);
        this.submit.execute(in);
    }

    public Page<Quiz> findAllByStudentId(Integer pageNum, Integer pageSize, Integer studentId) throws DataAccessException {
        MapSqlParameterSource in = (new MapSqlParameterSource()).addValue(PAGE, pageNum <= 0 ? 1 : pageNum).addValue(PAGE_SIZE, pageSize <= 0 ? 10 : pageSize).addValue("studentId", studentId);

        Map<String, Object> m = this.findAllByStudentId.execute(in);
        List<Quiz> content = (List<Quiz>) m.get(MULTIPLE_RESULT);
        List<Long> counts = (List<Long>) m.get(RESULT_COUNT);
        Long count = counts.isEmpty() ? 0 : (Long) counts.get(0);

        return new Page<>(count, content);
    }
}
