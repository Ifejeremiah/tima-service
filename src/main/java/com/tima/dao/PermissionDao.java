package com.tima.dao;

import com.tima.model.Permission;
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
public class PermissionDao extends BaseDao<Permission> {
    SimpleJdbcCall findByCode;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_permission")
                .withReturnValue();
        findById = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_permission")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Permission.class));
        findByCode = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_permission_by_code")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Permission.class));
        findAll = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_permissions")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Permission.class))
                .returningResultSet(RESULT_COUNT, new RowCountMapper());
        update = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_permission")
                .withReturnValue();
    }

    public List<Permission> findAll(String searchQuery) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue(SEARCH_QUERY, searchQuery == null ? "" : searchQuery);
        Map<String, Object> m = this.findAll.execute(in);
        return (List<Permission>) m.get(MULTIPLE_RESULT);
    }

    public Permission findByCode(String code) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("code", code);
        Map<String, Object> m = this.findByCode.execute(in);
        List<Permission> result = (List<Permission>) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? result.get(0) : null;
    }
}
