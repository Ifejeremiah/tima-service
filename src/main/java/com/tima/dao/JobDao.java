package com.tima.dao;

import com.tima.enums.JobStatus;
import com.tima.model.Job;
import com.tima.model.Question;
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
public class JobDao extends BaseDao<Job> {
    SimpleJdbcCall updateJobStatus,
            updateJobStatusMessage,
            findAllByNewStatus;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_job")
                .withReturnValue();
        findById = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_find_job_by_id")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Job.class));
        findAllByNewStatus = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_find_all_new_jobs")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Question.class));
        updateJobStatus = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_job_status")
                .withReturnValue();
        updateJobStatusMessage = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_job_status_message")
                .withReturnValue();
    }

    public List<Job> findAllByNewStatus() throws DataAccessException {
        Map<String, Object> m = this.findAllByNewStatus.execute();
        return (List<Job>) m.get(MULTIPLE_RESULT);
    }

    public void updateJobStatus(Integer id, JobStatus status) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("id", id).addValue("status", status);
        this.updateJobStatus.execute(in);
    }

    public void updateJobStatusMessage(Integer id, String statusMessage) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("id", id).addValue("status_message", statusMessage);
        this.updateJobStatusMessage.execute(in);
    }
}
