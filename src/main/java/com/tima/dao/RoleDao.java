package com.tima.dao;

import com.tima.model.Permission;
import com.tima.model.Role;
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
public class RoleDao extends BaseDao<Role> {
    SimpleJdbcCall findByName,
            createRolePermission,
            deleteRolePermission,
            findRolePermission,
            findPermissionsOnRole;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_role")
                .withReturnValue();
        findById = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_role")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Role.class));
        findByName = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_role_by_name")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Role.class));
        findAllPaginated = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_roles")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Role.class))
                .returningResultSet(RESULT_COUNT, new RowCountMapper());
        update = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_role")
                .withReturnValue();
        createRolePermission = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_role_permission")
                .withReturnValue();
        deleteRolePermission = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_delete_role_permission")
                .withReturnValue();
        findRolePermission = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_role_permission")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Permission.class));
        findPermissionsOnRole = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_permissions_on_role")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Permission.class));
    }

    public Role findByName(String name) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("name", name);
        Map<String, Object> m = this.findByName.execute(in);
        List<Role> result = (List<Role>) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? result.get(0) : null;
    }

    public void createRolePermission(int roleId, int permissionId) {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("role_id", roleId).addValue("permission_id", permissionId);
        this.createRolePermission.execute(in);
    }

    public void deleteRolePermission(int roleId, int permissionId) {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("role_id", roleId).addValue("permission_id", permissionId);
        this.deleteRolePermission.execute(in);
    }

    public Permission findRolePermission(int roleId, int permissionId) {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("role_id", roleId).addValue("permission_id", permissionId);
        Map<String, Object> m = this.findRolePermission.execute(in);
        List<Permission> result = (List<Permission>) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? result.get(0) : null;
    }

    public List<Permission> findPermissionsOnRole(int roleId) {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("role_id", roleId);
        Map<String, Object> m = this.findPermissionsOnRole.execute(in);
        return (List<Permission>) m.get(MULTIPLE_RESULT);
    }
}
