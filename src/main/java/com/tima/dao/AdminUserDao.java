package com.tima.dao;

import com.tima.model.AdminUser;
import com.tima.model.Permission;
import com.tima.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminUserDao extends BaseDao<AdminUser> {
    SimpleJdbcCall createUserRole,
            deleteUserRole,
            findByUserId,
            findUserRole,
            findRolesOnUser,
            findPermissionsOnUser,
            findRolesOnCurrentUser,
            findPermissionsOnCurrentUser;

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
        findByUserId = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_admin_user_by_current_user_id")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(AdminUser.class));
        findAllPaginated = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_admin_users")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(AdminUser.class))
                .returningResultSet(RESULT_COUNT, new RowCountMapper());
        update = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_admin_user")
                .withReturnValue();
        createUserRole = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_user_role")
                .withReturnValue();
        deleteUserRole = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_delete_user_role")
                .withReturnValue();
        findUserRole = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_user_role")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Role.class));
        findRolesOnUser = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_roles_on_user")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Role.class));
        findPermissionsOnUser = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_permissions_on_user")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Permission.class));
        findRolesOnCurrentUser = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_roles_on_current_user")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Role.class));
        findPermissionsOnCurrentUser = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_permissions_on_current_user")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Permission.class));
    }

    public AdminUser findByUserId(int userId) {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("current_user_id", userId);
        Map<String, Object> m = this.findByUserId.execute(in);
        List<AdminUser> result = (List<AdminUser>) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? result.get(0) : null;
    }

    public void createUserRole(int userId, int roleId) {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("user_id", userId).addValue("role_id", roleId);
        this.createUserRole.execute(in);
    }

    public void deleteUserRole(int userId, int roleId) {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("user_id", userId).addValue("role_id", roleId);
        this.deleteUserRole.execute(in);
    }

    public Role findUserRole(int userId, int roleId) {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("user_id", userId).addValue("role_id", roleId);
        Map<String, Object> m = this.findUserRole.execute(in);
        List<Role> result = (List<Role>) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? result.get(0) : null;
    }

    public List<Role> findRolesOnUser(int userId) {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("user_id", userId);
        Map<String, Object> m = this.findRolesOnUser.execute(in);
        return (List<Role>) m.get(MULTIPLE_RESULT);
    }

    public List<Permission> findPermissionsOnUser(int userId) {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("user_id", userId);
        Map<String, Object> m = this.findPermissionsOnUser.execute(in);
        return (List<Permission>) m.get(MULTIPLE_RESULT);
    }

    public List<Role> findRolesOnCurrentUser(int currentUserId) {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("current_user_id", currentUserId);
        Map<String, Object> m = this.findRolesOnCurrentUser.execute(in);
        return (List<Role>) m.get(MULTIPLE_RESULT);
    }

    public List<Permission> findPermissionsOnCurrentUser(int currentUserId) {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("current_user_id", currentUserId);
        Map<String, Object> m = this.findPermissionsOnCurrentUser.execute(in);
        return (List<Permission>) m.get(MULTIPLE_RESULT);
    }
}
