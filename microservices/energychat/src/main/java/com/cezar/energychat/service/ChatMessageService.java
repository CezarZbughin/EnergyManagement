package com.cezar.energychat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageService {
    @Autowired
    public SimpMessagingTemplate template;


}
