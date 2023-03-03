package org.vodzinskiy.handler.impl;

import org.springframework.stereotype.Component;
import org.vodzinskiy.enums.ConversationState;
import org.vodzinskiy.handler.UserRequestHandler;
import org.vodzinskiy.model.User;
import org.vodzinskiy.model.UserRequest;
import org.vodzinskiy.model.UserSession;
import org.vodzinskiy.service.TelegramService;
import org.vodzinskiy.service.UserSessionService;
import org.vodzinskiy.service.UserService;

@Component
public class UserHandler extends UserRequestHandler {
    private final TelegramService telegramService;
    private final UserSessionService userSessionService;
    private final UserService userService;

    public UserHandler(TelegramService telegramService, UserSessionService userSessionService, UserService userService) {
        this.telegramService = telegramService;
        this.userSessionService = userSessionService;
        this.userService = userService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && (ConversationState.WAITING_FOR_USER_ADD.equals(userRequest.getUserSession().getState())
                || ConversationState.WAITING_FOR_USER_REMOVE.equals(userRequest.getUserSession().getState()));
    }

    @Override
    public void handle(UserRequest request) {
        if (ConversationState.WAITING_FOR_USER_ADD.equals(request.getUserSession().getState())) {
            try {
                userService.saveUser(new User(request.getUpdate().getMessage().getText()));
                telegramService.sendMessage(request.getChatId(), "User added:\n  UserName: "
                        + request.getUpdate().getMessage().getText());
            } catch (Exception e) {
                telegramService.sendMessage(request.getChatId(), "Error");
            }
        } else {
            try {
                userService.deleteUser(userService.findByUserName(request.getUpdate().getMessage().getText()));
                telegramService.sendMessage(request.getChatId(), "User deleted:\n  UserName: "
                        + request.getUpdate().getMessage().getText());
            } catch (Exception e) {
                telegramService.sendMessage(request.getChatId(), "Error");
            }
        }

        UserSession session = request.getUserSession();
        session.setState(ConversationState.WAITING_FOR_TEXT);
        userSessionService.saveSession(request.getChatId(), session);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
