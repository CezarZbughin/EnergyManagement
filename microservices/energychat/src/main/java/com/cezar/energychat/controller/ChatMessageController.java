package com.cezar.energychat.controller;

import com.cezar.energychat.DTO.ChatMessageRequestDTO;
import com.cezar.energychat.DTO.ChatMessageWebSocketDTO;
import com.cezar.energychat.configuration.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class ChatMessageController {
    @Autowired
    public JwtUtil jwtUtil;

    @Autowired
    public SimpMessagingTemplate template;

    @GetMapping("/hello")
    public ResponseEntity<?> hello(){
        return ResponseEntity.ok("owo");
    }

    @PostMapping("/api/v1/chat-message")
    public ResponseEntity<?> postChatMessage(HttpServletRequest request, @RequestBody ChatMessageRequestDTO chatMessageRequestDTO) {
        Claims claims = jwtUtil.resolveClaims(request);
        int userId = (Integer)claims.get("userid");

        ChatMessageWebSocketDTO chatMessageWebSocketDTO = new ChatMessageWebSocketDTO();
        chatMessageWebSocketDTO.setType("MESSAGE");
        chatMessageWebSocketDTO.setMessage(chatMessageRequestDTO.getMessage());
        chatMessageWebSocketDTO.setSenderId(userId);
        template.convertAndSend("/topic/chat/" + chatMessageRequestDTO.getReceiverId(), chatMessageWebSocketDTO);
        return ResponseEntity.ok(new WebMessage("Success!"));
    }

    @GetMapping("/api/v1/chat-message/typing/{toWhoId}")
    public ResponseEntity<?> typing(HttpServletRequest request, @PathVariable long toWhoId) {
        Claims claims = jwtUtil.resolveClaims(request);
        int userId = (Integer)claims.get("userid");

        ChatMessageWebSocketDTO chatMessageWebSocketDTO = new ChatMessageWebSocketDTO();
        chatMessageWebSocketDTO.setType("TYPING");
        chatMessageWebSocketDTO.setSenderId(userId);
        template.convertAndSend("/topic/chat/" + toWhoId, chatMessageWebSocketDTO);
        return ResponseEntity.ok(new WebMessage("Success!"));
    }

    @GetMapping("/api/v1/chat-message/seen/{whoseId}")
    public ResponseEntity<?> seen(HttpServletRequest request, @PathVariable long whoseId) {
        Claims claims = jwtUtil.resolveClaims(request);
        int userId = (Integer)claims.get("userid");

        ChatMessageWebSocketDTO chatMessageWebSocketDTO = new ChatMessageWebSocketDTO();
        chatMessageWebSocketDTO.setType("SEEN");
        chatMessageWebSocketDTO.setSenderId(userId);
        template.convertAndSend("/topic/chat/" + whoseId, chatMessageWebSocketDTO);
        return ResponseEntity.ok(new WebMessage("Success!"));
    }
}
