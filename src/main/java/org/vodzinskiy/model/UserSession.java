package org.vodzinskiy.model;

import lombok.Builder;
import lombok.Data;
import org.vodzinskiy.enums.ConversationState;

@Data
@Builder
public class UserSession {
    private Long chatId;
    private String userName;
    private String text;
    private ConversationState state;
}
