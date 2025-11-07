package com.tima.service;

import com.tima.dao.TransactionDao;
import com.tima.exception.NotFoundException;
import com.tima.model.Page;
import com.tima.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class TransactionService extends BaseService {
    TransactionDao transactionDao;

    public TransactionService(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    public void create(Transaction transaction) {
        try {
            transaction.setTransactionRef("TXN0" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
            transactionDao.create(transaction);
        } catch (Exception error) {
            log.error("Error creating transaction", error);
            throw error;
        }
    }

    public Page<Transaction> findAll(int page, int size, String searchQuery) {
        try {
            return transactionDao.findAll(page, size, searchQuery);
        } catch (Exception error) {
            log.error("Error fetching all transactions", error);
            throw error;
        }
    }

    public Transaction findById(int id) {
        try {
            Transaction transaction = transactionDao.find(id);
            if (transaction == null)
                throw new NotFoundException("Could not find transaction with transaction id " + id);
            return transaction;
        } catch (Exception error) {
            log.error("Error fetching transaction", error);
            throw error;
        }
    }

    public Transaction findByTransactionRef(String transactionRef) {
        try {
            Transaction transaction = transactionDao.findByTransactionRef(transactionRef);
            if (transaction == null)
                throw new NotFoundException("Could not find transaction with transaction reference " + transactionRef);
            return transaction;
        } catch (Exception error) {
            log.error("Error fetching transaction by ref", error);
            throw error;
        }
    }

    public void update(int id, String status) {
        try {
            Transaction existing = this.findById(id);
            existing.setStatus(status);
            transactionDao.update(existing);
        } catch (Exception error) {
            log.error("Error updating transaction", error);
            throw error;
        }
    }
}
