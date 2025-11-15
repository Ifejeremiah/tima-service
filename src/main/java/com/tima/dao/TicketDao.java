package com.tima.dao;

import com.tima.dto.TicketSummary;
import com.tima.model.Page;
import com.tima.model.Ticket;
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
public class TicketDao extends BaseDao<Ticket> {
    SimpleJdbcCall findTicketSummary;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_ticket")
                .withReturnValue();
        findById = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_ticket_by_id")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Ticket.class));
        findAllPaginated = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_tickets")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Ticket.class))
                .returningResultSet(RESULT_COUNT, new RowCountMapper());
        findTicketSummary = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_ticket_summary")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(TicketSummary.class));
        update = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_tickets")
                .withReturnValue();
    }

    public Page<Ticket> findAll(Integer pageNum, Integer pageSize, String searchQuery, String status, String priority, String category) throws DataAccessException {

        SqlParameterSource in = (new MapSqlParameterSource())
                .addValue(PAGE, pageNum <= 0 ? 1 : pageNum)
                .addValue(PAGE_SIZE, pageSize <= 0 ? 10 : pageSize)
                .addValue(SEARCH_QUERY, searchQuery == null ? "" : searchQuery)
                .addValue("status", status == null ? "" : status)
                .addValue("priority", priority == null ? "" : priority)
                .addValue("category", category == null ? "" : category);
        Map<String, Object> m = this.findAllPaginated.execute(in);
        List<Ticket> content = (List<Ticket>) m.get(MULTIPLE_RESULT);
        List<Long> counts = (List<Long>) m.get(RESULT_COUNT);
        Long count = counts.isEmpty() ? 0 : (Long) counts.get(0);
        return new Page<>(count, content);
    }

    public TicketSummary findTicketSummary() throws DataAccessException {
        Map<String, Object> m = this.findTicketSummary.execute();
        List<TicketSummary> result = (List<TicketSummary>) m.get(SINGLE_RESULT);
        return result.get(0);
    }
}
