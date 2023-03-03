package org.vodzinskiy.handler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.vodzinskiy.enums.ConversationState;
import org.vodzinskiy.handler.UserRequestHandler;
import org.vodzinskiy.model.UserRequest;
import org.vodzinskiy.model.UserSession;
import org.vodzinskiy.service.TelegramService;
import org.vodzinskiy.service.UserSessionService;

@Component
public class UserHundler extends UserRequestHandler {
    private final TelegramService telegramService;
    private final UserSessionService userSessionService;

    public UserHundler(TelegramService telegramService, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && (ConversationState.WAITING_FOR_USER_ADD.equals(userRequest.getUserSession().getState())
                || ConversationState.WAITING_FOR_USER_REMOVE.equals(userRequest.getUserSession().getState()));
    }

    @Override
    public void handle(UserRequest request) {
        System.out.println("---");
        /*UserSession session = request.getUserSession();
        session.setState(ConversationState.WAITING_FOR_TEXT);
        userSessionService.saveSession(request.getChatId(), session);*/
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
