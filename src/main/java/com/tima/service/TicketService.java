package com.tima.service;

import com.tima.dao.TicketDao;
import com.tima.dto.TicketCreateRequest;
import com.tima.dto.TicketSummary;
import com.tima.dto.TicketUpdateRequest;
import com.tima.enums.TicketPriority;
import com.tima.enums.TicketStatus;
import com.tima.exception.NotFoundException;
import com.tima.model.AdminUser;
import com.tima.model.Page;
import com.tima.model.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TicketService extends BaseService {
    TicketDao ticketDao;
    AdminUserService adminUserService;

    public TicketService(TicketDao ticketDao, AdminUserService adminUserService) {
        this.ticketDao = ticketDao;
        this.adminUserService = adminUserService;
    }

    public void create(TicketCreateRequest request) {
        try {
            Ticket ticket = new Ticket();
            BeanUtils.copyProperties(request, ticket);
            ticket.setPriority(TicketPriority.valueOf(request.getPriority()));
            ticket.setCreatedBy(fetchCurrentUserEmail());
            ticketDao.create(ticket);
        } catch (Exception error) {
            log.error("Error creating ticket", error);
            throw error;
        }
    }

    public Page<Ticket> findAll(int page, int size, String searchQuery, String status, String priority, String category) {
        try {
            return ticketDao.findAll(page, size, searchQuery, status, priority, category);
        } catch (Exception error) {
            log.error("Error fetching all tickets", error);
            throw error;
        }
    }

    public Ticket findById(int id) {
        try {
            Ticket ticket = ticketDao.find(id);
            if (ticket == null) throw new NotFoundException("Could not find ticket with ticket id " + id);
            if (ticket.getAssignedTo() != null) {
                AdminUser adminUser = adminUserService.findById(ticket.getAssignedTo());
                ticket.setAssigneeEmail(adminUser.getEmail());
            }
            return ticket;
        } catch (Exception error) {
            log.error("Error fetching ticket", error);
            throw error;
        }
    }

    public void update(int id, TicketUpdateRequest request) {
        try {
            Ticket existing = this.findById(id);
            adminUserService.findById(request.getAssignedTo());
            existing.setStatus(TicketStatus.valueOf(request.getStatus()));
            existing.setAssignedTo(request.getAssignedTo());
            existing.setLastUpdatedBy(fetchCurrentUserEmail());
            ticketDao.update(existing);
        } catch (Exception error) {
            log.error("Error updating ticket", error);
            throw error;
        }
    }

    public TicketSummary findTicketSummary() {
        try {
            return ticketDao.findTicketSummary();
        } catch (Exception error) {
            log.error("Error fetching ticket summary", error);
            throw error;
        }
    }
}
