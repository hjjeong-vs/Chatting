package com.KoreaItAcdemy.OurNotion.Chat.controller;

import com.KoreaItAcdemy.OurNotion.Chat.entity.ChatMessage;
import com.KoreaItAcdemy.OurNotion.Chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/test-messagedb")
    public ResponseEntity<String> testDatabase() {
        try {
            return ResponseEntity.ok("DB 연결 성공! 메시지 수: " + chatService.getMessagesByRoomId("testRoom").size());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("DB 연결 실패: " + e.getMessage());
        }
    }

    // ✅ 특정 채팅방의 메시지 가져오기
    @GetMapping("/messages/{chatRoomId}")
    public ResponseEntity<List<ChatMessage>> getMessagesByRoom(@PathVariable String chatRoomId) {
        List<ChatMessage> messages = chatService.getMessagesByRoom(chatRoomId);
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/messages")
    public ResponseEntity<String> saveMessage(
            @RequestParam String roomId,
            @RequestParam int sender,
            @RequestParam String message) {
        try {
            chatService.saveMessage(roomId, sender, message);
            return ResponseEntity.ok("메시지 저장 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("메시지 저장 실패: " + e.getMessage());
        }
    }
}
