package com.tima.dao;

import com.tima.model.QuestionOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class QuestionOptionDao extends BaseDao<QuestionOptions> {
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_question_options_map")
                .withReturnValue();
        findAll = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_question_options_map_by_question")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(QuestionOptions.class));
        delete = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_delete_question_options_map")
                .withReturnValue();
    }

    public List<QuestionOptions> findAll(Integer questionId) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("question_id", questionId);
        Map<String, Object> m = this.findAll.execute(in);
        return (List<QuestionOptions>) m.get(MULTIPLE_RESULT);
    }

    public void delete(Integer questionId) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("question_id", questionId);
        this.delete.execute(in);
    }
}
