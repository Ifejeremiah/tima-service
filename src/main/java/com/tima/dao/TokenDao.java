package com.tima.dao;

import com.tima.model.Token;
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
public class TokenDao extends BaseDao<Token> {
    SimpleJdbcCall findByEmailAndToken;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_token")
                .withReturnValue();
        findByEmailAndToken = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_token_by_email_and_refresh_token")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Token.class));
    }

    public Token findByEmailAndToken(String email, String refreshToken) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("email", email).addValue("refresh_token", refreshToken);
        Map<String, Object> m = this.findByEmailAndToken.execute(in);
        List<Token> result = (List<Token>) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? result.get(0) : null;
    }
}
