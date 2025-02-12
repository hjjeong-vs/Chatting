package com.KoreaItAcdemy.OurNotion.Chat.controller;

import com.KoreaItAcdemy.OurNotion.Chat.dto.ChatRoomRequestDto;
import com.KoreaItAcdemy.OurNotion.Chat.entity.ChatRoom;
import com.KoreaItAcdemy.OurNotion.Chat.repository.ChatRoomRepository;
import com.KoreaItAcdemy.OurNotion.Chat.service.ChatRoomService;
import com.KoreaItAcdemy.OurNotion.Chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    //test
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/test-db")
    public ResponseEntity<String> testDatabase() {
        try {
            long count = chatRoomRepository.count();
            return ResponseEntity.ok("DB 연결 성공! 채팅방 개수: " + count);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("DB 연결 실패: " + e.getMessage());
        }
    }

    /// ////////////

    private final ChatService chatService;

    // ✅ @RequestBody를 사용하여 JSON 데이터를 받도록 변경
    @PostMapping("/room")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoomRequestDto requestDto) {
        ChatRoom chatRoom = chatService.createChatRoom(
                requestDto.getName(),
                requestDto.getChatuserInfo(),
                requestDto.getParticipants()
        );
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> getRooms() {
        return ResponseEntity.ok(chatService.getAllChatRooms());
    }


    @Autowired
    private ChatRoomService chatRoomService; // 서비스 레이어 주입

    // 특정 채팅방의 Room Name 가져오기
    @GetMapping("/room/{roomId}")
    public ResponseEntity<String> getRoomName(@PathVariable String roomId) {
        String roomName = chatRoomService.getRoomNameById(roomId);
        if (roomName != null) {
            return ResponseEntity.ok(roomName);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
        }
    }
}
