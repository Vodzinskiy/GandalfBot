package org.vodzinskiy.handler.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.vodzinskiy.handler.UserRequestHandler;
import org.vodzinskiy.model.UserRequest;
import org.vodzinskiy.model.UserSession;
import org.vodzinskiy.service.TelegramService;
import org.vodzinskiy.service.UserSessionService;
import org.vodzinskiy.enums.ConversationState;

import java.util.Objects;

@Component
public class AddCommandHandler extends UserRequestHandler {
    @Value("${admin}")
    private String admin;

    private static final String command = "/add";

    private final TelegramService telegramService;
    private final UserSessionService userSessionService;


    public AddCommandHandler(TelegramService telegramService, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), command);
    }

    @Override
    public void handle(UserRequest request) {
        if (Objects.equals(request.getUpdate().getMessage().getFrom().getUserName(), admin)) {
            telegramService.sendMessage(request.getChatId(), "Enter Username:");
            String user = request.getUpdate().getMessage().getText();
            UserSession session = request.getUserSession();
            session.setUserName(user);
            session.setState(ConversationState.WAITING_FOR_USER_ADD);
            userSessionService.saveSession(request.getChatId(), session);
        } else {
            telegramService.sendMessage(request.getChatId(), "Sorry, you don't have permission to perform this action");
        }

    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}

