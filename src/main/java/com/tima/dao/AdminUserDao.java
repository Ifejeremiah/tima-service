package com.tima.dao;

import com.tima.model.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class AdminUserDao extends BaseDao<AdminUser> {
    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_admin_user")
                .withReturnValue();
        findById = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_admin_user")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(AdminUser.class));
        findAllPaginated = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_admin_users")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(AdminUser.class))
                .returningResultSet(RESULT_COUNT, new RowCountMapper());
        update = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_admin_user")
                .withReturnValue();
    }
}
