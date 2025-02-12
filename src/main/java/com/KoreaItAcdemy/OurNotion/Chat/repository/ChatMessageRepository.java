package com.KoreaItAcdemy.OurNotion.Chat.repository;

import com.KoreaItAcdemy.OurNotion.Chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomId(String chatRoomId);  // 🔥 ChatRoom 객체 대신 String 사용
    List<ChatMessage> findByChatRoomIdOrderBySendTimeAsc(String chatRoomId);
}
