package com.tima.dao;

import com.tima.model.Transaction;
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
public class TransactionDao extends BaseDao<Transaction> {
    SimpleJdbcCall findByTransactionRef;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_create_transaction")
                .withReturnValue();
        findById = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_transaction_by_id")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Transaction.class));
        findByTransactionRef = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_transaction_by_ref")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Transaction.class));
        findAllPaginated = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_fetch_transactions")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Transaction.class))
                .returningResultSet(RESULT_COUNT, new RowCountMapper());
        update = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("psp_update_transaction_status")
                .withReturnValue();
    }

    public Transaction findByTransactionRef(String transactionRef) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("transaction_ref", transactionRef);
        Map<String, Object> m = this.findByTransactionRef.execute(in);
        List<Transaction> result = (List<Transaction>) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? result.get(0) : null;
    }
}
