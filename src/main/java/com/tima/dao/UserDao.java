package com.tima.dao;

import com.tima.model.User;
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
public class UserDao extends BaseDao<User> {
    SimpleJdbcCall findByEmail;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_user_login")
                .withReturnValue();
        findById = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_user_login_by_id")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(User.class));
        findByEmail = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_user_login_by_email")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(User.class));
        findAllPaginated = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_retrieve_paginated_all_users")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(User.class))
                .returningResultSet(RESULT_COUNT, new RowCountMapper());
        update = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_user_login")
                .withReturnValue();
        delete = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_delete_user_login")
                .withReturnValue();
    }

    public User findByEmail(String email) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("email", email);
        Map<String, Object> m = this.findByEmail.execute(in);
        List<User> result = (List<User>) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? result.get(0) : null;
    }
}
