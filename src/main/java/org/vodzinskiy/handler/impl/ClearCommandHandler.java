package org.vodzinskiy.handler.impl;

import org.springframework.stereotype.Component;
import org.vodzinskiy.handler.UserRequestHandler;
import org.vodzinskiy.model.UserRequest;
import org.vodzinskiy.model.UserSession;
import org.vodzinskiy.service.ChatGPTService;
import org.vodzinskiy.service.TelegramService;
import org.vodzinskiy.service.UserService;
import org.vodzinskiy.service.UserSessionService;
@Component
public class ClearCommandHandler extends UserRequestHandler {
    private static final String command = "/clear";

    private final TelegramService telegramService;
    private final UserSessionService userSessionService;
    private final UserService userService;
    private final ChatGPTService chatGPTService;


    public ClearCommandHandler(TelegramService telegramService, UserSessionService userSessionService, UserService userService, ChatGPTService chatGPTService) {
        this.telegramService = telegramService;
        this.userSessionService = userSessionService;
        this.userService = userService;
        this.chatGPTService = chatGPTService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), command);
    }

    @Override
    public void handle(UserRequest request) {
        if (userService.findByUserName("@" + request.getUpdate().getMessage().getFrom().getUserName()) != null) {
            chatGPTService.clear();
        } else {
            telegramService.sendMessage(request.getChatId(), "Sorry,\nyou are not on the whitelist");
        }

        UserSession session = request.getUserSession();
        session.setState(null);
        userSessionService.saveSession(request.getChatId(), session);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
