package com.KoreaItAcademy.chat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    // 채팅방을 저장할 Map (ID -> ChatRoom 객체)
    private final Map<String, ChatRoom> chatRooms = new HashMap<>();

    // 채팅방 생성 API
    @PostMapping("/rooms")
    public ResponseEntity<String> createRoom(@RequestParam String name) {
        ChatRoom newRoom = new ChatRoom(name);
        chatRooms.put(newRoom.getId(), newRoom);
        return ResponseEntity.ok(newRoom.getId()); // 생성된 채팅방 ID 반환
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<Map<String, String>>> getRooms() {
        return ResponseEntity.ok(
                chatRooms.values().stream()
                        .map(room -> Map.of(
                                "id", room.getId(),
                                "name", room.getName(),
                                "creationDate", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(room.getCreationDate())
                        ))
                        .collect(Collectors.toList())
        );
    }
}
