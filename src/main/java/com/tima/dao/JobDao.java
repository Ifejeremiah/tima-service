package com.tima.dao;

import com.tima.enums.JobStatus;
import com.tima.model.Job;
import com.tima.model.Page;
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
            findByRequestId,
            updateJobStatusAndRecordCount;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_job")
                .withReturnValue();
        findAllPaginated = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_jobs")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Job.class))
                .returningResultSet(RESULT_COUNT, new RowCountMapper());
        findByRequestId = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_find_job_by_request_id")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Job.class));
        updateJobStatus = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_job_status")
                .withReturnValue();
        updateJobStatusAndRecordCount = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_job_status_and_record_count")
                .withReturnValue();
    }

    public void updateJobStatus(Integer id, JobStatus jobStatus, String statusMessage) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("id", id).addValue("job_status", jobStatus).addValue("status_message", statusMessage);
        this.updateJobStatus.execute(in);
    }

    public void updateJobStatusAndRecordCount(Integer id, JobStatus jobStatus, Integer recordCount) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("id", id).addValue("job_status", jobStatus).addValue("record_count", recordCount);
        this.updateJobStatusAndRecordCount.execute(in);
    }

    public Page<Job> findAll(Integer pageNum, Integer pageSize, String status) throws DataAccessException {

        SqlParameterSource in = (new MapSqlParameterSource())
                .addValue(PAGE, pageNum <= 0 ? 1 : pageNum)
                .addValue(PAGE_SIZE, pageSize <= 0 ? 10 : pageSize)
                .addValue("status", status == null ? "" : status);

        Map<String, Object> m = this.findAllPaginated.execute(in);
        List<Job> content = (List<Job>) m.get(MULTIPLE_RESULT);
        List<Long> counts = (List<Long>) m.get(RESULT_COUNT);
        Long count = counts.isEmpty() ? 0 : (Long) counts.get(0);
        return new Page<>(count, content);
    }

    public Job findByRequestId(String requestId) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("request_id", requestId);
        Map<String, Object> m = this.findByRequestId.execute(in);
        List<Job> result = (List<Job>) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? result.get(0) : null;
    }
}
