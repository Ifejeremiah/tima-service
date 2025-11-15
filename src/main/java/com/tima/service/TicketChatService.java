package com.tima.service;

import com.tima.dao.TicketChatDao;
import com.tima.dto.CurrentUserResponse;
import com.tima.model.Page;
import com.tima.model.TicketChat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TicketChatService extends BaseService {
    TicketChatDao ticketChatDao;
    TicketService ticketService;
    UserService userService;

    public TicketChatService(TicketChatDao ticketChatDao, TicketService ticketService, UserService userService) {
        this.ticketChatDao = ticketChatDao;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    public void create(TicketChat request) {
        try {
            ticketService.findById(request.getTicketId());
            request.setUserId(fetchCurrentUserId());
            CurrentUserResponse user = userService.findByCurrentUser();
            request.setName(user.getAdminUser() != null ? user.getAdminUser().getFirstName() : user.getStudent().getName());
            request.setEmail(fetchCurrentUserEmail());
            ticketChatDao.create(request);
        } catch (Exception error) {
            log.error("Error creating ticket chat", error);
            throw error;
        }
    }

    public Page<TicketChat> findAll(int page, int size, int ticketId) {
        try {
            ticketService.findById(ticketId);
            return ticketChatDao.findAll(page, size, ticketId);
        } catch (Exception error) {
            log.error("Error fetching all ticket chat", error);
            throw error;
        }
    }
}
