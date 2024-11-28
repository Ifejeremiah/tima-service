package com.tima.dao;

import com.tima.model.Otp;
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
public class OtpDao extends BaseDao<Otp> {
    SimpleJdbcCall findByEmailAndOTP;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_otp")
                .withReturnValue();
        findByEmailAndOTP = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_otp_by_email_and_otp")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Otp.class));
        delete = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_delete_otp")
                .withReturnValue();
    }

    public Otp findByEmailAndOTP(String email, String otp) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("email", email).addValue("otp", otp);
        Map<String, Object> m = this.findByEmailAndOTP.execute(in);
        List<Otp> result = (List<Otp>) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? result.get(0) : null;
    }
}
