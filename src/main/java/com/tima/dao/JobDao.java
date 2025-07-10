package com.tima.dao;

import com.tima.enums.JobStatus;
import com.tima.model.Job;
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
            findJobsForProcessing;

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
        findJobsForProcessing = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_find_jobs_for_processing")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Job.class));
        updateJobStatus = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_job_status")
                .withReturnValue();
    }

    public List<Job> findJobsForProcessing(Integer fetchCount, Integer amnestyTime) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("fetch_count", fetchCount).addValue("amnesty_time", amnestyTime);
        Map<String, Object> m = this.findJobsForProcessing.execute(in);
        return (List<Job>) m.get(MULTIPLE_RESULT);
    }

    public void updateJobStatus(Integer id, JobStatus status, String statusMessage) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("id", id).addValue("status", status).addValue("status_message", statusMessage);
        this.updateJobStatus.execute(in);
    }
}
