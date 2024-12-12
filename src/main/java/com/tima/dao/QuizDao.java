package com.tima.dao;

import com.tima.model.Page;
import com.tima.model.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class QuizDao extends BaseDao<Quiz> {
    SimpleJdbcCall findAllByStudentId;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_quiz")
                .withReturnValue();
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
        update = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_quiz")
                .withReturnValue();
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
