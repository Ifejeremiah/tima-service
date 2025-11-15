package com.tima.dao;

import com.tima.model.Page;
import com.tima.model.TicketChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class TicketChatDao extends BaseDao<TicketChat> {
    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_ticket_chat")
                .withReturnValue();
        findAllPaginated = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_ticket_chats")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(TicketChat.class))
                .returningResultSet(RESULT_COUNT, new RowCountMapper());
    }

    public Page<TicketChat> findAll(Integer pageNum, Integer pageSize, int ticketId) throws DataAccessException {
        MapSqlParameterSource in = (new MapSqlParameterSource())
                .addValue(PAGE, pageNum <= 0 ? 1 : pageNum)
                .addValue(PAGE_SIZE, pageSize <= 0 ? 10 : pageSize)
                .addValue("ticket_id", ticketId);
        Map<String, Object> m = this.findAllPaginated.execute(in);
        List<TicketChat> content = (List<TicketChat>) m.get(MULTIPLE_RESULT);
        List<Long> counts = (List<Long>) m.get(RESULT_COUNT);
        Long count = counts.isEmpty() ? 0 : (Long) counts.get(0);

        return new Page<>(count, content);
    }
}
