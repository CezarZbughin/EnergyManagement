package com.cezar.energychat.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ChatMessageWebSocketDTO {
    String type; // = MESSAGE | SEEN | TYPING
    String message = "";
    long senderId;
}
