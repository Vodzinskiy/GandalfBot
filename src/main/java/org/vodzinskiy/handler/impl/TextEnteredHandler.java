package org.vodzinskiy.handler.impl;

import org.springframework.stereotype.Component;
import org.vodzinskiy.enums.ConversationState;
import org.vodzinskiy.handler.UserRequestHandler;
import org.vodzinskiy.model.UserRequest;
import org.vodzinskiy.model.UserSession;
import org.vodzinskiy.service.TelegramService;
import org.vodzinskiy.service.UserService;
import org.vodzinskiy.service.UserSessionService;

@Component
public class TextEnteredHandler extends UserRequestHandler {

    private final TelegramService telegramService;

    private final UserSessionService userSessionService;
    private final UserService userService;


    public TextEnteredHandler(TelegramService telegramService, UserSessionService userSessionService, UserService userService) {
        this.telegramService = telegramService;
        this.userSessionService = userSessionService;
        this.userService = userService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_TEXT.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest request) {
        if (userService.findByUserName("@" + request.getUpdate().getMessage().getFrom().getUserName()) != null) {
            telegramService.sendMessage(request.getChatId(), "Answer for question: " + request.getUpdate().getMessage().getText());
        } else {
            telegramService.sendMessage(request.getChatId(), "Sorry,\nyou are not on the whitelist");
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
