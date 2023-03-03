package org.vodzinskiy.handler.impl;

import org.springframework.stereotype.Component;
import org.vodzinskiy.handler.UserRequestHandler;
import org.vodzinskiy.model.UserRequest;
import org.vodzinskiy.service.TelegramService;
import org.vodzinskiy.service.UserService;

@Component
public class StartCommandHandler extends UserRequestHandler {

    private static final String command = "/start";

    private final TelegramService telegramService;
    private final UserService userService;

    public StartCommandHandler(TelegramService telegramService, UserService userService) {
        this.telegramService = telegramService;
        this.userService = userService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), command);
    }

    @Override
    public void handle(UserRequest request) {
        System.out.println(userService.findByUserName("@" + request.getUpdate().getMessage().getFrom().getUserName()));
        if (userService.findByUserName("@" + request.getUpdate().getMessage().getFrom().getUserName()) != null) {
            telegramService.sendMessage(request.getChatId(), "\uD83D\uDC4B");
        } else {
            telegramService.sendMessage(request.getChatId(), "Sorry,\nyou are not on the whitelist");
        }
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
