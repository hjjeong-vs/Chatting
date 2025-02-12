package com.KoreaItAcdemy.OurNotion.Chat.service;

import com.KoreaItAcdemy.OurNotion.Chat.entity.ChatMessage;
import com.KoreaItAcdemy.OurNotion.Chat.entity.ChatRoom;
import com.KoreaItAcdemy.OurNotion.Chat.repository.ChatMessageRepository;
import com.KoreaItAcdemy.OurNotion.Chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 🔥 ✅ [ChatRoom 생성 기능 복구]
    @Transactional
    public ChatRoom createChatRoom(String name, String chatuserInfo, String participants) {
        String roomId = UUID.randomUUID().toString(); // 고유한 채팅방 ID 생성
        ChatRoom chatRoom = new ChatRoom(name, chatuserInfo, participants);
        return chatRoomRepository.save(chatRoom);
    }

    // 기존 채팅방 목록 가져오기
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    // 기존 메시지 저장 기능 유지
    @Transactional
    public void saveMessage(String roomId, int sender, String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomId(roomId);  // ✅ String 타입의 roomId 사용
        chatMessage.setSender(sender);
        chatMessage.setMessage(message);
        chatMessage.setSendTime(LocalDateTime.now());
        chatMessageRepository.save(chatMessage);
        chatMessageRepository.flush(); // 🔥 DB에 즉시 반영

        System.out.println("✅ [DEBUG] saveMessage() - 메시지가 DB에 저장될 예정");
        
    }

    public List<ChatMessage> getMessagesByRoomId(String roomId) {
        return chatMessageRepository.findByChatRoomId(roomId);
    }

    public List<ChatMessage> getMessagesByRoom(String chatRoomId) {
        return chatMessageRepository.findByChatRoomIdOrderBySendTimeAsc(chatRoomId);
    }


}
