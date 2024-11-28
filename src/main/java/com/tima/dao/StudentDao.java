package com.tima.dao;

import com.tima.model.Student;
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
public class StudentDao extends BaseDao<Student> {
    SimpleJdbcCall findByUserId;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_student")
                .withReturnValue();
        findById = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_student_by_id")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Student.class));
        findByUserId = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_student_by_user_id")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Student.class));
        findAllPaginated = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_students")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Student.class))
                .returningResultSet(RESULT_COUNT, new RowCountMapper());
        update = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_student")
                .withReturnValue();
    }

    public Student findByUserId(int userId) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("user_id", userId);
        Map<String, Object> m = this.findByUserId.execute(in);
        List<Student> result = (List<Student>) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? result.get(0) : null;
    }
}
